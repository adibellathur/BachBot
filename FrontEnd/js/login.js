/*(
  Logic for logging into the BachBot Application:
  cleans up input, requires email, and if email and password
  are valid it sends you to your profile page;
)*/
const Store = require('electron-store');
const store = new Store();

function validateForm() {
  console.log("You are in the validate page");
  var username = document.getElementById("materialFormLoginUsername").value;
  var username_split = username.split("@");
  console.log(username_split[0]);
  username = username_split[0];
  var password = document.getElementById("materialFormLoginPassword").value;
  var valid = false;
  console.log("username: " + username + ", password: " + password);
  if(username == "" || password == "") {
    return false;
  } else if(username === 'me') {
    valid = true;
    store.set("username", "Guest");
    store.set("userimage", "../image/guest.png");
  } else {
    $.ajax({
      url: 'http://localhost:8080/CSCI201-FinalProject/login',
      data: {
        format: 'json',
        'username': username,
        'password': password
      },
      error: function() {
        $('#info').html('<p>An error has occurred</p>');
        console.log("this is wrong");
      },
      dataType: 'json',
      success: function(data) {
        console.log("SUCCESS");
        console.log(data);
        if(data.exists === true) {
          console.log("I AM HERE");
          valid = true;
          store.set("username", data.user.username);
          store.set("userimage", data.user.imageUrl);
        }
      },
      type: 'POST',
      async: false
    });
    return valid;
  }
}
