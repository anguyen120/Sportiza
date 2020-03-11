$( function() {
    $('#team').autocomplete({
        source: team_names
    });

    $('#conference').autocomplete({
        source: conferences
    });

    $('#subdivision').autocomplete({
        source: subdivisions
    });

} );

$.extend($.ui.autocomplete.prototype.options, {
	open: function(event, ui) {
		$(this).autocomplete("widget").css({
            "width": ($(this).width() + "px")
        });
    }
});