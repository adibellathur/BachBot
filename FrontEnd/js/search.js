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
      if(data.length > 0) {
        document.getElementById("search-results-songs").innerHTML = "";
        var html = "";
        html += "<table class=\"table table-striped\"><tbody>";
        for(var i=0 ; i<data.length ; i++) {
          url = "http://localhost:8080/CSCI201-FinalProject/" + data[i].path;
          html += "<tr>";
          html += "<td>" + (i+1) + ". </td>";
          html += "<td>" + data[i].title + "</td>";
          html += "<td><button type=\"button\" id=\"button-play\" class=\"btn btn-primary btn-lg\" onClick=\"MIDIjs.play('" + url + "');\">"
                  + "Play <i class=\"fa fa-play\"> </i></button>";
          html += "<td><button type=\"button\" id=\"button-save\" class=\"btn btn-primary btn-lg\" onclick=\"saveSong(\'"+ store.get("userid")+ "\',\'" + data[i].title + "\',\'" + data[i].path + "\')\">"
                  + "<i>Save</i></button>";
          html += "</tr>";
        }
        html += "</tbody></table>";
        document.getElementById("search-results-songs").innerHTML = html;
      }
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
      if(data.length > 0) {
        document.getElementById("search-results-users").innerHTML = "";
        var html = "";
        for(var i=0 ; i<data.length ; i++) {
          html += "<div class=\"row\">";
          html +=   "<div class=\"col-sm-2\">";
          html +=     "<h4>" + (i+1) + "</h4>";
          html +=   "</div>";
          html +=   "<div class=\"col-sm-6\">";
          console.log("You clicked on username: " + data[i].username);
          html +=     "<a href=\"#\" onClick=\"loadUserpage(\'" + data[i].username.toLowerCase() + "\');\"><h4>" + data[i].username + "</h4></a>";
          html +=   "</div>";
          html +=   "<div class=\"col-sm-4 justify-content-center\">"
          html +=     "<img src=\"" + data[i].imageUrl + "\" class=\"results-img\"></img>"
          html +=   "</div>"
          html += "</div>";
        }
        document.getElementById("search-results-users").innerHTML += html;
      }
    },
    type: 'GET'
  });
}
