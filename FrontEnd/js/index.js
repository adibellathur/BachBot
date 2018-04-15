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
  if(sessionStorage.getItem("view") === null) {
    sessionStorage.setItem("view", "Create");
  }
  loadContent();
  loadUser();
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
    url: 'https://jsonplaceholder.typicode.com/users',
    data: {
      format: 'json'
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'jsonp',
    success: function(data) {
      document.getElementById("top-users-1").innerHTML = "";
      document.getElementById("top-users-2").innerHTML  = "";
      for(var i=0 ; i<10 ; i++) {
        var user = data[i];
        console.log(user.name);
        if(i / 5 < 1) {
          document.getElementById("top-users-1").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.name + "</p></li>";
        } else {
          document.getElementById("top-users-2").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.name + "</p></li>";
        }
      }
    },
    type: 'GET'
  });

  $.ajax({
    url: 'https://jsonplaceholder.typicode.com/users',
    data: {
      format: 'json'
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'jsonp',
    success: function(data) {
      document.getElementById("top-songs-1").innerHTML = "";
      document.getElementById("top-songs-2").innerHTML  = "";
      for(var i=0 ; i<4 ; i++) {
        var user = data[i];
        console.log(user.name);
        if(i / 2 < 1) {
          document.getElementById("top-songs-1").innerHTML += "<li class=\"list-group-item\"><h4>" + (i+1) + ". " + user.name + "</h4></li>";
        } else {
          document.getElementById("top-songs-2").innerHTML += "<li class=\"list-group-item\"><h4>" + (i+1) + ". " + user.name + "</h4></li>";
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
  loadContent();
}

function loadUser() {
  document.getElementById("userimage").src = store.get("userimage");  //userdata.userimage;
  document.getElementById("username").innerHTML = store.get("username");  //userdata.username;
}
