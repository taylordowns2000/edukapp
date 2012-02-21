$(document).ready(function(){

  //
  // Get featured widgets
  //
  $.getJSON('/featured', function(data){
  
    //
    // Load carousel
    //
    var itemClass = "active item"; // first item is active
    for (var i=0;i<data.length;i++){
        
        var div = document.createElement("div");
        $(div).attr("class",itemClass);
        $(div).append("<div class='pull-left' style='border:6px gray solid; margin-left:75px'>"+data[i].renderInfo)+"</div>";
        $(div).append("<div class='pull-left' style='padding:10px'><h3>"+data[i].widgetProfile.name+"</h3><p>"+data[i].widgetProfile.description.description+"</p></div>");
        $(div).append("<a class='btn btn-inverse' href='widget.jsp?id="+data[i].widgetProfile.id+"'>Read more</a>");
        $(div).appendTo(".carousel-inner");
        itemClass="item";
    }
    
    $('#myCarousel').carousel('pause');
  
  });

});