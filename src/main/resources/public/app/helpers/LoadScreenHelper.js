/**
 * Splash screen implementation sourced from
 * http://www.thatsoftwaredude.com/content/6254/building-a-side-scroller-with-craftyjs 
 */
Splash = {
	screenWidth: 800,
	screenHeight: 400,
	levelWidth: 1600,
	
	init: function() {
		Crafty.init(Splash.screenWidth, Splash.screenHeight, document.getElementById('game'));
		
		Crafty.defineScene("HomeScreen", function() {
		  Crafty.background("#000");
		  Crafty.e("2D, DOM, Text, Mouse")
		      .attr({ w: 300, h: 20, x: 100, y: 200 })
		      .text("Click to start")
		      .css({ "text-align": "center"})
		      .textFont({size: '20px', weight: 'bold'})
		      .textColor("#FFFFFF")
		      .bind('Click', function(MouseEvent){
		        Game.init();
		      });
		
		      Crafty.e("2D, DOM, Text")
		      .attr({ w:400, h:40, x: 50, y: 50})
		      .text("HailToo")
		      .textFont({size:'130px', weight:'bold'})
		      .css({"text-align": "center"})
		      .textColor("#FFFFFF");
		});
		
		Crafty.defineScene("Splash1", function(){
		  Crafty.background('#FFFFFF no-repeat center center');
		  Splash.load_scene("HomeScreen", 3000);
		});
		
		Splash.load_scene("Splash1", 3000);
		
		Crafty.bind("EnterFrame", function(){
			  if (Crafty.frame() % 2 == 0)
			    Splash.drop();
			});
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
	        .attr({alpha:0.0, x:0, y:0, w:800, h:600})
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