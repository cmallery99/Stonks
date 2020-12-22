$(document).ready(function(){
  document.getElementById("year").innerHTML = new Date().getFullYear();
  getPlayer();
});

URL = "http://localhost:8000/"
PLAYER_ROUTE = URL + "player"

function getPlayer() {
  $.get( PLAYER_ROUTE, function( data ) {
    var object = JSON.parse(data);
    $( "#cash" ).html( "$" + object["cash"] );
    $( "#netWorth" ).html( "$" + object["netWorth"] );
  });
}
