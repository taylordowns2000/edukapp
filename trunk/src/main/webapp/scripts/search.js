var q;
var start;

$(document).ready(function(){

  //
  // Get query
  //
  q = getParameterByName("q");
  start = parseInt(getParameterByName("start"));
  if (isNaN(start)) start = 0;
  
  //
  // Update page title
  //
  $("h1").text("Results for '"+q+"'");
  
  //
  // Execute query
  //
  $.getJSON('/search?q='+q+"&start="+start, displaySearchResults);

});

function displaySearchResults(data){
    var number_of_results = parseInt(data.number_of_results);
    if (isNaN(number_of_results)){
      $("#search_results_info").text("No results found");
    } else {
      var end = start + data.widgets.length;
      $("#search_results_info").text("showing results "+(start+1)+" to "+end+" of "+number_of_results);    
    }
    
    for (var i=0;i<data.widgets.length;i++){
      showWidget(data.widgets[i]);
    }
}

function showWidget(widget){
    var resultItem = document.createElement("li");
    $(resultItem).html("<a href='widget.jsp?id="+widget.id+"'>"+widget.name+"</a>");
    $(resultItem).appendTo('#search_results');
}

function createPaginationLinks(number_of_results, current_page){

}

function getParameterByName(name)
{
  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
  var regexS = "[\\?&]" + name + "=([^&#]*)";
  var regex = new RegExp(regexS);
  var results = regex.exec(window.location.search);
  if(results == null)
    return "";
  else
    return decodeURIComponent(results[1].replace(/\+/g, " "));
}