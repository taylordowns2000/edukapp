$(document).ready(function(){
	$.getJSON('/api/tag/GET/popular', function(data){
		console.log(data);
		for (var i=0;i<data.length;i++){
			$('#footer-tags ul').append('<a class="btn btn-info" href="/tag/'+data[i][0].id+'">'+
					'<i class="icon-tag icon-white"></i>'+
					data[i][0].tagtext+' ('+data[i][1]+')'+
					'</a>');
		}
		
	});
});