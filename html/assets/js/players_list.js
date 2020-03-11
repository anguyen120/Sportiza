$(document).ready(function(){
    //Check if the current URL contains '#'
    if (document.URL.indexOf("#") == -1) {
        // Set the URL to whatever it was plus "#".
        url = document.URL + "#";
        location = "#";

        //Reload the page
        location.reload(true);
    }

    $('#table').DataTable().destroy();

    $('#table').on('click', 'a.player_select', function () {
        window.javaConnector.playerStatsRequest(table.row($(this).closest('tr')).data().id,
            table.row($(this).closest('tr')).data()["First Name"],
            table.row($(this).closest('tr')).data()["Last Name"],
            table.row($(this).closest('tr')).data()["Home Town"],
            table.row($(this).closest('tr')).data()["Home State"],
            table.row($(this).closest('tr')).data()["Home Country"]
        );
    } );

    var table = $('#table').DataTable( {
       data: query,
       "columns": [
           { "data": "id",
             "visible": false,
             "searchable": false},
           { "data": "First Name" },
           { "data": "Last Name" },
           { "data": "Position" },
           { "data": "Home Town" },
           { "data": "Home State" },
           { "data": "Home Country" },
           { "data": null,
             "className": "center",
             "defaultContent": '<a href="players.html" class="player_select">Select</a>'},
        ],

        fnDrawCallback: function (settings) {
            $("#table").parent().toggle(settings.fnRecordsDisplay() >= 0);
        }
    } );
});

