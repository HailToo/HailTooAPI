GameService = {
	token: "",
	
	setAuthToken: function(authToken) {
		GameService.token = authToken;
		console.log("ajax default header set.");
	},
	
	ajax: function(request) {
		request.beforeSend = function (xhr) {
			xhr.setRequestHeader("Authorization", GameService.token);
			xhr.setRequestHeader("HailData", "yeah");
		}
		
		return $.ajax(request);
	},
	
	getGameState: function(gameGuid) {
		return GameService.ajax({
			url: "api/game/" + gameGuid,
			method: 'GET'
		});
	},

	characters: function() {
		return GameService.ajax({
			url: "api/suspects",
			method: 'GET'
		});
	},
	
	weapons: function() {
		return GameService.ajax({
			url: "api/weapons",
			method: 'GET'
		});
	},
	
	joinGame: function(gameGuid, characterName) {
		return GameService.ajax({
			url: "api/game",
			method: 'POST',
			data: { gameGuid: gameGuid, characterChoice:  characterName}
		});
	},
	
	newGame: function() {
		return GameService.ajax({
			url: "api/game",
			method: 'PUT',
			async: false
		}).responseText;
	},
	
	getVersion: function() {
		return GameService.ajax({
			url: "version",
			method: 'GET',
			async: false
		}).responseText;
	},
	
	login: function(logonData) {
		return GameService.ajax({
			url: "login",
			method: 'POST',
			data: JSON.stringify(logonData),
			contentType: "application/json",
			async: false
		});
	},
	
	move: function(gameGuid, area) {
		return GameService.ajax({
			url: "api/game/" + gameGuid + "/move",
			method: 'POST',
			data: { gameGuid: gameGuid, area: area }
		});
	},
	
	start: function(gameGuid) {
		return GameService.ajax({
			url: "api/game/" + gameGuid + "/start",
			method: 'POST'
		});
	},
	
	getMoves: function(gameGuid) {
		return GameService.ajax({
			url: "api/game/" + gameGuid + "/move",
			method: 'GET'
		});
	},
	
	suggest: function(gameGuid, room, weapon, suspect) {
		return GameService.ajax({
			url: "api/game/" + gameGuid + "/suggest",
			method: 'POST',
			data: { gameGuid: gameGuid, room: room, weapon: weapon, suspect: suspect }
		});
	}
};