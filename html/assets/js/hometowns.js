$(document).ready(function() {
    var listeners = [];

    var options = [
        "Players"
    ];

    options = options.concat(positions);

    document.getElementById("render").innerHTML = `<h2>HOMETOWNS</h2>`;

    options.forEach (function (option) {
        document.getElementById("render").innerHTML += `<section>` +
            `<h4 style="margin-bottom: 0px;">Most ${option}</h4>` +
            `<div id="${option}_container"></div>` +
            `</section>`
        ;

        var container = option + "_container";
        var table = "#" + option + "_table";

        document.getElementById(container).innerHTML += `<div class="content" style="padding-bottom: 5%;">` +
            `<table class="alt" data-order='[[ 0, "asc" ]]' data-page-length='25' id="${option}_table">` +
            `<thead>` +
            `<tr>` +
            `<th>Rank</th>`+
            `<th>Hometown</th>` +
            `<th>Player Count</th>` +
            `</tr>` +
            `</thead>` +
            `</table>` +
            `</div>`
        ;

        $(table).DataTable( {
            data: query[option],
            "paging": false,
            "ordering": false,
            "info": false,
            "searching": false,
            "columns": [
                { "data": "Rank" },
                { "data": "Home Town",
                  "render": function(data, type, row, meta) {
                     if (type === 'display') {
                         data = '<a href="players_list.html" class="hometown_select">' + data + '</a>';
                     }
                     return data;
                  }
                },
                { "data": "Player Count" },
            ],

            fnDrawCallback: function (settings) {
                 $(table).parent().toggle(settings.fnRecordsDisplay() >= 0);
            }
        });

        listeners.push(table);
    });

    stats.forEach (function (stat) {
        document.getElementById("render").innerHTML += `<section>` +
            `<h4 style="margin-bottom: 0px;">Most ${stat}</h4>` +
            `<div id="${stat}_container"></div>` +
            `</section>`
        ;

        var container = stat + "_container";
        var stat_id = stat.replace(/ /g, "_") + "_table";
        var table = "#" + stat_id;

        document.getElementById(container).innerHTML += `<div class="content" style="padding-bottom: 5%;">` +
            `<table class="alt" data-order='[[ 0, "asc" ]]' data-page-length='25' id="${stat_id}">` +
            `<thead>` +
            `<tr>` +
            `<th>Rank</th>`+
            `<th>Hometown</th>` +
            `<th>${stat}</th>` +
            `</tr>` +
            `</thead>` +
            `</table>` +
            `</div>`
        ;

        $(table).DataTable( {
            data: query[stat],
            "paging": false,
            "ordering": false,
            "info": false,
            "searching": false,
            "columns": [
                { "data": "Rank" },
                { "data": "Home Town",
                  "render": function(data, type, row, meta) {
                     if (type === 'display') {
                         data = '<a href="players_list.html" class="hometown_select">' + data + '</a>';
                     }
                     return data;
                  }
                },
                { "data": stat },
            ],

            fnDrawCallback: function (settings) {
                 $(table).parent().toggle(settings.fnRecordsDisplay() >= 0);
            }
        });

        listeners.push(table);
    });

    listeners.forEach(function(listener) {
        $(listener).on('click', 'a.hometown_select', function () {
            window.javaConnector.playerFormRequest("NULL",
                "NULL",
                "NULL",
                "NULL",
                $(this).text()
            );
        } );
    });
});