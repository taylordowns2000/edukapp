/**
 * 
 */
$(document).ready(function() {
	tagid = $('#tagid').val();
	
	$.getJSON('/api/tag/' + tagid, function(data) {		
		$('.breadcrumb').append(''+
		  '  <li class="active"> '+
		  data.tagtext+
		  '  </li>');
	});
	
	$.getJSON('/api/tag/' + tagid +"/widgets", function(data) {
		for ( var i = 0; i < data.length; i++) {
            $('#widget-tagged-as-results').append(''+renderWidget(data[i].id, data[i].icon, data[i].name, data[i].description));	    
		}
	});
	
	
});
