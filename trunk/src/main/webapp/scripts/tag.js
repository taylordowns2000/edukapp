/**
 * 
 */
$(document).ready(function() {
	tagid = $('#tagid').val();
	
	$.getJSON('api/tag/' + tagid, function(data) {		
		$('.breadcrumb').append(''+
		  '  <li class="active"> '+
		  data.tagtext+
		  '  </li>');
	});
	
	$.getJSON('api/tag/' + tagid +"/widgets", function(data) {
		for ( var i = 0; i < data.SearchResults.length; i++) {
            $('#widget-tagged-as-results').append(''+renderWidget(data.SearchResults[i].id, data.SearchResults[i].icon, data.SearchResults[i].name, data.SearchResults[i].description));	    
		}
	});
	
});
