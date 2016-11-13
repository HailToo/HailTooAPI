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
		//NotificationHelper.modalAlert("HailToo " + GameService.getVersion(), 5000);
		
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
				}
			});
		});
		
		document.gameState = gameState;
		
		//Prompt player for move/suggestion
		if (gameState.isActive === true && Game._user.character === gameState.currentPlayer.character) {
			Game.prompt();
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
				data.forEach(function(element) {
					console.log("adding move: " + element);
					$('select.moves').append($("<option></option>")
		                    .attr("value", element)
		                    .text(element));
				})
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
	
	doMove: function() {
		// Get user's choice
		var moveTo = $('select.moves').val()
		GameService.move(document.gameState.name, moveTo).done(function(data) {
			if (data) {
				$('#hail_move').modal("hide");
				console.log("user has moved!")
			}
		});
	}
	
}