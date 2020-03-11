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

var form = document.getElementById("teams_form");
form.addEventListener("submit", function() {
  var team = document.getElementById("team").value;
  var conference = document.getElementById("conference").value;
  var subdivision = document.getElementById("subdivision").value;

  if (team.length == 0) {
      team = "NULL";
  }
  if (conference.length == 0) {
      conference = "NULL";
  }
  if (subdivision.length == 0) {
      subdivision = "NULL";
  }

    async_submit(team, conference, subdivision);
});

async function async_submit(team, conference, subdivision) {
  await window.javaConnector.teamFormRequest(team, conference, subdivision)
  form.submit()
}