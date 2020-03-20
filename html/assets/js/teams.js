$.extend($.ui.autocomplete.prototype.options, {
    open: function(event, ui) {
        $(this).autocomplete("widget").css({
             "width": ($(this).width() + "px")
        });
    }
});

function td_render(element_id, data_source) {
    var source = teamStatsData[data_source];
    var total_td = document.getElementById(element_id).getContext('2d');

    var gradientStroke = total_td.createLinearGradient(0, 0, 0, 150);
    gradientStroke.addColorStop(0, '#2EE4BB');
    gradientStroke.addColorStop(1, '#2f4858');

    var labels = [
        "Total TD",
        "Rush TD Total",
        "Pass TD Total",
        "Interception Ret TD",
        "Kickoff Ret TD",
        "Punt Ret TD",
        "Fumble Ret TD",
        "Other TD"
    ];

    var data = [];

    labels.forEach (function (label) {
        data.push(teamStatsData[data_source][label]);
    });

    var td_chart = new Chart(total_td, {
        type: 'horizontalBar',
        data: {
            labels: labels,
            datasets: [{
                backgroundColor: gradientStroke,
                data: data
            }]
        },
        options: {
            animation: {
                duration: 2000,
            },
            legend: {
                display: false
            },
            scales: {
                yAxes: [{
                    ticks: {
                        fontColor: "rgba(0,0,0,0.5)",
                        fontStyle: "bold",
                        beginAtZero: true,
                        maxTicksLimit: labels.length,
                        padding: 20
                    }

                }],
                xAxes: [{
                    gridLines: {
                        zeroLineColor: "transparent"
                    },
                    ticks: {
                        padding: 20,
                        fontColor: "rgba(0,0,0,0.5)",
                        fontStyle: "bold"
                    }
                }]
            }
        }
    });

    td_chart.render();
}

function yards_render(element_id, data_source) {
    var total_yards = document.getElementById(element_id).getContext('2d');

    var gradientStroke = total_yards.createLinearGradient(0, 0, 0, 150);
    gradientStroke.addColorStop(0, '#2EE4BB');
    gradientStroke.addColorStop(1, '#5F5695');

    var labels = [
        "Total Yards",
        "Rush Yards Total",
        "Pass Yards Total",
        "Interception Ret Yards",
        "Kickoff Ret Yards",
        "Punt Ret Yards",
        "Fumble Ret Yards",
        "Other Yards"
    ];

    var data = [];

    labels.forEach (function (label) {
        data.push(teamStatsData[data_source][label]);
    });

    var yards_chart = new Chart(total_yards, {
        type: 'horizontalBar',
        data: {
            labels: labels,
            datasets: [{
                backgroundColor: gradientStroke,
                data: data
            }]
        },
        options: {
            animation: {
                duration: 2000,
            },
            legend: {
                display: false
            },
            scales: {
                yAxes: [{
                    ticks: {
                        fontColor: "rgba(0,0,0,0.5)",
                        fontStyle: "bold",
                        beginAtZero: true,
                        maxTicksLimit: labels.length,
                        padding: 20
                    }

                }],
                xAxes: [{
                    gridLines: {
                        zeroLineColor: "transparent"
                    },
                    ticks: {
                        padding: 20,
                        fontColor: "rgba(0,0,0,0.5)",
                        fontStyle: "bold"
                    }
                }]
            }
        }
    });

    yards_chart.render();
}

