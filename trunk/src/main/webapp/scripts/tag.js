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
		console.log(data);
		for ( var i = 0; i < data.length; i++) {
			$('#widget-tagged-as-results').append(''+		
		'	<li class="span3">'+
		'	<div class="thumbnail">'+
		'		<img src="http://placehold.it/260x180" alt="">'+
		'		<div class="caption">'+
		'			<h5>'+data[i].name+'</h5>'+
		'			<p>'+data[i].description+'</p>'+
		'			<p>'+
		'				<a href="/widget/'+data[i].id+'" class="btn btn-primary">Read more >>></a>'+
		'			</p>'+
		'		</div>'+
		'	</div>'+
		'</li>');
		}
		
	});
	
	
});
