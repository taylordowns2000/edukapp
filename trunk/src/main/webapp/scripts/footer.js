$(document).ready(function(){
	$.getJSON('/ajaxHandlers/getPopularTags.jsp', function(data){
		console.log(data);
		for (var i=0;i<data.length;i++){
			$('#footer-tags ul').append('<a class="btn btn-info" href="/tag/'+data[i].id+'">'+
					'<i class="icon-tag icon-white"></i>'+
					data[i].tagtext+' ('+data[i].freq+')'+
					'</a>');
		}
		
	});
});