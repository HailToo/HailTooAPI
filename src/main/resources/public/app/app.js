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
		width: 40,
		height: 40
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
		Game._player = Crafty.e('Player').at(Math.floor(Game.board.width / 2), Game.board.height - 10);
		Game._player.color("rgba(0,0,0,0.01)");
		Game._player.w = 35;
		Game._player.h = 70;
		Game._player.z = 1000000000;
		Game._player.css("background-size", "100%")
		Game._player.attr("name", window.prompt("Pick a character", "profPlum"));
		Game._player.css("background-image", "url('images/" + Game._player.attr("name") + ".png')");
		
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
			}
		}
		
		//Draw each room
		Game.makeRoom('study');
		Game.makeRoom('hall');
		Game.makeRoom('lounge');
		Game.makeRoom('library');
		Game.makeRoom('billiardRoom');
		Game.makeRoom('diningRoom');
		Game.makeRoom('conservatory');
		Game.makeRoom('ballroom');
		Game.makeRoom('kitchen');
		
		// Create hallways between rooms
		for (var i = 0; i < this._rooms.length - 1; ++i) {
			if (i === 0 || i % 3 !==  2) {
				Game.makeHallway(this._rooms[i], this._rooms[i + 1]);
			}
			if (i < this._rooms.length - 3) {
				Game.makeHallway(this._rooms[i], this._rooms[i + 3]);
			}
		}
	},
	
	makeRoom(name) {
		var atX, atY;
		switch(name) {
			case 'study':
				atX = 1;
				atY = 1;
				break;
			case 'hall':
				atX = 15;
				atY = 1;
				break;
			case 'lounge':
				atX = 29;
				atY = 1;
				break;
			case 'library':
				atX = 1;
				atY = 15;
				break;
			case 'billiardRoom':
				atX = 15;
				atY = 15;
				break;
			case 'diningRoom':
				atX = 29;
				atY = 15;
				break;
			case 'conservatory':
				atX = 1;
				atY = 29;
				break;
			case 'ballroom':
				atX = 15;
				atY = 29;
				break;
			case 'kitchen':
				atX = 29;
				atY = 29;
				break;
		}
		
		var room = Crafty.e('Room').at(atX, atY);
		room.setName(name);
		var backgroundImageCss = "url('images/" + name + ".png')";
		room.css('background-image', backgroundImageCss);
		room.css('background-size', '100% 100%');
		this._rooms.push(room);
	},
	
	makeHallway(room1, room2) {
		var x = 0, y = 0, w = 0, h = 0;
		// Determine width/height of hallway
		if (room1.pos()._x === room2.pos()._x) {
			// vertical hallway
			w = 2 * Game.board.tile.width;
			h = room2.pos()._y - (room1.pos()._y + room1.pos()._h);
			y = room1.pos()._y + room1.pos()._h;
			x = room1.pos()._x + (4 * Game.board.tile.width);
		} else if (room1.pos()._y === room2.pos()._y) {
			// horizontal hallway
			h = 2 * Game.board.tile.height;
			w = room2.pos()._x - (room1.pos()._x + room1.pos()._w);
			x = room1.pos()._x + room1.pos()._w;
			y = room1.pos()._y + (4 * Game.board.tile.height); 
		} else {
			return;
		}
		
		// Determine where to draw (tile position)
		var hallway = Crafty.e('Hall').attr({ x: x, y: y, w: w, h: h });
		this._hallways.push(hallway);
		return hallway;
	}
}