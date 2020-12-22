$(document).ready(function(){
  setupD3();
  getPlayer();
  setInterval(simulateDay, 1000)
});

function getPlayer() {
  $.get( PLAYER_ROUTE, function( playerData ) {
    $.get( COMPANY_ROUTE, function( companyData ) {
      var playerObject = JSON.parse(playerData);
      var companyObject = JSON.parse(companyData);

      $( "#cash" ).html( "$" + playerObject["cash"] );
      $( "#netWorth" ).html( "$" + playerObject["netWorth"] );

      var html_text = "";

      for (stockName in playerObject["allShares"]) {
        var amount = playerObject["allShares"][stockName];
        if (amount > 0) {

          for (index in companyObject) {
            if (companyObject[index]["name"] === stockName) {
              stockPrice = companyObject[index]["stockPrice"];
              html_text = html_text.concat("<li class=\"list-group-item\"><a href=\"#\">" + stockName + "</a><p>Amount: " + amount + "</p><p>Price: $" + stockPrice + "</p></li>\n");
            }
          }
        }
      }
      $( "#boughtStocks" ).html( html_text );
    });
  });
}

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
  });
  getPlayer();
}


function setupD3() {
  // set the dimensions and margins of the graph
  var width = 450
      height = 450
      margin = 40

  // The radius of the pieplot is half the width or half the height (smallest one). I subtract a bit of margin.
  var radius = Math.min(width, height) / 2 - margin

  // append the svg object to the div called 'my_dataviz'
  var svg = d3.select("#my_dataviz")
    .append("svg")
      .attr("width", width)
      .attr("height", height)
    .append("g")
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

  var playerShares = {}

  $.get( PLAYER_ROUTE, function( data ) {
    var object = JSON.parse(data);

    for (stockName in object["allShares"]) {
      var stockPrice = object["allShares"][stockName];
      if (stockPrice > 0) {
        playerShares[stockName] = stockPrice;
      }
    }

    console.log(playerShares);

    // set the color scale
    var color = d3.scaleOrdinal()
      .domain(data)
      .range(d3.schemeSet2);

    // Compute the position of each group on the pie:
    var pie = d3.pie()
      .value(function(d) {return d.value; })
    var data_ready = pie(d3.entries(playerShares))
    // Now I know that group A goes from 0 degrees to x degrees and so on.

    // shape helper to build arcs:
    var arcGenerator = d3.arc()
      .innerRadius(0)
      .outerRadius(radius)

    // Build the pie chart: Basically, each part of the pie is a path that we build using the arc function.
    svg
      .selectAll('mySlices')
      .data(data_ready)
      .enter()
      .append('path')
        .attr('d', arcGenerator)
        .attr('fill', function(d){ return(color(d.data.key)) })
        .attr("stroke", "black")
        .style("stroke-width", "2px")
        .style("opacity", 0.7)

    // Now add the annotation. Use the centroid method to get the best coordinates
    svg
      .selectAll('mySlices')
      .data(data_ready)
      .enter()
      .append('text')
      .text(function(d){ return d.data.key + ": " + d.data.value})
      .attr("transform", function(d) { return "translate(" + arcGenerator.centroid(d) + ")";  })
      .style("text-anchor", "middle")
      .style("font-size", 17)
  });

}
