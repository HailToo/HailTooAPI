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
	}
};