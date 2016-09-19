Game = {
	// Gameboard layout
	board: {
		// Dimension of each tile (in pixels)
		tile: {
			width: 15,
			height: 15
		},
		
		// Dimensions of board (in tiles)
		width: 30,
		height: 30
	},

	init: function() {
		// Initialize game
		Crafty.init(Game.width(), Game.height(), document.getElementById('game'));
		
		// Background
		Crafty.background('green');
		
		// Register components of the game
		Crafty.c('Grid', Components.grid);
		Crafty.c('Actor', Components.actor);
		Crafty.c('Player', Components.player);
		Crafty.c('Edge', Components.edge);
		Crafty.c('Room', Components.room);
		Crafty.c('Hall', Components.hall);
		Crafty.c('Suspect', Components.suspect);
		
		// Create entity (player object)
		Crafty.e('Player').at(5, 5);
		
		Game.drawBoard();
	
	},
	
	// width of game board (in pixels)
	width: function() {
		return (this.board.width * this.board.tile.width); 
	},
	
	// height of game board (in pixels)
	height: function() {
		return (this.board.height * this.board.tile.height);
	},
	
	drawBoard: function() {
		// Traverse the entire board (per tile)
		for (var x = 0; x < this.board.width; ++x) {
			for (var y = 0; y < this.board.height; ++y) {
//				if (x % 3 === 0 && y % 3 === 0) {
//					// Draw either a room or hallway
//					var room = Crafty.e('Room');
//					room.at(x, y);
//				}
				// Draw the edge of the board
				if (x === 0 || x === this.board.width - 1 || y ===0 || y === this.board.height - 1) {
					Crafty.e('Edge').at(x, y);
				}
			}
		}
	}
}