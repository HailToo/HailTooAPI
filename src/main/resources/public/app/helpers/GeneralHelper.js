GeneralHelper = {
	populateDropdown: function(selector, optionList) {
		if (optionList === undefined || optionList === null || optionList.length === 0) {
			return;
		}
		
		$(selector)
	    .find('option')
	    .remove()
	    .end();
		
		optionList.forEach(function(element) {
			console.log("adding move: " + element);
			$(selector).append($("<option></option>")
                    .attr("value", element)
                    .text(element));
		});
	}
};