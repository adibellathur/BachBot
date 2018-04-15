function validateSearch(num) {
  var search = document.getElementById("searchbar-input-" + num).value;
  console.log("search = " + search);
  var params = {
    "search": search
  }
  getSearchResults(params);
  sessionStorage.setItem("view", "Search");
  loadContent();
  return false;
}

function getSearchResults(params) {
  console.log(params);
  document.getElementById("you-searched-for").innerHTML = "<h2>You Searched for \"" +
                                                          params.search + "\""
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
