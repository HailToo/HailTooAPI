/*
angular.module('hailclient'. [])
	.controller('home', HomeController);

function HomeController(scope) {
	scope.greeting = {
		id: 'xxx',
		content: 'Hello world!'
	};
}
*/

HailClient = {
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
		Crafty.init(HailClient.width(), HailClient.height(), document.getElementById('game'));
		
		// Background
		Crafty.background('green');
		
		// Create grid system
		Crafty.c('Grid', {
			init: function() {
				this.attr({
					w: HailClient.board.tile.width,
					h: HailClient.board.tile.height
				});
			},
			
			location: function(x, y) {
			
			}
		});
		
		// Create entity (player object)
		Crafty.e('2D, DOM, Color, Fourway')
			.attr({x: 0, y: 0, w: 100, h: 100})
			.color('#F00')
			.fourway(200);
		
		//HailClient.drawBoard();
	
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
				if (x % 3 === 0 && y % 3 === 0) {
					// Draw either a room or hallway
					var room = Crafty.e('Room');
					room.at(x, y);
				}
			}
		}
	}
}