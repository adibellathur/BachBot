window.onload = start;

function start() {
  if(sessionStorage.getItem("view") === null) {
    sessionStorage.setItem("view", "You");
  }
  loadContent();
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

function loadYou() {
  console.log("you clicked");
  sessionStorage.setItem("view", "You");
  loadContent();
}
