/**
 * Partial renderer for widgets
 */
function renderWidget(id, icon, name, description){
    
    //
    // Replace with placeholder for now
    //
    icon = "http://placehold.it/128x128"

    var renderedWidget = 
    '	<li class="span3">'+
    '	<div class="thumbnail">'+
    '		<img src="'+icon+'" alt="">'+
    '		<div class="caption">'+
    '			<h5>'+name+'</h5>'+
    '			<p class="caption-description">'+description+'</p>'+
    '			<p>'+
    '				<a href="/widget/'+id+'" class="btn btn-primary btn-mini">Read more &raquo;</a>'+
    '			</p>'+
    '		</div>'+
    '	</div>'+
    '</li>';
    
    return renderedWidget;

}