/*
  Main Window:
  Handles window interaction and moving between pages,
  as well as persistent storage of useful data
*/

//Run this on startup

var userdata = require(__dirname + "/user.json");


window.onload = start;

function start() {
  if(sessionStorage.getItem("view") === null) {
    sessionStorage.setItem("view", "Create");
  }
  loadContent();
  loadUser();
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
  //store.setItem('username', 'guest');
  document.getElementById("userimage").src = userdata.userimage; //store.getItem("userimage");
  document.getElementById("username").innerHTML = userdata.username;  //store.getItem("username");
}
