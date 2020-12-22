SELECTED_COMPANY = "";

$(document).ready(function(){
  getCompanies();
  getPlayer();
  setupD3();
  setInterval(simulateDay, 1000)

  $("#buyStock").click(function() {
    console.log("Buy button clicked");
    $.post( BUY_ROUTE, SELECTED_COMPANY + ",1" )
    .done(function( data ) {
      console.log(data);
    });
  });

  $("#sellStock").click(function() {
    console.log("Sell button clicked");
    $.post( SELL_ROUTE, SELECTED_COMPANY + ",1" )
    .done(function( data ) {
      console.log(data);
    });
  });

});

$(document).on('click', '.stockButton', function () {
    console.log("Stock button clicked")
    var stockCompany = $(this).val();
    SELECTED_COMPANY = stockCompany;
    d3.select("svg").remove();
    setupD3();
});

URL = "http://localhost:8000/"
COMPANY_ROUTE = URL + "companies"
PLAYER_ROUTE = URL + "player"
BUY_ROUTE = URL + "buy"
SELL_ROUTE = URL + "sell"
SIMULATE_ROUTE = URL + "day"

function simulateDay() {
  $.get( SIMULATE_ROUTE )
    .done(function( data ) {
      console.log(data);
      d3.select("svg").remove();
      setupD3();
      getPlayer();
  });
}

function getPlayer() {
  $.get( PLAYER_ROUTE, function( data ) {
    var object = JSON.parse(data);
    $( "#cash" ).html( "$" + object["cash"] );
    $( "#netWorth" ).html( "$" + object["netWorth"] );
  });
}

function getCompanies() {
  $.get( COMPANY_ROUTE, function( data ) {
    var object = JSON.parse(data);
    var html_text = "";

    for (item in object) {
      var stockName = object[item]["name"];
      var stockPrice = object[item]["stockPrice"];
      html_text = html_text.concat("<li class=\"list-group-item\"><button class=\"stockButton\" value=\"" + stockName + "\">" + stockName + "</button><p>" + stockPrice + "</p></li>\n");
    }

    $( "#companyList" ).html( html_text );

    for (item in object) {
      SELECTED_COMPANY = object[item]["name"]
    }
  });
}

function setupD3() {
  console.log("Selected company: " + SELECTED_COMPANY);

  $.get( COMPANY_ROUTE, function( data ) {
    var object = JSON.parse(data);

    for (index in object) {
      if (object[index]["name"] === SELECTED_COMPANY) {
        var dataset = object[index]["priceHistory"];

    		var m = [80, 80, 10, 80]; // margins
    		var w = 1000 - m[1] - m[3]; // width
    		var h = 800 - m[0] - m[2]; // height

    		var x = d3.scaleLinear().domain([0, dataset.length]).range([0, w]);
    		var y = d3.scaleLinear().domain([d3.min(dataset), d3.max(dataset)]).range([h, 0]);

    		var line = d3.line()
    			.x(function(d,i) {
    				return x(i);
    			})
    			.y(function(d) {
    				return y(d);
    			})

          var n = dataset.length;

    			var graph = d3.select("#my_dataviz").append("svg:svg")
    			      .attr("width", w + m[1] + m[3])
    			      .attr("height", h + m[0] + m[2])
    			      .append("svg:g")
    			      .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

          var xAxis = d3.axisBottom(x).tickFormat(function(d){ return d.x;});
          var yAxis = d3.axisLeft(y);

    			graph.append("svg:g")
    			      .attr("class", "x axis")
    			      .attr("transform", "translate(0," + h + ")")
    			      .call(xAxis);

          graph.append("svg:g")

    			graph.append("svg:g")
    			      .attr("class", "y axis")
    			      .attr("transform", "translate(-25,0)")
    			      .call(yAxis);

      			graph.append("svg:path")
            .attr("fill", "none")
            .attr("stroke", "steelblue")
            .attr("stroke-width", 1.5)
            .attr("d", line(dataset));

      }
    }
  });
}
