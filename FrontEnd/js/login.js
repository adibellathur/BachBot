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
  var password = document.getElementById("materialFormLoginPassword").value;
  var valid = false;
  console.log("username: " + username + ", password: " + password);
  if(username == "" || password == "") {
    return false;
  } else {
    $.ajax({
      url: 'http://localhost:8080/CSCI201-FinalProject/login',
      data: {
        format: 'json',
        'username': username
      },
      error: function() {
        $('#info').html('<p>An error has occurred</p>');
      },
      dataType: 'json',
      success: function(data) {
        console.log("SUCCESS");
        console.log(data);
        valid = data;
        store.set("username", "Guest");
        store.set("userimage", "../image/guest.png");
        console.log(store.get("userimage"));
        console.log("-" + username + "-");
        if(username === "me@me")  {
          console.log("I am here");
          valid = true;
          console.log("In AJAX: " + valid);
        }
      },
      type: 'POST',
      async: false
    });
    console.log("OUTSIDE AJAX: " + valid);
    return valid;
  }
}
