Components = {
	grid: {
		init: function() {
			this.attr({ 
				w: HailClient.board.tile.width, 
				h: HailClient.board.tile.height 
			});
		},
		at: function(x, y) {
			this.attr({ 
				x: x*HailClient.board.tile.width, 
				y: y*HailClient.board.tile.height 
			});
			return this;
		}
	},
	
	room: {
		init: function() {
			this.requires('Actor, Color')
				.color('rgb(255, 255, 255)');
		},
	},
	
	hall: {
		init: function() {
			this.requires('Actor, Color')
				.color('rgb(0, 0, 0)');
		},
	},
	
	suspect: {
	
	}
};

Crafty.c('Grid', Components.grid);
Crafty.c('Room', Components.room);
Crafty.c('Hall', Components.hall);
Crafty.c('Suspect', Components.suspect);