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