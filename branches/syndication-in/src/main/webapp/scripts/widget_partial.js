/**
 * Partial renderer for widgets
 */
function renderWidget(id, icon, name, description){
    
    //
    // Replace with placeholder for now if missing
    //
    if (!icon) icon = "http://placehold.it/64x64"

    var renderedWidget = 
    '<li class="span4 widget-partial-container">'+
    '	 <a href="/widget/'+id+'">'+
    '   <div class="widget-partial">'+
    '		<img class="widget-icon pull-left" src="'+icon+'" alt="">'+
    '			<h4>'+name+'</h4>'+
    '			<p class="caption-description">'+description+'</p>'+
    '	</div>'+
    '    </a>'+
    '</li>';
    
    return renderedWidget;

}