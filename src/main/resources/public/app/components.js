Components = {
	grid: {
		init: function() {
			console.log('initializing component: grid.');
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
	
	actor: {
		init: function() {
			console.log('initializing component: actor.');
		    this.requires('2D, Canvas, Grid');
		  }
	},
	
	player: {
		init: function() {
			console.log('initializing component: player.');
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
		}
	},
	
	edge: {
		init: function() {
			this.requires('Actor, Color, Solid')
				.color('#000000');
		}
	},
	
	room: {
		init: function() {
			console.log('initializing component: room.');
			this.requires('Actor, Color, DOM, Solid')
				.color('rgb(255, 255, 255)');
			this.w = 5 * Game.board.tile.width;
			this.h = 5 * Game.board.tile.height;
		},
	},
	
	hall: {
		init: function() {
			console.log('initializing component: hall.');
			this.requires('Actor, Color')
				.color('rgb(0, 0, 0)');
		},
	},
	
	suspect: {
	
	}
};

