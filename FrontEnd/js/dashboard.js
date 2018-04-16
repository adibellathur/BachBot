function validateDashboard(form) {
  //string key, string tempo, string instrument-soprano, string instrument-alto, string chord progression

  var key = document.getElementById("key-signature").value;
  var tempo = document.getElementById("tempo").value;

  var instr1 = document.getElementById("instr1").value;
  var instr2 = document.getElementById("instr2").value;
  var instr3 = document.getElementById("instr3").value;
  var instr4 = document.getElementById("instr4").value;

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
    "instrument1": instr1,
    "instrument2": instr2,
    "instrument3": instr3,
    "instrument4": instr4,
    "option1": option1,
    "option2": option2,
    "option3": option3
  }
  getMusic(params);
  return false;
}

function getMusic(params) {
  console.log(params);
  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/allSongs',
    data: { format: 'json' },
    error: function(xhr, status, err) {
      console.log("ERROR " + status + "YOU MORON: " + err);
    },
    dataType: 'json',
    success: function(data) {
      // url = "http://localhost:8080/CSCI201-FinalProject/Mii_Channel.mid";
      url = "http://localhost:8080/CSCI201-FinalProject/Michael_Jackson-Billie_Jean.midi";
      if(data[0].title == 'Ben1') {
        console.log(data[0]);
        document.getElementById("dashboard-results").innerHTML = "<a href=\"#\" onClick=\"MIDIjs.play('" + url + "');\">Play Billie Jean</a>"
        //playMidi(url);
      }
    },
    type: 'GET'
  });
}

function playMidi(url){
  MIDIjs.play(url);
}
