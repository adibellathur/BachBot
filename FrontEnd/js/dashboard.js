function validateDashboard(form) {
  //string key, string tempo, string instrument-soprano, string instrument-alto, string chord progression

  var title = document.getElementById("song-title").value;
  var key = document.getElementById("key-signature").value;
  var tempo = document.getElementById("tempo").value;

  var instr1 = document.getElementById("instr1").value;
  var instr2 = document.getElementById("instr2").value;
  var instr3 = document.getElementById("instr3").value;
  var instr4 = document.getElementById("instr4").value;

  var inv1 = document.getElementById("firstInversion").checked;
  var inv2 = document.getElementById("secondInversion").checked;
  var chordProgression = document.getElementById("chordProgression").value;

  // console.log("key = " + key);
  // console.log("tempo = " + tempo);
  // console.log("option1 = " + option1);
  // console.log("option2 = " + option2);
  // console.log("option3 = " + option3);

  var params = {
    "username": store.get("username"),
    "title": title,
    "key": key,
    "tempo": tempo,
    "instr1": instr1,
    "instr2": instr2,
    "instr3": instr3,
    "instr4": instr4,
    "inv1": inv1,
    "inv2": inv2,
    "chordProgression": chordProgression
  }
  getMusic(params);
  return false;
}

function getMusic(params) {
  console.log(params);
  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/generateSongs',
    data: {
      username: params.username,
      title: params.title,
      format: 'json',
      key: params.key,
      tempo: params.tempo,
      instrument1: params.instr1,
      instrument2: params.instr2,
      instrument3: params.instr3,
      instrument4: params.instr4,
      firstInversion: params.inv1,
      secondInversion: params.inv2,
      chordProgression: params.chordProgression
    },
    error: function(xhr, status, err) {
      console.log("ERROR " + status + "YOU MORON: " + err);
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
      var html = "";
      // html += "<div class=\"list-group\">"
      html += "<table class=\"table table-striped\"><tbody>";
      for(var i=0 ; i<data.length ; i++) {
        url = "http://localhost:8080/CSCI201-FinalProject/" + data[i].path;
        html += "<tr>";
        html += "<td>" + (i+1) + ". </td>";
        html += "<td>" + data[i].title + "</td>";
        html += "<td><button type=\"button\" id=\"button-play\" class=\"btn btn-primary btn-lg\" onClick=\"MIDIjs.play('" + url + "');\">"
                + "Play <i class=\"fa fa-play\"> </i></button>";
        html += "<td><button type=\"button\" id=\"button-save\" class=\"btn btn-primary btn-lg\" onclick=\"saveSong(\'"+ store.get("userid")+ "\',\'" + data[i].title + "\',\'" + data[i].path + "\')\">"
                + "<i>Save</i></button>";
        html += "</tr>";
        // html += "<a href=\"#\" class=\"list-group-item waves-effect\" onClick=\"MIDIjs.play('" + url + "');\">";
        // html += "<h5>" + song.title + "</h5>";
        // html += "</a>";
        // html += "<br/>";
      }
      url = "http://localhost:8080/CSCI201-FinalProject/Michael_Jackson-Billie_Jean.midi";
      // html += "</div>";
      html += "</tbody></table>";
      html += "<a href=\"#\" onClick=\"MIDIjs.play('" + url + "');\">Play Billie Jean</a>";

      document.getElementById("dashboard-results").innerHTML = html;
    },
    type: 'GET'
  });
}

function saveSong(userid, songtitle, path) {
  console.log(userid + songtitle + path);
  $.ajax({
    url:"http://localhost:8080/CSCI201-FinalProject/saveSong",
    data: {
      format: "json",
      userId: userid,
      title: songtitle,
      url: path
    },
    error: function(xhr, status, err) {
      console.log("ERROR " + status + "YOU MORON: " + err);
    },
    type: 'GET'
  });
}