$(document).ready(function() {
    // Check if the current URL contains '#'
    if (document.URL.indexOf("#") == -1) {
        // Set the URL to whatever it was plus "#".
        url = document.URL + "#";
        location = "#";

        // Reload the page
        location.reload(true);
    }

  var td_list = {};
  var yards_list = {};

  var team_name = teamStatsData["Team Name"].toUpperCase();

  document.getElementById("render").innerHTML = `<h2>${team_name}</h2>` +
    `<section class="banner style1 onload-content-fade-in">` +
    `<div class="content" style="padding: 0% 0% 3% 0%; -webkit-align-self: flex-start;align-self: flex-start;">` +
    `<h4 style="margin-bottom: 0px;"><b>DEFENSE STATS</b></h4>` +
    `<p style="margin-bottom: 0px;"><b>Sack Yards per Game: </b>${teamStatsData["Overall"]["Sack Yards per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Sack Yards: </b>${teamStatsData["Overall"]["Total Sack Yards"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Fumbles forced per Game: </b>${teamStatsData["Overall"]["Fumbles forced per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Fumbles forced: </b>${teamStatsData["Overall"]["Total Fumbles forced"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Passes Broken per Game: </b>${teamStatsData["Overall"]["Passes Broken per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Passes broken: </b>${teamStatsData["Overall"]["Total Passes broken"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Kick/Punt Blocked: </b>${teamStatsData["Overall"]["Kick/Punt Blocked"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Kick/Punts Blocked: </b>${teamStatsData["Overall"]["Total Kick/Punts Blocked"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>QB Hurries per Game: </b>${teamStatsData["Overall"]["QB Hurries per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total QB Hurries: </b>${teamStatsData["Overall"]["Total QB Hurries"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Tackles per Game: </b>${teamStatsData["Overall"]["Tackles per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Tackles: </b>${teamStatsData["Overall"]["Total Tackles"]}</p>` +
    `</div>` +
    `<div class="content" style="padding: 0% 0% 3% 2%;">` +
    `<h4 style="margin-bottom: 0px;"><b>PLAY + PENALTIES</b></h4>` +
    `<p style="margin-bottom: 0px;"><b>Total Penalties: </b>${teamStatsData["Overall"]["Total Penalties"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Penalty Yards: </b>${teamStatsData["Overall"]["Total Penalty Yards"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Fourth Down Conversion Completion Rate: </b>${teamStatsData["Overall"]["Fourth Down Conversion Completion Rate"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Third Down Conversion Completion Rate: </b>${teamStatsData["Overall"]["Third Down Conversion Completion Rate"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Penalties Average per Game: </b>${teamStatsData["Overall"]["Penalties Average per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Penalty Yards Average per Game: </b>${teamStatsData["Overall"]["Penalty Yards Average per Game"]}</p>` +
    `<p style="margin-bottom: 2%;"><b>Possession Time Average per Game: </b>${teamStatsData["Overall"]["Possession Time Average per Game"]}</p>` +
    `<h4 style="margin-bottom: 0px;"><b>KICKING + EXTRA POINTS</b></h4>` +
    `<p style="margin-bottom: 0px;"><b>Total Kickoff Yards: </b>${teamStatsData["Overall"]["Total Kickoff Yards"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Field Goals Made: </b>${teamStatsData["Overall"]["Total Field Goals Made"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total Extra Point Kicks Made: </b>${teamStatsData["Overall"]["Total Extra Point Kicks Made"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Total 2-Point Conversions Made: </b>${teamStatsData["Overall"]["Total 2-Point Conversions Made"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Kickoff Yards Per Game: </b>${teamStatsData["Overall"]["Kickoff Yards Per Game"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Field Goal Completion Rate: </b>${teamStatsData["Overall"]["Field Goal Completion Rate"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>2-Point Conversion Completion Rate: </b>${teamStatsData["Overall"]["2-Point Conversion Completion Rate"]}</p>` +
    `<p style="margin-bottom: 0px;"><b>Extra-Point Kick Completion Rate: </b>${teamStatsData["Overall"]["Extra-Point Kick Completion Rate"]}</p>` +
    `</div>` +
    `</section>` +
    `<section class="banner style1 onload-content-fade-in">` +
    `<div class="content" style="padding: 2% 2% 5% 2%;">` +
    `<h4 style="margin-bottom: 0px; padding: 0% 0% 0% 0%;">TOUCHDOWNS</h4>` +
    `<canvas id="overall_td_graph"></canvas>` +
    `</div>` +
    `<div class="content" style="padding: 2% 2% 5% 2%;">` +
    `<h4 style="margin-bottom: 0px; padding: 0% 0% 0% 0%;">YARDS</h4>` +
    `<canvas id="overall_yards_render"></canvas>` +
    `</div>` +
    `</section>` +
    `<section class="wrapper style1 align-left fullscreen">` +
    `<div class="inner medium" style="padding-left: 0px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">` +
    `<section>` +
    `<h4>OPPOSING TEAMS STAT</h4>` +
    `<form action="" id="opposing_teams">` +
    `<div class="fields">` +
    `<div class="field third">` +
    `<label for="options" style="margin-bottom: 0px;">Options</label>` +
    `<select id="options">` +
    `<option value="Most Pass Yard">Most Pass Yard</option>` +
    `<option value="Most Rush Yard">Most Rush Yard</option>` +
    `<option value="Most Sack">Most Sack</option>` +
    `<option value="Most Rush TD">Most Rush TD</option>` +
    `<option value="Most Pass TD">Most Pass TD</option>` +
    `<option value="Most Points">Most Points</option>` +
    `</select>` +
    `</div>` +
    `<div class="field third">` +
    `<label for="" style="margin-bottom: 0px; visibility: hidden;">hidden</label>` +
    `<ul class="actions">` +
    `<li><input id="submit" name="submit" type="submit" value="Submit"/></li>` +
    `</ul>` +
    `</div>` +
    `</div>` +
    `</form>` +
    `</section>` +
    `</div>` +
    `</section>` +
    `<div id="option_table"></div>` +
    `<h4>VICTORY CHAIN</h4>` +
    `<form action="victory_chain.html" id="victory_chain">` +
    `<div class="fields">` +
    `<div class="field third">` +
    `<label for="team" style="margin-bottom: 0px;">Team</label>` +
    `<input id="team" name="team" type="text" value="">` +
    `</div>` +
    `<div class="field third">` +
    `<label for="" style="margin-bottom: 0px; visibility: hidden;">hidden</label>` +
    `<ul class="actions">` +
    `<li><input id="submit" name="submit" type="submit" value="Submit"/></li>` +
    `</ul>` +
    `</div>` +
    `</div>` +
    `</form>` +
    `<div class="accordion_container">`
  ;

  td_list["Overall"] = "overall_td_graph";
  yards_list["Overall"] = "overall_yards_render";

  // Create season accordion menu dynamically
  teamStatsData.Seasons.forEach (function (season) {
    document.getElementById("render").innerHTML += `<div class="accordion_head">${season}<span class="plusminus">+</span></div>` +
        `<div class="accordion_body" style="display: none;">` +
        `<section class="banner style1 onload-content-fade-in">` +
        `<div class="content" style="padding: 0% 0% 5% 0%;">` +
        `<h4 style="padding: 3% 4% 0% 4%;margin-bottom: 0px;"><b>GENERAL</b></h4>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Conference: </b>${teamStatsData[season]["Conference"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Pass Completion Rate: </b>${teamStatsData[season]["Pass Completion Rate"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>TD Average per Game: </b>${teamStatsData[season]["TD Average per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Yards per Game: </b>${teamStatsData[season]["Yards per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Possession Time Average per Game: </b>${teamStatsData[season]["Possession Time Average per Game"]}</p>` +
        `<h4 style="padding: 3% 4% 0% 4%;margin-bottom: 0px;"><b>DEFENSE STATS</b></h4>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Sack Yards per Game: </b>${teamStatsData[season]["Sack Yards per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Sack Yards: </b>${teamStatsData[season]["Total Sack Yards"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Fumbles forced per Game: </b>${teamStatsData[season]["Fumbles forced per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Fumbles forced: </b>${teamStatsData[season]["Total Fumbles forced"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Passes Broken per Game: </b>${teamStatsData[season]["Passes Broken per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Passes broken: </b>${teamStatsData[season]["Total Passes broken"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Kick/Punt Blocked: </b>${teamStatsData[season]["Kick/Punt Blocked"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Kick/Punts Blocked: </b>${teamStatsData[season]["Total Kick/Punts Blocked"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>QB Hurries per Game: </b>${teamStatsData[season]["QB Hurries per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total QB Hurries: </b>${teamStatsData[season]["Total QB Hurries"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Tackles per Game: </b>${teamStatsData[season]["Tackles per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Tackles: </b>${teamStatsData[season]["Total Tackles"]}</p>` +
        `</div>` +
        `<div class="content" style="padding: 0% 0% 5% 0%; -webkit-align-self: flex-start;align-self: flex-start;">` +
        `<h4 style="padding: 3% 4% 0% 4%;margin-bottom: 0px;"><b>PLAY + PENALTIES</b></h4>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Penalties: </b>${teamStatsData[season]["Total Penalties"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Penalty Yards: </b>${teamStatsData[season]["Total Penalty Yards"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Fourth Down Conversion Completion Rate: </b>${teamStatsData[season]["Fourth Down Conversion Completion Rate"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Third Down Conversion Completion Rate: </b>${teamStatsData[season]["Third Down Conversion Completion Rate"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Penalties Average per Game: </b>${teamStatsData[season]["Penalties Average per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Penalty Yards Average per Game: </b>${teamStatsData[season]["Penalty Yards Average per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Possession Time Average per Game: </b>${teamStatsData[season]["Possession Time Average per Game"]}</p>` +
        `<h4 style="padding: 1% 4% 0% 4%;margin-bottom: 0px;"><b>KICKING + EXTRA POINTS</b></h4>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Kickoff Yards: </b>${teamStatsData[season]["Total Kickoff Yards"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Field Goals Made: </b>${teamStatsData[season]["Total Field Goals Made"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total Extra Point Kicks Made: </b>${teamStatsData[season]["Total Extra Point Kicks Made"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Total 2-Point Conversions Made: </b>${teamStatsData[season]["Total 2-Point Conversions Made"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Kickoff Yards Per Game: </b>${teamStatsData[season]["Kickoff Yards Per Game"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Field Goal Completion Rate: </b>${teamStatsData[season]["Field Goal Completion Rate"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>2-Point Conversion Completion Rate: </b>${teamStatsData[season]["2-Point Conversion Completion Rate"]}</p>` +
        `<p style="padding: 0% 4% 0% 4%;"><b>Extra-Point Kick Completion Rate: </b>${teamStatsData[season]["Extra-Point Kick Completion Rate"]}</p>` +
        `</div>` +
        `</section>` +
        `<section class="banner style1 onload-content-fade-in">` +
        `<div class="content" style="padding: 2% 2% 5% 2%;">` +
        `<h4 style="margin-bottom: 0px; padding: 0% 0% 0% 0%;">TOUCHDOWNS</h4>` +
        `<canvas id="${season}_td_graph"></canvas>` +
        `</div>` +
        `<div class="content" style="padding: 2% 2% 5% 2%;">` +
        `<h4 style="margin-bottom: 0px; padding: 0% 0% 0% 0%;">YARDS</h4>` +
        `<canvas id="${season}_yards_render"></canvas>` +
        `</div>` +
        `</section>` +
        `</div>`
    ;
    td_list[season] = season + "_td_graph";
    yards_list[season] = season + "_yards_render";
  });

  // Add closing div tag for accordion menu
  document.getElementById("render").innerHTML += `</div>`;

  // Render each graph
  for (const [ key, value ] of Object.entries(td_list)) {
      td_render(value, key);
  }

  for (const [ key, value ] of Object.entries(yards_list)) {
      yards_render(value, key);
  }

  // Toggle the component with class accordion_body
  $(".accordion_head").click(function() {
    if ($('.accordion_body').is(':visible')) {
      $(".accordion_body").slideUp(300);
      $(".plusminus").text('+');
    }
    if ($(this).next(".accordion_body").is(':visible')) {
      $(this).next(".accordion_body").slideUp(300);
      $(this).children(".plusminus").text('+');
    } else {
      $(this).next(".accordion_body").slideDown(300);
      $(this).children(".plusminus").text('–');
    }
  });

  // Autocomplete
  $('#team').autocomplete({
      source: team_names
  });

  $(document).on('submit', '#opposing_teams', function() {
    var option = document.getElementById("options").value;

    $("#option_table").empty();

    document.getElementById("option_table").innerHTML += `<div class="content" style="padding-bottom: 5%;">` +
        `<table class="alt" data-order='[[ 0, "asc" ]]' data-page-length='25' id="table">` +
        `<thead>` +
        `<tr>` +
        `<th>Rank</th>`+
        `<th>Team</th>` +
        `<th>${team_options[option]}</th>` +
        `<th>Season</th>` +
        `</tr>` +
        `</thead>` +
        `</table>` +
        `</div>`
    ;

    $('#table').DataTable().destroy();

    var table = $('#table').DataTable( {
        data: teamStatsData[option],
        "columns": [
            { "data": "rank" },
            { "data": "Team Name" },
            { "data": team_options[option] },
            { "data": "season" },
        ],

        fnDrawCallback: function (settings) {
             $("#table").parent().toggle(settings.fnRecordsDisplay() >= 0);
        }
    });

    return false;
  });

  $(document).on('submit', '#victory_chain', function() {
    var source_team = teamStatsData["Team Name"];
    var source_id = "";
    var target_team = document.getElementById("team").value;
    var target_id = "";

    Object.keys(teams).forEach(function(key) {
      if (teams[key] == source_team) {
        source_id = key;
      }
      else if (teams[key] == target_team) {
        target_id = key;
      }
    });

    if (source_id.length != 0 && target_id.length != 0) {
       window.javaConnector.shortestVictoryChainRequest(source_id, target_id);
    }
  });
});
