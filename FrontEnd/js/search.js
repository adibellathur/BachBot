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
  // console.log(params);
  document.getElementById("you-searched-for").innerHTML = "<h2>You Searched for \"" +
                                                          params.search + "\""
  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/searchSongsByTitle',
    data: {
      format: 'json',
      search: params.search
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
      console.log("IDIOT");
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
    },
    type: 'GET'
  });

  $.ajax({
    url: 'http://localhost:8080/CSCI201-FinalProject/searchUsersByName',
    data: {
      format: 'json',
      search: params.search
    },
    error: function() {
      $('#info').html('<p>An error has occurred</p>');
      console.log("IDIOT");
    },
    dataType: 'json',
    success: function(data) {
      console.log(data);
    },
    type: 'GET'
  });
}
