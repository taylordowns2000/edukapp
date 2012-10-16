$(document).ready(function(){

    var id = getParameterByName("id");
    if (!id || id === ""){
        id = getResource();
    }
    
    //
    // Load profile
    //
    $.getJSON('api/user/'+id, function(data){
    
      var name = document.createElement("h2");
      $(name).text(data.realname).appendTo("#user-profile");
      
      if (data.joined){
        var header = $("<h6>Joined</h6>").appendTo("#user-profile");
        var joined = document.createElement("p");
        $(joined).text(data.joined).appendTo("#user-profile");
      }
      
      if (data.lastSeen){
        var header = $("<h6>Last Seen</h6>").appendTo("#user-profile");
        var lastSeen = document.createElement("p");
        $(lastSeen).text(data.lastSeen).appendTo("#user-profile");
      }
      
      if (data.shortbio){
        var header = $("<h6>Bio</h6>").appendTo("#user-profile");
        var bio = document.createElement("p");
        $(bio).text(data.shortbio).appendTo("#user-profile");  
      }
      
      if (data.website){
        var header = $("<h6>Website</h6>").appendTo("#user-profile");
        var website = document.createElement("p");
        $(website).text(data.website).appendTo("#user-profile");   
      }
    
    });
    
    //
    // TODO load user activity
    //

});