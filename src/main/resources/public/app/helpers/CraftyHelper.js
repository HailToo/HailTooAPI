CraftyHelper = {
	// Register components of the game
	registerComponents: function() {
		Crafty.c('Grid', Components.grid);
		Crafty.c('Actor', Components.actor);
		Crafty.c('Player', Components.player);
		Crafty.c('Edge', Components.edge);
		Crafty.c('Room', Components.room);
		Crafty.c('Hall', Components.hall);
		Crafty.c('Suspect', Components.suspect);
	},
		
	makeRoom: function(name) {
		var atX, atY;
		switch(name) {
			case 'Study':
				atX = 1;
				atY = 1;
				break;
			case 'Hall':
				atX = 15;
				atY = 1;
				break;
			case 'Lounge':
				atX = 29;
				atY = 1;
				break;
			case 'Library':
				atX = 1;
				atY = 15;
				break;
			case 'BilliardRoom':
				atX = 15;
				atY = 15;
				break;
			case 'DiningRoom':
				atX = 29;
				atY = 15;
				break;
			case 'Conservatory':
				atX = 1;
				atY = 29;
				break;
			case 'Ballroom':
				atX = 15;
				atY = 29;
				break;
			case 'Kitchen':
				atX = 29;
				atY = 29;
				break;
		}
		
		var room = Crafty.e('Room').at(atX, atY);
		room.attr("name", name);
		room.css('background-image', "url('images/" + name + ".png')");
		room.css('background-size', '100% 100%');
		return room;
	},
		
	makeHallway: function(room1, room2) {
		var x = 0, y = 0, w = 0, h = 0;
		var name = "HW_" + room1.attr("name").substring(0,1) + room2.attr("name").substring(0,1);
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
		hallway.attr("name", name);
		return hallway;
	},
	
	createCharacter: function(characterName) {
		var suspect = Crafty.e('Suspect');
		suspect.color("rgba(0,0,0,0.01)");
		suspect.w = 35;
		suspect.h = 70;
		suspect.z = 1000000000;
		suspect.css("background-size", "100%")
		suspect.attr("name", characterName);
		suspect.css("background-image", "url('images/" + suspect.attr("name") + ".png')");
		return suspect;
	},
	
	drawEdge: function(width, height) {
		for (var x = 0; x < width; ++x) {
			for (var y = 0; y < height; ++y) {
				// Draw the edge of the board
				if (x === 0 || x === width - 1 || y ===0 || y === height - 1) {
					Crafty.e('Edge').at(x, y);
				} 
			}
		}
	}
};