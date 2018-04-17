/*
  Main Window:
  Handles window interaction and moving between pages,
  as well as persistent storage of useful data
*/

//Run this on startup
const Store = require('electron-store');
const store = new Store();
var userdata = require(__dirname + "/user.json");


window.onload = start;

function start() {
  console.log(store.get("userid"));
  if(sessionStorage.getItem("view") === null) {
    sessionStorage.setItem("view", "Browse");
  }
  loadContent();
  loadUser();
  loadProfileContent();
  loadBrowseContent();
}

function loadContent() {
  var views = document.getElementsByClassName("content");
  for(let v of views) {
    v.style.display = "none";
  }
  document.getElementById(sessionStorage.getItem("view")).style.display = "block";

  var sidebar = document.getElementsByClassName("nav-link");
  for(let s of sidebar) {
    if(s.innerHTML == sessionStorage.getItem("view")) {
      s.style.backgroundColor = "#2bbbad";
    } else {
      s.style.backgroundColor = "";
    }
  }
}

function loadBrowse() {
  console.log("browse clicked");
  sessionStorage.setItem("view", "Browse");
  loadContent();
  loadBrowseContent();
}

function loadBrowseContent() {
  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/getAllUsers',
    data: {
      format: 'json'
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'json',
    success: function(data) {
      document.getElementById("top-users-1").innerHTML = "";
      document.getElementById("top-users-2").innerHTML  = "";
      for(var i=0 ; i<data.length && i < 10 ; i++) {
        var user = data[i];
        //console.log(user.name);
        if(i / 5 < 1) {
          document.getElementById("top-users-1").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.username + "</p></li>";
        } else {
          document.getElementById("top-users-2").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.username + "</p></li>";
        }
      }
    },
    type: 'GET'
  });

  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/getTopSongs',
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
      document.getElementById("top-songs-1").innerHTML = "";
      document.getElementById("top-songs-2").innerHTML  = "";
      var url = "http://localhost:8080/CSCI201-FinalProject/";
      for(var i=0 ; i<data.length && i<4 ; i++) {
        var song = data[i];
        if(i / 2 < 1) {
          document.getElementById("top-songs-1").innerHTML += "<li class=\"list-group-item\"><h4>" + (i+1) + ". " + "<a href='#' onClick=\"MIDIjs.play('" + url + song.path + "');\">" + song.title + "</a></h4></li>";
        } else {
          document.getElementById("top-songs-2").innerHTML += "<li class=\"list-group-item\"><h4>" + (i+1) + ". " + song.title + "</h4></li>";
        }
      }
    },
    type: 'GET'
  });
}

function loadCreate() {
  console.log("create clicked");
  sessionStorage.setItem("view", "Create");
  loadContent();
}

function loadProfile() {
  console.log("profile clicked");
  sessionStorage.setItem("view", "Profile");
  loadProfileContent();
  loadContent();
}

function loadProfileContent() {
  document.getElementById("userimage-profile").src = store.get("userimage");
  document.getElementById("username-profile").innerHTML = store.get("username");


  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/getDetailsOfUser",
    data: {
      format: 'json',
      username: store.get("username")
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
      document.getElementById("profile-followers").innerHTML = data.followers;
      document.getElementById("profile-following").innerHTML = data.following;
      document.getElementById("profile-saved").innerHTML = data.songs;
    },
    type: 'GET'
  });

  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/getSongsOfUser",
    data: {
      format: 'json',
      username: store.get("username")
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
      var html = "";
      var url = "http://localhost:8080/CSCI201-FinalProject/";
      for(var i=0 ; i<data.length ; i++) {
        html += "<tr>";
        html += "<td>" + (i+1) + ". </td>";
        html += "<td>" + data[i].title + "</td>";
        html += "<td><button type=\"button\" id=\"button-play\" class=\"btn btn-primary btn-lg\" onClick=\"MIDIjs.play('" + url + data[i].path + "');\">"
              + "Play <i class=\"fa fa-play\"> </i></button>";
        // html += "<button type=\"button\" id=\"button_stop\" class=\"btn btn btn-primary btn-lg\" onclick=\"buttonStopPress()\">"
        //       + "Pause <i class=\"fa fa-stop\"></i></button></td>";
        html += "<td><button type=\"button\" id=\"button-save\" class=\"btn btn-primary btn-lg\" onclick=\"buttonRemovePress()\">"
              + "<i>Remove</i></button>";
        html += "</tr>"
      }
      document.getElementById("profile-saved-songs").innerHTML = html;
    },
    type: 'GET'
  });


}

function loadUser() {
  document.getElementById("userimage").src = store.get("userimage");  //userdata.userimage;
  document.getElementById("username").innerHTML = store.get("username");  //userdata.username;
}
