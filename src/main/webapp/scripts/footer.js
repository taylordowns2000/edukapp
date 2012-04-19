$(document).ready(function(){
	$.getJSON('/api/tag/popular', function(data){
		//console.log(data);
		for (var i=0;i<data.length;i++){
			$('#footer-tags ul').append('<a class="btn btn-info" href="/tag/'+data[i][0].id+'">'+
					'<i class="icon-tag icon-white"></i>'+
					data[i][0].tagtext+' ('+data[i][1]+')'+
					'</a>');
		}
		
	});

	//should it be all or popular??
	$.getJSON('/api/activity?operation=popular',function(data){
		for (var i=0;i<data.length;i++){
			$('#footer-activities ul').append(''+
				'<a class="btn btn-warning" href="/activity/'+data[i][0].id+'">'+
				'<i class="icon-ok-circle icon-white"></i>'+
				data[i][0].activitytext+' ('+data[i][1]+')'+
				'</a>');
		}
	});
});