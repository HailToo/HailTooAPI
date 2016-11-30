Game = {
	_user: {},
	_rooms: [],
	_hallways: [],
	_suspects: [],
	isLoaded: false,
		
	// Gameboard layout
	board: {
		// Dimension of each tile (in pixels)
		tile: {
			width: 15,
			height: 15
		},
		
		// Dimensions of board (in tiles)
		width: 40,
		height: 40
	},
	
	// width of game board (in pixels)
	width: function() {
		return (this.board.width * this.board.tile.width); 
	},
	
	// height of game board (in pixels)
	height: function() {
		return (this.board.height * this.board.tile.height);
	},

	init: function() {
		console.log("loaded.");
			
		//Show current version
		try {
			$('#version').text(GameService.getVersion())
		} catch (err) {
			console.log("failed to get version information.");
		}
		
		Game._user.name = window.prompt("Enter username", "");
		var authToken = GameService.login({ username: Game._user.name }).done(function(data) {
			console.log("Auth token received: " + data.token);
  			GameService.setAuthToken(data.token);	
		});
		
		GameService.characters().done(function(data) {
			document.characters = data;
		});
	},
	
	moveToArea: function(actor, areaName) {
		var targetRoom = this._rooms.filter(function(r) {
			return r.attr("name") === areaName;
		});
		
		//TODO: adjust if multiple occupants in the room.
		if(targetRoom.length === 1) {
			targetRoom = targetRoom[0];
			//move player within the bounds of this room.
			var targetX = (targetRoom.x + (targetRoom.w / 2)) / Game.board.tile.width;
			var targetY = targetRoom.y / Game.board.tile.height;
			actor.at(targetX, targetY);
		} else {
			//Check hallways
			targetRoom = this._hallways.filter(function(h) {
				return h.attr("name") === areaName;
			});
			if(targetRoom !== null && targetRoom.length === 1) {
				targetRoom = targetRoom[0];
				//move player within the bounds of this room. 
				var targetX = targetRoom.x / Game.board.tile.width;
				var targetY = (targetRoom.y - (actor.h / 2)) / Game.board.tile.height;
				actor.at(targetX, targetY);
			}
		}
		
	},
	
	pollGameState: function() {
		GameService.getGameState(document.gameState.name).done(function(data) {
			try {
				if(JSON.stringify(document.gameState) !== JSON.stringify(data)) {
					console.log("game state has changed!");
					// HACK - Don't try to draw on the board until all objects on the board are defined/drawn.
					// This is a [bad] solution to a race-condition with scene loading and how we draw the game board.
					if (Game.isLoaded) {
						Game.updateDisplay(data);
					}
				}
			} catch(err) {
				console.log("error: " + err);
			}
			
			setTimeout(Game.pollGameState, 3000);
		});
	},
	
	updateDisplay: function(gameState) {
		//Move all players to their appropriate positions
		gameState.board.locations.forEach(function (location) {
			location.occupants.forEach(function(player) {
				var actor = Game.getActor(player.character);
				if(actor === undefined || actor === null) {
					//Add new suspect (must've joined since last state change).
					actor = CraftyHelper.createCharacter(player.character);
					Game._suspects.push(actor);
				} 

				//Move actor
				Game.moveToArea(actor, location.name);
				
				// Current player
				if (player.name === Game._user.name) {
					//Update hand on screen
					NotificationHelper.populateHand(player.cards);
					
					Game._user.cards = player.cards;
				}
			});
		});
		
		//show new messages
		for (var i = $('#tail').children().length; i < gameState.messages.length; ++i) {
			NotificationHelper.pushMessage(gameState.messages[i]);
		}
	
		document.gameState = gameState;
		
		//Prompt player for move/suggestion
		if (gameState.status === "Active" && Game._user.character === gameState.currentPlayer.character) {
			if (gameState.currentPlayer.availableActions.indexOf('Wait') >= 0) {
				Game.promptWait();
			} else {
				Game.prompt();
				//Enable/disable action buttons
				disableMove = gameState.currentPlayer.availableActions.indexOf('Move') == -1;
				disableSuggest = gameState.currentPlayer.availableActions.indexOf('Suggest') == -1;
				disableAccuse = gameState.currentPlayer.availableActions.indexOf('Accuse') == -1;
				
				$('#b_promptMove').prop('disabled', disableMove);
				$('#b_promptSuggest').prop('disabled', disableSuggest);
				$('#b_promptAccuse').prop('disabled', disableAccuse);
			}
		} else {
			$('#hail_prompt').modal("hide");
			$('#hail_suggestion').modal("hide");
			$('#hail_move').modal("hide");
			$('#hail_wait').modal("hide");
			
			//Check for available actions for self
			for(var i = 0; i < gameState.players.length; ++i) {
				if (gameState.players[i].character === Game._user.character && gameState.players[i].availableActions.indexOf('Disprove') >= 0) {
					//Prompt user to disprove suggest!
					console.log("Prompt to disprove suggestion!");
					Game.promptDisprove();
				}
			}
		}
	},
	
	getActor: function(characterName) {
		var ret = null;
		Game._suspects.forEach(function(actor) {
			if (actor !== null && actor.attr("name") === characterName) {
				ret = actor;
			}
		});
		
		return ret;
	},
	
	prompt: function() {
		//Splash.load_scene("Prompt");
		// Toggle main modal ON
		$('#hail_prompt').modal("show");
	},
	
	promptMove: function() {
		console.log("Displaying modal for user to MOVE their gamepiece.");
		// Toggle main modal OFF
		$('#hail_prompt').modal("hide");
		
		// Toggle move modal ON
		$('#hail_move').modal("show");
		
		// Get available moves, populate list
		GameService.getMoves(document.gameState.name).done(function(data){
			if (data !== null && data.length > 0) {
				GeneralHelper.populateDropdown('select.moves', data);
			} else {
				console.log("no available moves were returned!");
			}
		});
		
		//
	},
	
	promptSuggestion: function() {
		console.log("Displaying modal for user to MAKE A SUGGESTION at solving the mystery.");
		// Toggle main modal OFF
		$('#hail_prompt').modal("hide");
		
		// Toggle suggestion modal ON
		$('#hail_suggestion').modal("show");
		
		// TODO - pre-load current room in list.
		GeneralHelper.populateDropdown('select.rooms', [ 'current room']);
		// Get available suspects, populate list
		GameService.characters().done(function(data){
			GeneralHelper.populateDropdown('select.suspects', data);
		});

		// Get available weapons, populate list
		GameService.weapons().done(function(data){
			GeneralHelper.populateDropdown('select.weapons', data);
		});
	},
	
	promptDisprove: function() {
		console.log("Displaying modal for user to DISPROVE A SUGGESTION at solving the mystery.");
		
		// Toggle disprove modal ON
		$('#hail_disprove').modal("show");
		
		var challengeCards = [document.gameState.currentSuggestion.room, document.gameState.currentSuggestion.suspect, document.gameState.currentSuggestion.weapon];
		for(var i = 0; i < challengeCards.length; ++i) {
			if(Game._user.cards.indexOf(challengeCards[i]) === -1) {
				challengeCards.splice(i, 1);
				--i;
			}
		}
		if (challengeCards.length === 0) {
			challengeCards.push('Cannot disprove');
		}
		
		GeneralHelper.populateDropdown('select.challenge', challengeCards);
	},
	
	promptAccusation: function() {
		//send up guess
	},
	
	promptWait: function() {
		$('#hail_prompt').modal("hide");
		$('#hail_suggestion').modal("hide");
		$('#hail_disprove').modal("hide");
		$('#hail_move').modal("hide");
		$('#hail_wait').modal("show");
	},
	
	doMove: function() {
		// Get user's choice
		var moveTo = $('select.moves').val();
		GameService.move(document.gameState.name, moveTo).done(function(data) {
			if (data) {
				$('#hail_move').modal("hide");
				console.log("user has moved!")
			}
		});
	},
	
	doSuggestion: function() {
		// Get user's choices
		var room = document.gameState.board.locations.filter(function(l) { 
			for(var i = 0; i < l.occupants.length; ++i) {
				if(l.occupants[i].character === Game._user.character) {
					return l;
				}
			}
		});
		var weapon = $('select.weapons').val();
		var suspect = $('select.suspects').val();
		
		GameService.suggest(document.gameState.name, room[0].name, weapon, suspect).done(function(data) {
			if (data) {
				console.log("player guessed successfully.");
			} else {
				//TODO: disable "accuse" button.
				console.log("player guessed incorrectly.");
			}
		});
		
	},
	
	doDisprove: function() {
		var challengeItem = $('select.challenge').val();
		
		GameService.disprove(document.gameState.name, challengeItem).done(function(data) {
			if (data) {
				console.log("disproval submitted.");
				$('#hail_disprove').modal("hide");
			}
		});
	}
	
}