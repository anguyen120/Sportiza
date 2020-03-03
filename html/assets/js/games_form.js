$( function() {
    $('#home_teams').autocomplete({
        source: team_names
    });
    $('#visit_teams').autocomplete({
        source: team_names
    });
} );

$.extend($.ui.autocomplete.prototype.options, {
	open: function(event, ui) {
		$(this).autocomplete("widget").css({
            "width": ($(this).width() + "px")
        });
    }
});