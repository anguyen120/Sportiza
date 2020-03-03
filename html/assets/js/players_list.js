$(document).ready(function(){
    //Check if the current URL contains '#'
    if(document.URL.indexOf("#")==-1){
        // Set the URL to whatever it was plus "#".
        url = document.URL+"#";
        location = "#";

        //Reload the page
        location.reload(true);
    }

    $('#table').DataTable().destroy();

    var table = $('#table').DataTable( {
       data: query,
       "columns": [
           { "data": "id" },
           { "data": "First Name" },
           { "data": "Last Name" },
           { "data": "Position" },
           { "data": "Home Town" },
           { "data": "Home State" },
           { "data": "Home Country" }
        ],

        fnDrawCallback: function (settings) {
            $("#table").parent().toggle(settings.fnRecordsDisplay() >= 0);
        }
    } );
});

