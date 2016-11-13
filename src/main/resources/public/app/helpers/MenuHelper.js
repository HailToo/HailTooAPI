MenuHelper = {
	join: function()  {
		// TODO: Prompt user to pick from list.
		var gameGuid = window.prompt("Enter game id", document.gameGuid);
		GameService.getGameState(gameGuid).done(function(data) {
			document.gameState = data;
			
			//Determine which characters are available to choose from
			var availableCharacters = document.characters;
			data.players.forEach(function(p) {
				//Remove any character from available list if it's already taken
				if(availableCharacters.indexOf(p.character) >= 0) {
					availableCharacters.splice(availableCharacters.indexOf(p.character), 1);
				}
			});
			
			//Prompt user for character choice
			var characterName = window.prompt("Pick a character", availableCharacters[0]);
			Game._user.character = characterName;
			console.log("character choice: " + characterName);
				
			GameService.joinGame(gameGuid, characterName).done(function(data) {			  
				Game.pollGameState();
			});
		});
	},

	create: function() {
		document.gameGuid = GameService.newGame();
		console.log("Game ID: " + document.gameGuid);
		MenuHelper.join();
	},

	start: function() {
		GameService.start(document.gameState.name).done(function(data) {
			console.log("start game response: " + data);
		});
	},

	get: function() {
		GameService.getGameState(document.gameState.name).done(function(data) {
			console.log("reply: " + JSON.stringify(data));
		});
	}
};