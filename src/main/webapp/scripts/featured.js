$(document).ready(function(){

  //
  // Get featured widgets
  //
  $.getJSON('/featured', function(data){
    $("#main").hide();
  
    //
    // Create carousel
    // 
    var myCarousel = document.createElement("div");
    $(myCarousel).attr("id", "myCarousel");
    $(myCarousel).attr("class", "carousel slide");
    $(myCarousel).css({"background":"gray","color":"white"});
    var innerCarousel = document.createElement("div");
    $(innerCarousel).attr("class","carousel-inner");
    $(innerCarousel).appendTo(myCarousel);
  
    //
    // Load carousel
    //
    var itemClass = "active item"; // first item is active
    for (var i=0;i<data.length;i++){
        
        var div = document.createElement("div");
        $(div).attr("class",itemClass);
        $(div).append("<div class='pull-left' style='border:6px gray solid; margin-left:75px'>"+data[i].renderInfo)+"</div>";
        $(div).append("<div class='pull-left' style='padding:10px'><h3>"+data[i].widgetProfile.name+"</h3><p>"+data[i].widgetProfile.description.description+"</p><p><a class='btn btn-inverse' href='widget.jsp?id="+data[i].widgetProfile.id+"'>Read more</a></p></div>");
        $(div).appendTo(innerCarousel);
        itemClass="item";
    }
    
    $('#myCarousel').carousel('pause');
    
    //
    // Add controls
    //
    $(myCarousel).append('<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>');
    $(myCarousel).append('<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>');
    
    //
    // Show carousel
    //
    $("#main").append(myCarousel).fadeIn("slow");
  
  });

});