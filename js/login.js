/*(
  Logic for logging into the BachBot Application
  cleans up input, requires email, and if email and password
  are valid it sends you to your profile page;
)*/

function validateForm() {
  console.log("You are in the validate page");
  var username = document.getElementById("materialFormLoginUsername").value;
  var password = document.getElementById("materialFormLoginPassword").value;
  console.log("username: " + username + ", password: " + password);
  if(username == "" || password == "") {
    return false;
  } else {
    
  }

}
