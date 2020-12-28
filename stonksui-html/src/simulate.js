$(document).ready(function(){
  setInterval(simulateDay, 10000)
});

URL = "http://localhost:8000/"
SIMULATE_ROUTE = URL + "day"

function simulateDay() {
  $.get( SIMULATE_ROUTE )
    .done(function( data ) {
      console.log(data);
  });

}
