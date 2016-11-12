/**
 * Splash screen implementation sourced from
 * http://www.thatsoftwaredude.com/content/6254/building-a-side-scroller-with-craftyjs 
 */
Splash = {
	screenWidth: 800,
	screenHeight: 400,
	levelWidth: 1600,
	
	init: function() {
		//Crafty.init(Splash.screenWidth, Splash.screenHeight, document.getElementById('loader'));
		Crafty.init(Game.width(), Game.height(), document.getElementById('game'));
		CraftyHelper.registerComponents();
		
		Crafty.defineScene("HomeScreen", function() {
		  Crafty.background("#000");
		  
		  //Splash title
		  Crafty.e("2D, DOM, Text")
		      .attr({ w:400, h:40, x: 50, y: 50})
		      .text("HailToo")
		      .textFont({size:'130px', weight:'bold'})
		      .css({"text-align": "center"})
		      .textColor("#FFFFFF");
		  
		  //Splash option: "Click to Join" 
		  Crafty.e("2D, DOM, Text, Mouse")
		      .attr({ w: 300, h: 20, x: 100, y: 200 })
		      .text("Click to join")
		      .css({ "text-align": "center"})
		      .textFont({size: '20px', weight: 'bold'})
		      .textColor("#FFFFFF")
		      .bind('Click', function(MouseEvent){
		      	//Prompt user for gameId to join
		      	MenuHelper.join();
		      	
		      	//Show game board
		      	Splash.load_scene("GameBoard", 500);
		      });
		
	      //Splash option: "Create Game" 
		  Crafty.e("2D, DOM, Text, Mouse")
		      .attr({ w: 300, h: 20, x: 100, y: Game.height() - 30 })
		      .text("Create Game")
		      .css({ "text-align": "center", "cursor": "pointer"})
		      .textFont({size: '20px', weight: 'bold'})
		      .textColor("#FFFFFF")
		      .bind('Click', function(MouseEvent){
		      	MenuHelper.create();
		      	
		      	//Show game board
		      	Splash.load_scene("GameBoard", 500);
		      });
		});
		
		Crafty.defineScene("GameBoard", function() {
			Crafty.background('green');
			
			// Traverse the entire board (per tile)
			CraftyHelper.drawEdge(Game.board.width, Game.board.height);
			
			//Draw each room
			Game._rooms.push(CraftyHelper.makeRoom('Study'));
			Game._rooms.push(CraftyHelper.makeRoom('Hall'));
			Game._rooms.push(CraftyHelper.makeRoom('Lounge'));
			Game._rooms.push(CraftyHelper.makeRoom('Library'));
			Game._rooms.push(CraftyHelper.makeRoom('BilliardRoom'));
			Game._rooms.push(CraftyHelper.makeRoom('DiningRoom'));
			Game._rooms.push(CraftyHelper.makeRoom('Conservatory'));
			Game._rooms.push(CraftyHelper.makeRoom('Ballroom'));
			Game._rooms.push(CraftyHelper.makeRoom('Kitchen'));
			
			// Create hallways between rooms
			for (var i = 0; i < Game._rooms.length - 1; ++i) {
				if (i === 0 || i % 3 !==  2) {
					Game._hallways.push(CraftyHelper.makeHallway(Game._rooms[i], Game._rooms[i + 1]));
				}
				if (i < Game._rooms.length - 3) {
					Game._hallways.push(CraftyHelper.makeHallway(Game._rooms[i], Game._rooms[i + 3]));
				}
			}

			Game.isLoaded = true;
		});
		
		Crafty.defineScene("Prompt", function() {
			Crafty.background('#127F6F no-repeat center center');
			//Splash title
			  Crafty.e("2D, DOM, Text")
			      .attr({ w:400, h:40, x: 50, y: 50})
			      .text("It's your turn...")
			      .textFont({size:'40px', weight:'bold'})
			      .css({"text-align": "center", "curser": "default"})
			      .textColor("#FFFFFF");
			  
//			  Crafty.e("HTML")
//			  	.attr({x:20, y:20, w:100, h:100})
//			  	.append("<div>" +
//			  				"<select class=''>"
//			  			"</div>");
		});
		
		Crafty.defineScene("Splash1", function() {
		  Crafty.background('#FFFFFF no-repeat center center');
		  Splash.load_scene("HomeScreen", 500);
		});
		
		Splash.load_scene("Splash1", 100);

	},
	
	drop: function()
	{
	    var randomx = Math.floor((Math.random() * Splash.levelWidth) + 50);
	    Crafty.e('Drop, 2D, Canvas, Color, Solid, Gravity, Collision')
	        .attr({x: randomx, y: 0, w: 2, h: 15})
	        .color('#FFFFFF')
	        .gravity()
	        .gravityConst(.5)
	        .checkHits('Player')
	        .bind("HitOn", function(){
	            this.destroy();
	        })
	        .bind("EnterFrame", function() {
	            if (this.y > Splash.screenHeight)
	              this.destroy();
	        });
	},
	
	load_scene: function(scene, duration) {
	    Crafty.e("2D, Canvas, Tween, Color")
	        .attr({alpha:0.0, x:0, y:0, w:Game.width(), h:Game.height()})
	        .color("#000000")
	        .tween({alpha: 1.0}, duration)
	        .bind("TweenEnd", function() {
	            Crafty.scene(scene);
	            Crafty.e("2D, Canvas, Tween, Color")
	                .attr({alpha:1.0, x:0, y:0, w:800, h:600})
	                .color("#000000")
	                .tween({alpha: 0.0}, duration);
	        });
	}
};