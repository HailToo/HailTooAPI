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

		$.each(cards, function(i, name) {
			var $li = $('<li class="loading">').appendTo($list);

			$('<img>').appendTo($li).one('load', function() {
				$li.removeClass('loading');
			}).attr('src', "images/" + name + ".png");
		})
	}
};