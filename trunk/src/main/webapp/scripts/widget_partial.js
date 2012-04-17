/**
 * Partial renderer for widgets
 */
function renderWidget(id, icon, name, description){
    
    //
    // Replace with placeholder for now if missing
    //
    if (!icon) icon = "http://placehold.it/80x80"

    var renderedWidget = 
    '	<li class="span3">'+
    '	<div class="thumbnail">'+
    '		<img style="max-height:80px; max-width:80px" src="'+icon+'" alt="">'+
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