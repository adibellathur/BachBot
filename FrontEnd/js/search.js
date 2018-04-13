function validateSearch(form) {
  var search = document.getElementById("searchbar").value;
  console.log("search = " + search);
  var params = {
    "search": search
  }
  getSearchResults(params);
  sessionStorage.setItem("view", "search")
  //loadContent();
  return false;
}

function getSearchResults(params) {
  console.log(params);
  // $.ajax({
  //   url: '',
  //   data: { format: 'json' },
  //   error: function() {
  //     $('#info').html('<p>An error has occurred</p>');
  //   },
  //   dataType: 'jsonp',
  //   success: function(data) { },
  //   type: 'GET'
  // });
}
