/*(
  Logic for logging into the BachBot Application:
  cleans up input, requires email, and if email and password
  are valid it sends you to your profile page;
)*/
const Store = require('electron-store');
const store = new Store();
var userdata = require(__dirname + "/user.json");
var fs = require("fs");


console.log(userdata);

function validateForm() {
  console.log("You are in the validate page");
  var username = document.getElementById("materialFormLoginUsername").value;
  var password = document.getElementById("materialFormLoginPassword").value;
  console.log("username: " + username + ", password: " + password);
  console.log(userdata.username);
  if(username == "" || password == "") {
    return false;
  } else {
    store.set("username", "Guest");
    store.set("userimage", __dirname + "/../image/guest.png");
    console.log(store.get("username"));
    userdata.username = username;
    fs.writeFileSync(__dirname + '/user.json', JSON.stringify(userdata));
    // sleep(10000);
    return true;
  }
}

  // $.ajax({
  //   url: 'http://api.joind.in/v2.1/talks/10889',
  //   data: {
  //     format: 'json'
  //   },
  //   error: function() {
  //     $('#info').html('<p>An error has occurred</p>');
  //   },
  //   dataType: 'jsonp',
  //   success: function(data) {
  //     localStorage.setItem("username") = "Guest";
  //     localStorage.setItem("userimage") = "../image/guest.png";
  //     console.log(sessionStorage.getItem("userimage"));
  //     return true;
  //   },
  //   type: 'GET'
  // });
