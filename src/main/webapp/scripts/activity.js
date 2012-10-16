/**
 * 
 */
$(document).ready(function() {
	activityid = $('#activityid').val();
	
	$.getJSON('api/activity/name/'+activityid, function(data) {		
		$('.breadcrumb').append(''+
		  '  <li class="active"> '+
		  data.activitytext+
		  '  </li>');
	});
	
	$.getJSON('api/activity/widgets/'+activityid, function(data) {
		for ( var i = 0; i < data.length; i++) {
            $('#widgets-with-activity').append(''+renderWidget(data[i].id, data[i].icon, data[i].name, data[i].description));	    
		}
	});
	
	
});
