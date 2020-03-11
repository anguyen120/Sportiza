function td_render(element_id, data_source) {
    var source = query[data_source];
    var total_td = document.getElementById(element_id).getContext('2d');

    var gradientStroke = total_td.createLinearGradient(0, 0, 0, 150);
    gradientStroke.addColorStop(0, '#2EE4BB');
    gradientStroke.addColorStop(1, '#2f4858');

    var labels = [
        "Pass TD Total",
        "Fumble Ret TD",
        "Total TD",
        "Rush TD Total",
        "Kickoff Ret TD",
        "Other TD",
        "Punt Ret TD",
        "Pass Received TD",
        "Interception Ret TD"
    ];

    var data = [];

    labels.forEach (function (label) {
        data.push(query[data_source][label]);
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
        "Rush Yards Total",
        "Interception Ret Yard",
        "Pass Received Yards",
        "Total Yards",
        "Other Yards",
        "Total Kickoff Yards",
        "Pass Yards Total",
        "Kickoff Ret Yards",
        "Fumble Ret Yards"
    ];

    var data = [];

    labels.forEach (function (label) {
        data.push(query[data_source][label]);
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
    //Check if the current URL contains '#'
    if (document.URL.indexOf("#") == -1) {
        // Set the URL to whatever it was plus "#".
        url = document.URL + "#";
        location = "#";

        //Reload the page
        location.reload(true);
    }

  td_list = {};
  yards_list = {};

  var first_name = query["First Name"].toUpperCase();
  var last_name = query["Last Name"].toUpperCase();

  document.getElementById("render").innerHTML = `<h2 style="margin-bottom: 0px;">${first_name} ${last_name}</h2>`;

  if (query["Home Town"] != "N/A" && query["Home State"] != "N/A" && query["Home Country"] != "N/A") {
    document.getElementById("render").innerHTML += `<p style="margin-bottom: 2%;">from ${query["Home Town"]}, ${query["Home State"]} ${query["Home Country"]}</p>`
  } else if (query["Home Town"] != "N/A" && query["Home State"] != "N/A") {
    document.getElementById("render").innerHTML += `<p style="margin-bottom: 2%;">from ${query["Home Town"]}, ${query["Home State"]}</p>`
  } else if (query["Home Town"] != "N/A") {
    document.getElementById("render").innerHTML += `<p style="margin-bottom: 2%;">from ${query["Home Town"]}</p>`
  }

  document.getElementById("render").innerHTML += `<section class="banner style1 onload-content-fade-in">` +
     `<div class="content" style="padding: 0% 0% 3% 0%; -webkit-align-self: flex-start;align-self: flex-start;">` +
     `<h4 style="margin-bottom: 0px;"><b>OFFENSE STATS</b></h4>` +
     `<p style="margin-bottom: 0px;"><b>Pass Completion Rate: </b>${query["Overall"]["Pass Completion Rate"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>TD Average per Game: </b>${query["Overall"]["TD Average per Game"]}</p>` +
     `<p style="margin-bottom: 2%;"><b>Yards per Game: </b>${query["Overall"]["Yards per Game"]}</p>` +
     `<h4 style="margin-bottom: 0px;"><b>KICKER STATS</b></h4>` +
     `<p style="margin-bottom: 0px;"><b>Field Goal Completion Rate: </b>${query["Overall"]["Field Goal Completion Rate"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Kickoff Yards Per Game: </b>${query["Overall"]["Kickoff Yards Per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Kickoff Yards: </b>${query["Overall"]["Total Kickoff Yards"]}</p>` +
     `</div>` +
     `<div class="content" style="padding: 0% 0% 3% 2%;">` +
     `<h4 style="margin-bottom: 0px;"><b>DEFENSE STATS</b></h4>` +
     `<p style="margin-bottom: 0px;"><b>Kick/Punt Blocked: </b>${query["Overall"]["Kick/Punt Blocked"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Kick/Punts Blocked: </b>${query["Overall"]["Total Kick/Punts Blocked"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Fumbles forced per Game: </b>${query["Overall"]["Fumbles forced per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Fumbles forced: </b>${query["Overall"]["Total Fumbles forced"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Sack Yards per Game: </b>${query["Overall"]["Sack Yards per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Sack Yards: </b>${query["Overall"]["Total Sack Yards"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Passes Broken per Game: </b>${query["Overall"]["Passes Broken per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Passes broken: </b>${query["Overall"]["Total Passes broken"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>QB Hurries per Game: </b>${query["Overall"]["QB Hurries per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total QB Hurries: </b>${query["Overall"]["Total QB Hurries"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Tackles per Game: </b>${query["Overall"]["Tackles per Game"]}</p>` +
     `<p style="margin-bottom: 0px;"><b>Total Tackles: </b>${query["Overall"]["Total Tackles"]}</p>` +
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
     `</div>` +
     `<div class="accordion_container">`
  ;

  td_list["Overall"] = "overall_td_graph";
  yards_list["Overall"] = "overall_yards_render";

  // Create season accordion menu dynamically
  query.Seasons.forEach (function (season) {
        var height = "";
        var weight = "";

        if (query[season].hasOwnProperty("Height")) {
            height = `<p style="padding: 0% 4% 0% 4%;"><b>Height:</b> ${query[season]["Height"]} inches</p>`;
        }

        if (query[season].hasOwnProperty("Weight")) {
            weight = `<p style="padding: 0% 4% 0% 4%;"><b>Weight:</b> ${query[season]["Weight"]} pounds</p>`;
        }

        document.getElementById("render").innerHTML += `<div class="accordion_head">${season}<span class="plusminus">+</span></div>` +
            `<div class="accordion_body" style="display: none;">` +
            `<section class="banner style1 onload-content-fade-in">` +
            `<div class="content" style="padding: 0% 0% 5% 0%; -webkit-align-self: flex-start;align-self: flex-start;">` +
            `<h4 style="padding: 3% 4% 0% 4%;margin-bottom: 0px;"><b>GENERAL</b></h4>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Team: </b>${query[season]["Team Name"]}</p>` +
            height +
            weight +
            `<h4 style="padding: 1% 4% 0% 4%;margin-bottom: 0px;"><b>OFFENSE STATS</b></h4>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Pass Completion Rate: </b>${query[season]["Pass Completion Rate"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>TD Average per Game: </b>${query[season]["TD Average per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Yards per Game: </b>${query[season]["Yards per Game"]}</p>` +
            `<h4 style="padding: 1% 4% 0% 4%;margin-bottom: 0px;"><b>KICKER STATS</b></h4>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Field Goal Completion Rate: </b>${query[season]["Field Goal Completion Rate"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Kickoff Yards Per Game: </b>${query[season]["Kickoff Yards Per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Kickoff Yards: </b>${query[season]["Total Kickoff Yards"]}</p>` +
            `</div>` +
            `<div class="content" style="padding: 0% 0% 5% 0%;">` +
            `<h4 style="padding: 3% 4% 0% 4%;margin-bottom: 0px;"><b>DEFENSE STATS</b></h4>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Kick/Punt Blocked: </b>${query[season]["Kick/Punt Blocked"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Kick/Punts Blocked: </b>${query[season]["Total Kick/Punts Blocked"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Fumbles forced per Game: </b>${query[season]["Fumbles forced per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Fumbles forced: </b>${query[season]["Total Fumbles forced"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Sack Yards per Game: </b>${query[season]["Sack Yards per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Sack Yards: </b>${query[season]["Total Sack Yards"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Passes Broken per Game: </b>${query[season]["Passes Broken per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Passes broken: </b>${query[season]["Total Passes broken"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>QB Hurries per Game: </b>${query[season]["QB Hurries per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total QB Hurries: </b>${query[season]["Total QB Hurries"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Tackles per Game: </b>${query[season]["Tackles per Game"]}</p>` +
            `<p style="padding: 0% 4% 0% 4%;"><b>Total Tackles: </b>${query[season]["Total Tackles"]}</p>` +
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
});