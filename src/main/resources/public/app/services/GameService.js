GameService = {
		getGameState: function(gameGuid) {
			//console.log("query game status");
			return $.ajax({
			  url: "api/game/" + gameGuid,
			  method: 'GET'
			});
		},

		characters: function() {
			return $.ajax({
			  url: "api/suspects",
			  method: 'GET'
			});
		},
		
		joinGame: function(gameGuid, characterName) {
			return $.ajax({
				  url: "api/game",
				  method: 'POST',
				  data: { gameGuid: gameGuid, characterChoice:  characterName}
				});
		}
		
};