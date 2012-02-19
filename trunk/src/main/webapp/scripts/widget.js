$(document)
		.ready(
				function() {

					widget_id = $('#widgetid').val();
					
					$('#edit-widget-information').click(function(){
						var current_desc = $('#widget-description').text();
						console.log(current_desc);
						$('#widget-description').replaceWith('<div id="widget-description"><div class="controls"><textarea class="input-xlarge" id="textarea" rows="3">'+current_desc+
								'</textarea></div><button type="submit" class="btn btn-primary">Save changes</button><button id="edit-description-cancel" class="btn">Cancel</button></div>');
						
						$('#widget-description button[type="submit"]').click(function(){
							var newText = $('#widget-description textarea').val();
							
							$.ajax({
								  url: "updateWidget?id="+widget_id+"&operation=description&newText="+newText,
								  cache: false,
								  success: function(resp){
									  console.log("s"+resp+"s");
								    if (resp==="update done"){
								    	$('#widget-description').replaceWith('<dl id="widget-description">'+newText+'</dl>');
								    }else {
								    	$('#widget-description').append("1there was an error in your update");
								    }
								  },
								  error:function(){
									  $('#widget-description').append("2there was an error in your update");
								  }
								});
						});
						
						$('#widget-description #edit-description-cancel').click(function(){							
							$('#widget-description').replaceWith('<dl id="widget-description">'+current_desc+'</dl>');
						});
						
						
						
					});
					
					
					var widgetUri;
					//
					// Load the widget profile
					//
					$
							.getJSON(
									'/widget?id='+widget_id,
									function(data) {
										console.log(data);

										widgetUri = data.widgetProfile.uri;

										//
										// Show metadata
										//
										$("#widget_name").text(
												data.widgetProfile.name);
										if (data.uploadedBy) {
											$("#upload-info")
													.text(
															"Uploaded by "
																	+ data.uploadedBy.username);
										}

										//
										// Show description
										//
										if (data.widgetProfile.description.description) {
											$('#widget-description').text(data.widgetProfile.description.description);											
										}
										
										
										//
										// Show tags
										//
										if (data.widgetProfile.tags) {
											var tags = data.widgetProfile.tags;
											for ( var i = 0; i < tags.length; i++) {
												var tag = document.createElement("a");
												var tag_icon = document.createElement("i");
												$(tag_icon).attr("class","icon-tag icon-white");
												$(tag).attr("class", "btn btn-info");
												$(tag).text(tags[i].tagtext);
												$(tag).attr("href","/tags/" + tags[i].id);
												$(tag).prepend(tag_icon);
												$("#widget-tags").append(tag).append(" ");
											}

										}

										//
										// Load similar widget profiles
										//
										$
												.getJSON(
														'/similar?uri='
																+ widgetUri,
														function(similar) {
															for ( var i = 0; i < similar.length; i++) {
																$(
																		"<div>"
																				+ similar[i].name
																				+ "</div>")
																		.hide()
																		.appendTo(
																				"#related-widgets")
																		.fadeIn(
																				"slow");
															}
															;
														});

       //
       // Load reviews
       //
       $.getJSON('/review?uri='+widgetUri, function(reviews){ 
       for(var i=0;i<reviews.length;i++){ 
       
         var li = document.createElement("div");
         $(li).hide();
         
         var wrapper = document.createElement("div");
         $(wrapper).attr("class", "row-fluid");  
                 
         //
         // User avatar, handle and link
         //
         var pic = document.createElement("div");
         $(pic).attr("class", "span1");          
         var img = document.createElement("img");
         $(img).attr("src","http://www.gravatar.com/avatar/205e460b479e2e5b48aec05710c08d50?s=35&d=identicon");
         $(pic).append(img);
         $(pic).append("<h5><a href='#'>"+reviews[i].user+"</a></h5>");
         $(wrapper).append(pic);
         
         //
         // Date, stars and comment
         //
         var item = document.createElement("div");
         $(item).attr("class", "span11");
         
         var iteminfo  = document.createElement("div");
         $(iteminfo).attr("class", "review-item-info-wrapper");
         for (var stars=0;stars<reviews[i].rating;stars++){
           $(iteminfo).append("<i class='icon-star'></i>");
         }
         $(item).append(iteminfo);
         
         var itemtext = document.createElement("p");
         $(itemtext).attr("class", "review-content-text");
         $(itemtext).text(reviews[i].text);
         $(item).append(itemtext);
         $(item).append("<h6>"+reviews[i].time+"</h6>");
         
         $(wrapper).append(item);       
                
         $(li).append(wrapper);
         $(li).appendTo("#user-reviews").fadeIn("slow");
       };
     });     
    
   });

				});
