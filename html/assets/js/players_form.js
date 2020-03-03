$( function() {
    $('#team').autocomplete({
        source: team_names
    });

    $("#hometown").autocomplete({
        source: hometowns
    });
} );

$.extend($.ui.autocomplete.prototype.options, {
	open: function(event, ui) {
		$(this).autocomplete("widget").css({
            "width": ($(this).width() + "px")
        });
    }
});

var form = document.getElementById("players_form");
form.addEventListener("submit", function() {
  var first_name = document.getElementById("first_name").value;
  var last_name = document.getElementById("last_name").value;
  var team = document.getElementById("team").value;
  var uniform_number = document.getElementById('uniform_number').value;
  var hometown = document.getElementById('hometown').value;

  if (first_name.length == 0) {
      first_name = "NULL";
  }
  if (last_name.length == 0) {
      last_name = "NULL";
  }
  if (team.length == 0) {
      team = "NULL";
  }
  if (uniform_number.length == 0) {
      uniform_number = "NULL";
  }
  if (hometown.length == 0) {
      hometown = "NULL";
  }

    async_submit(first_name, last_name, team, uniform_number, hometown);
});

async function async_submit(first_name, last_name, team, uniform_number, hometown) {
  await window.javaConnector.playerFormRequest(first_name, last_name, team, uniform_number, hometown)
  form.submit()
}