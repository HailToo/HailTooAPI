NotificationHelper = {
	modalAlert: function (message, displayTime) {
		NotificationHelper.showModal();
		
		$("#modalDialog .modal-body").text(message);
		if (displayTime > 0) {
			setTimeout(NotificationHelper.hideModal, displayTime);
		}
	},
	
	showModal: function () {
		$("#modalDialog").css("display", "block");
	},
	
	hideModal: function () {
		$("#modalDialog").css("display", "none");
	},
	
	populateHand: function(cards) {
		var $list = $('#cardList');
		
		$list
	    .find('div')
	    .remove()
	    .end();

		$.each(cards, function(i, name) {
			var $card = $('<div class="card loading">').appendTo($list);

			//var $li = $('<li class="loading">').appendTo($list);

			$('<img>').appendTo($card).one('load', function() {
				$card.removeClass('loading');
			}).attr('src', "images/" + name + ".png");

			$('<p><b>' + name + '</b></p>').appendTo($card)
		})
	},
	
	pushMessage: function(message) {
		$("<div />").text(message).appendTo("#tail");
	    var height = $("#tail").get(0).scrollHeight;
	    $("#tail").animate({ scrollTop: height }, 500);
	}
};