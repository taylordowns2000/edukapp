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
  //$("h1").text("Results for '"+q+"'");
  $('.breadcrumb').append(''+
		  '  <li class="active"> '+
		  '\''+q+'\''+
		  '  </li>');
  
  //
  // Execute query
  //
  $.getJSON('/api/search?q='+q+"&start="+start, displaySearchResults);

});

function displaySearchResults(data){
    var number_of_results = parseInt(data.number_of_results);
    var searchResultsInfo = document.createElement("span");
    $(searchResultsInfo).attr("id","search_results_info");
    $(searchResultsInfo).addClass("pull-right");
    $(searchResultsInfo).addClass("help-inline");
    $('.breadcrumb').last().append(searchResultsInfo);
    
    
    if (isNaN(number_of_results) || number_of_results==0){
      $("#search_results_info").text("No results found");
    } else {
      var end = start + data.widgets.length;
      $("#search_results_info").text("showing results "+(start+1)+" to "+end+" of "+number_of_results);    
    }
    
    for (var i=0;i<data.widgets.length;i++){
    	//console.log(data.widgets[i]);
			$('#search_results').append(''+		
		'	<li class="span3">'+
		'	<div class="thumbnail">'+
		'		<img src="http://placehold.it/260x180" alt="">'+
		'		<div class="caption">'+
		'			<h5>'+data.widgets[i].name+'</h5>'+
		'			<p>'+data.widgets[i].description.description+'</p>'+
		'			<p>'+
		'				<a href="/widget/'+data.widgets[i].id+'" class="btn btn-primary">Read more >>></a>'+
		'			</p>'+
		'		</div>'+
		'	</div>'+
		'</li>');
		
    	
    	//showWidget(data.widgets[i]);
    }
}
/*
 * <div class="pagination">
                        <ul>
                            <li><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>
                        </ul>
                      </div>
                      
                      <div class="pagination">
  <ul>
    <li><a href="#">Prev</a></li>
    <li class="active">
      <a href="#">1</a>
    </li>
    <li><a href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">4</a></li>
    <li><a href="#">Next</a></li>
  </ul>
</div>
                      
                      
 */



//function showWidget(widget){
//    var resultItem = document.createElement("li");
//    $(resultItem).html("<a href='widget.jsp?id="+widget.id+"'>"+widget.name+"</a>");
//    $(resultItem).appendTo('#search_results');
//}

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