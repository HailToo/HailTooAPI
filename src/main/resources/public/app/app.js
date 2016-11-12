Game = {
	_user: {},
	_rooms: [],
	_hallways: [],
	_suspects: [],
		
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
		// Register components of the game
		Crafty.c('Grid', Components.grid);
		Crafty.c('Actor', Components.actor);
		Crafty.c('Player', Components.player);
		Crafty.c('Edge', Components.edge);
		Crafty.c('Room', Components.room);
		Crafty.c('Hall', Components.hall);
		Crafty.c('Suspect', Components.suspect);
		
		// Create board (grid, rooms, hallways)
		CraftyHelper.drawBoard();
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
			if(targetRoom !== null) {
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
			if(JSON.stringify(document.gameState) !== JSON.stringify(data)) {
				console.log("game state has changed!");
				Game.updateDisplay(data);
			}
			
			setTimeout(Game.pollGameState, 5000);
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
	
	finishSetup: function(data) {
		
	},
}