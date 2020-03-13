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

    $('#table').on('click', 'a.team_select', function () {
        window.javaConnector.playerStatsRequest(table.row($(this).closest('tr')).data().id,
            table.row($(this).closest('tr')).data()["Team Name"],
            table.row($(this).closest('tr')).data()["subdivision"],
            table.row($(this).closest('tr')).data()["Conference Name"]
        );
    } );

    var table = $('#table').DataTable( {
       data: query,
       "columns": [
           { "data": "id",
             "visible": false,
             "searchable": false},
           { "data": "Team Name" },
           { "data": "Conference Name" },
           { "data": "subdivision" },
           { "data": null,
             "className": "center",
             "defaultContent": '<a href="teams.html" class="team_select">Select</a>'},
        ],

        fnDrawCallback: function (settings) {
            $("#table").parent().toggle(settings.fnRecordsDisplay() >= 0);
        }
    } );
});

