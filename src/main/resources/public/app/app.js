Game = {
	
	_rooms: [],
	_hallways: [],
	_player: null,
		
	// Gameboard layout
	board: {
		// Dimension of each tile (in pixels)
		tile: {
			width: 15,
			height: 15
		},
		
		// Dimensions of board (in tiles)
		width: 31,
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
		Game._player = Crafty.e('Player').at(Math.floor(Game.board.width / 2), Game.board.height - 2);
		
		// Create board (grid, rooms, hallways)
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
				// Draw the edge of the board
				if (x === 0 || x === this.board.width - 1 || y ===0 || y === this.board.height - 1) {
					Crafty.e('Edge').at(x, y);
				} 
				// Draw rooms starting that the specified tiles
				else if (y === 3 || y === 12 || y === 20) {
					if (x === 4 || x === 13 || x === 22) {
						this._rooms.push(Crafty.e('Room').at(x, y));
							//.css({ 'background-image': 'url("images/lounge.png")', 'background-size': '100% 100%' }));
						//TODO send server the room's index, get back the name, the photo url, etc.
						var roomIndex = this._rooms.length - 1;
						$.getJSON('api/rooms/' + (roomIndex + 1),
							null,
							function(data) {
								Game._rooms[data.id].setName(data.name);
								Game._rooms[data.id].css('background-image', 'url("' + data.imageUri + '")');
								Game._rooms[data.id].css('background-size', '100% 100%');
							});
					}
				}
			}
		}
		
		// Create hallways between rooms
		for (var i = 0; i < this._rooms.length - 1; ++i) {
			if (i === 0 || i % 3 !==  2) {
				this._hallways.push(Game.makeHallway(this._rooms[i], this._rooms[i + 1]));
			}
			if (i < this._rooms.length - 3) {
				this._hallways.push(Game.makeHallway(this._rooms[i], this._rooms[i + 3]));
			}
		}
	},
	
	makeHallway(room1, room2) {
		var x = 0, y = 0, w = 0, h = 0;
		// Determine width/height of hallway
		if (room1.pos()._x === room2.pos()._x) {
			// vertical hallway
			w = 3 * Game.board.tile.width;
			h = room2.pos()._y - (room1.pos()._y + room1.pos()._h);
			y = room1.pos()._y + room1.pos()._h;
			x = room1.pos()._x + Game.board.tile.width;
		} else if (room1.pos()._y === room2.pos()._y) {
			// horizontal hallway
			h = 3 * Game.board.tile.height;
			w = room2.pos()._x - (room1.pos()._x + room1.pos()._w);
			x = room1.pos()._x + room1.pos()._w;
			y = room1.pos()._y + Game.board.tile.height; 
		} else {
			return;
		}
		
		// Determine where to draw (tile position)
		return Crafty.e('Hall').attr({ x: x, y: y, w: w, h: h });
	}
}