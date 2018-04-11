function validate(form) {
  if(form.preventDefault) {
    console.log("what are you doing");
  } else {
    console.log("You are now in the validate function");
  }
}

function getMusic(params) {
  console.log(params);
}

// 
// var form = document.getElementById('dashboard-form');
// if (form.attachEvent) {
//     form.attachEvent("submit", validate);
// } else {
//     form.addEventListener("submit", validate);
// }
