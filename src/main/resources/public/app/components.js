Components = {
	grid: {
		init: function() {
			//console.log('initializing component: grid.');
			this.attr({ 
				w: Game.board.tile.width, 
				h: Game.board.tile.height 
			});
		},
		at: function(x, y) {
			this.attr({ 
				x: x*Game.board.tile.width, 
				y: y*Game.board.tile.height 
			});
			return this;
		}
	},
	
	/**
	 * Base object to appear on our board, 
	 * it adheres to the limitations of the grid.
	 */
	actor: {
		init: function() {
			//console.log('initializing component: actor.');
		    this.requires('2D, Canvas, Grid');
		  }
	},
	
	player: {
		init: function() {
			//console.log('initializing component: player.');
			this.requires('Actor, Color, Fourway, Collision')
				.color('#FFF')
				.fourway(50)
				.collides();
		},
		
		/*
		 * Stop the actor when encountering a solid object.
		 */
		collides: function() {
			this.onHit('Solid', this.stop);
			this.onHit('Room', this.tryEnterRoom);
		},
		
		/*
		 * Prevent character from moving.
		 */
		stop: function() {
			console.log('Collision with solid object.');
			this._speed=0;
			if (this.motionDelta()) {
				this.x -= this.motionDelta().x;
				this.y -= this.motionDelta().y;
			}
		},
		
		tryEnterRoom: function() {
			console.log('Attempt to enter room.');
		}
	},
	
	edge: {
		init: function() {
			this.requires('Actor, Color, Solid')
				.color('#000000');
		}
	},
	
	room: {
		inhabitants: [],
		
		init: function() {
			//console.log('initializing component: room.');
			
			this.requires('Actor, Color, DOM')
				.color('rgb(255, 255, 255)');
			// 5 tiles wide
			this.w = 5 * Game.board.tile.width;
			// 5 tiles tall
			this.h = 5 * Game.board.tile.height;
		},
		
		tryToEnter: function(player) {
			if (this.inhabitants.length > 1) {
				return false;
			} else {
				this.inhabitants.push(player);
				return true;
			}
		},
		
		tryToLeave: function(player) {
			if (this.inhabitants.contains(player)) {
				this.inhabitants.remove(player);
				return true;
			} else {
				return false;
			}
		}
	},
	
	hall: {
		init: function() {
			//console.log('initializing component: hall.');
			this.requires('Actor, Color, DOM')
				.color('rgb(245, 245, 220)')
				.css({
					'border': '1px solid black'
				});
		},
	},
	
	suspect: {
	
	}
};

