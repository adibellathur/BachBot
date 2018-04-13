function validateDashboard(form) {
  var key = document.getElementById("key-signature").value;
  var tempo = document.getElementById("tempo").value;
  var option1 = document.getElementById("option1").checked;
  var option2 = document.getElementById("option2").checked;
  var option3 = document.getElementById("option3").checked;
  console.log("key = " + key);
  console.log("tempo = " + tempo);
  console.log("option1 = " + option1);
  console.log("option2 = " + option2);
  console.log("option3 = " + option3);
  var params = {
    "key": key,
    "tempo": tempo,
    "option1": option1,
    "option2": option2,
    "option3": option3
  }
  getMusic(params);
  return false;
}

function getMusic(params) {
  console.log(params);
  // $.ajax({
  //   url: '',
  //   data: { format: 'json' },
  //   error: function() {
  //     $('#info').html('<p>An error has occurred</p>');
  //   },
  //   dataType: 'jsonp',
  //   success: function(data) { },
  //   type: 'GET'
  // });
}
