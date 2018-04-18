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

function loadUserpage(username) {
  console.log("userpage clicked: " + username);
  sessionStorage.setItem("view", "Userpage");
  loadUserpageContent(username);
  loadContent();
}

function loadBrowseContent() {
  // Get Top Users
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
      // document.getElementById("top-users-1").innerHTML = "";
      // document.getElementById("top-users-2").innerHTML  = "";
      document.getElementById("browse-top-users").innerHTML = "";
      var html = "";
      for(var i=0 ; i<data.length && i < 10 ; i++) {
        var user = data[i];
        html += "<div class=\"row\">";
        html +=   "<div class=\"col-sm-2\">";
        html +=     "<h4>" + (i+1) + ". </h4>";
        html +=   "</div>";
        html +=   "<div class=\"col-sm-2 justify-content-center\">"
        html +=     "<img src=\"" + data[i].imageUrl + "\" class=\"results-img\"></img>"
        html +=   "</div>"
        html +=   "<div class=\"col-sm-4\">";
        html +=     "<a href=\"#\" onClick=\"loadUserpage(\'" + data[i].username + "\');\"><h4>" + data[i].username + "</h4></a>";
        html +=   "</div>";
        html += "</div>";
        // if(i / 5 < 1) {
        //   document.getElementById("top-users-1").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.username + "</p></li>";
        // } else {
        //   document.getElementById("top-users-2").innerHTML += "<li class=\"list-group-item\"><p>" + (i+1) + ". " + user.username + "</p></li>";
        // }
      }
      document.getElementById("browse-top-users").innerHTML += html;
    },
    type: 'GET'
  });

  // Get Top Songs
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
          document.getElementById("top-songs-2").innerHTML += "<li class=\"list-group-item\"><h4>" + (i+1) + ". " + "<a href='#' onClick=\"MIDIjs.play('" + url + song.path + "');\">" + song.title + "</a></h4></li>";
        }
      }
    },
    type: 'GET'
  });
}

function loadProfileContent() {
  document.getElementById("userimage-profile").src = store.get("userimage");
  document.getElementById("username-profile").innerHTML = store.get("username");
  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/getDetailsOfUser",
    data: {
      format: 'json',
      userId: store.get("userid"),
      target: ""
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
        html += "<td><button type=\"button\" id=\"button-save\" class=\"btn btn-primary btn-lg\" onclick=\"removeSong(\'" + data[i].title + "\')\">"
              + "<i>Remove</i></button>";
        html += "</tr>"
      }
      document.getElementById("profile-saved-songs").innerHTML = html;
    },
    type: 'GET'
  });
}

function loadUserpageContent(username) {
  document.getElementById("username-userpage").innerHTML = username;
  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/getDetailsOfUser",
    data: {
      format: 'json',
      userId: store.get("userid"),
      target: username
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
      document.getElementById("userpage-followers").innerHTML = data.followers;
      document.getElementById("userpage-following").innerHTML = data.following;
      document.getElementById("userpage-saved").innerHTML = data.songs;
      // document.getElementById("userimage-userpage").src = data.imageUrl;
      document.getElementById("following-userpage-switch").checked = data.isFollowing;
    },
    type: 'GET'
  });

  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/getSongsOfUser",
    data: {
      format: 'json',
      username: username
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
        html += "<td><button type=\"button\" id=\"button-save\" class=\"btn btn-primary btn-lg\" onclick=\"saveSong(\'" + store.get("userid") + "\',\'" + data[i].title + "\',\'" + data[i].path + "\');\">"
              + "<i>Save</i></button>";
        html += "</tr>"
      }
      document.getElementById("userpage-saved-songs").innerHTML = html;
    },
    type: 'GET'
  });
}

function followChange() {
  if(document.getElementById("following-userpage-switch").checked === true) {
    console.log("checked - ready to follow");
    $.ajax({
      url: "http://localhost:8080/CSCI201-FinalProject/addFollowing",
      data: {
        format: "json",
        userId: store.get("userid"),
        following: document.getElementById("username-userpage").innerHTML
      },
      error: function() {
        $('#info').html('<p>An error has occurred</p>');
      },
      success: function(data) {
        console.log("this worked: " + data);
      },
      type: "GET"
    });
  } else {
    console.log("not checked - ready to unfollow");
    $.ajax({
      url: "http://localhost:8080/CSCI201-FinalProject/removeFollowing",
      data: {
        format: "json",
        userId: store.get("userid"),
        target: document.getElementById("username-userpage").innerHTML
      },
      error: function() {
        $('#info').html('<p>An error has occurred</p>');
      },
      success: function(data) {
        console.log("this worked: " + data);
      },
      type: "GET"
    });
  }
}

function removeSong(title) {
  console.log("You are about to remove a song");
  $.ajax({
    url: "http://localhost:8080/CSCI201-FinalProject/removeSong",
    data: {
      format: "json",
      username: store.get("username"),
      title: title
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
    },
    type: "GET"
  });
  loadProfileContent();
}

function loadUser() {
  document.getElementById("userimage").src = store.get("userimage");  //userdata.userimage;
  document.getElementById("username").innerHTML = store.get("username");  //userdata.username;
}
