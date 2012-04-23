$(document).ready(

function () {
    "use strict";
    
    //
    // Get the current widget id
    //
    var widget_id = $('#widgetid').val();

    //
    // set up the rating module
    //
    $('#rating-module').raty({
        click   : function(score,evt){
            console.log('id='+$(this).attr('id'));
            console.log('score='+score);
            console.log('event=');
            console.log(evt);
            

            var rate  = {};
            rate.rating = score;
            rate.userId = $('#logged-in-user-id').text();


            $.ajax({
                type: 'POST',
                dataType: 'json',
                url:"/api/widget/"+widget_id+"/rating",
                data: JSON.stringify(rate),
                cache: false,
                beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/json;charset=UTF-8");
                    }
                },
                success: function (resp) {
                    if (resp['message'] === "OK") {
                       console.log("successssssssssss");
                    } else {
                        console.log('error');
                        //$('#widget-description').append("1there was an error in your update");
                    }
                },
                
                error: function () {
                    console.log('error');
                }
            });

        },
        starOff : 'star-off.png',
        starOn  : 'star-on.png',
        path    : '/images',
           //cancel  : true
        });
    
    //
    // Add edit event handler
    //
    $('#edit-widget-information').click(function () {
        var current_desc = $('#widget-description').text();
        console.log(current_desc);
        $('#widget-description').replaceWith('<div id="widget-description"><div class="controls"><textarea class="input-xlarge" id="textarea" rows="3">' + current_desc + '</textarea></div><button type="submit" class="btn btn-primary">Save changes</button><button id="edit-description-cancel" class="btn">Cancel</button></div>');
        $('#widget-description button[type="submit"]').click(function () {
            var newText = $('#widget-description textarea').val();
            $.ajax({
                type: 'PUT',
                dataType: 'json',
                url: "/api/widget/" + widget_id + "/description",
                data: newText,
                cache: false,
                success: function (resp) {
                    //console.log(resp);
                    if (resp['message'] === "OK update description done") {
                        $('#widget-description').replaceWith('<dl id="widget-description">' + newText + '</dl>');
                    } else {
                        $('#widget-description').append("1there was an error in your update");
                    }
                },
                error: function () {
                    $('#widget-description').append("2there was an error in your update");
                }
            });
        });
        $('#widget-description #edit-description-cancel').click(function () {
            $('#widget-description').replaceWith('<dl id="widget-description">' + current_desc + '</dl>');
        });
    });
    
    //
    // Add tag edit event handler
    //
    $('#add-tag').click(function () {
        $('#widget-tags').append('<form id="add-tag-form" action="javascript:function(){return false;}" class="well form-inline"><input type="text" class="input-small">' + '<button type="submit" class="btn">add</button>' + '</form>');
        // console.log('form added');
        // var new_position = $('#add-tag-form').offset();
        // console.log(new_position);
       //window.scrollTo(new_position.left,new_position.top);
        $('#add-tag-form input').focus();
        $('#add-tag-form').submit(function () {
            var newTag = $('#add-tag-form input[type="text"]').val();
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: "/api/widget/" + widget_id +"/tag",
                data: newTag,
                cache: false,
                success: function (resp) {
                    //console.log(resp);
                    if (resp['message']=="OK") {
                        var tag_db_id = resp['id'];
                        $('#add-tag-form').remove();
                        $('#widget-tags').append('<a class="btn btn-info" href="/tag/' + tag_db_id + '"><i class="icon-tag icon-white"></i>' + newTag + '</a>');
                    } else {
                        $('#widget-tags').append("1 - there was an error in your addition");
                    }
                },
                error: function () {
                    $('#widget-tags').append("2 - there was an error in your addition");
                }
            });
        });
        //console.log("done");
    });


    //
    // Add affordance event handler
    //
    $('#add-affordance').click(function () {

        //store in an array all current affordances of this widget profile
        //get this from dom elements
        var widget_affordances = [];
        $('#widget-useful-for a').each(function(){
            widget_affordances.push($(this).text());
        });
        console.log(widget_affordances);
        //show all available affordances
        $.ajax({
                type: 'GET',
                dataType: 'json',
                url: "/api/activity?operation=all",
                cache: false,
                beforeSend: function(){
                    //implement a loading icon here
                },
                success: function (resp) {
                    console.log(resp); 
                    $('#widget-useful-for').append('<form id="select-affordances-form" action="javascript:function(){return false;}" class="well form-inline"></form>');
        
                    for (var index in resp){
                        var affordance = resp[index];
                        if ($.inArray(affordance.activitytext,widget_affordances)<0){
                            //if not in the array show it
                            var affordance_element = document.createElement("a");
                            var affordance_icon = document.createElement("i");
                            var affordance_id = affordance.id;
                            $(affordance_icon).attr("class", "icon-ok-circle icon-white available-affordance");
                            $(affordance_element).attr("class", "btn btn-warning disabled");
                            $(affordance_element).text(affordance.activitytext);
                            $(affordance_element).attr("href", "/activity/" + affordance_id);
                            $(affordance_element).attr("id", "add-activity-" + affordance_id);
                            $(affordance_element).prepend(affordance_icon);


                            var insert_element = document.createElement("a");
                            $(insert_element).attr("class","btn-mini btn-warning insert-activity-element");
                            $(insert_element).attr("href","#add-activity-"+affordance_id);
                            $(insert_element).attr("id","add-activity-insert-icon-"+affordance_id);
                            var insert_element_icon = document.createElement("i");
                            $(insert_element_icon).attr("class", "icon-plus icon-white");                           
                            $(insert_element).prepend(insert_element_icon);
                            
                            $('#select-affordances-form').append(affordance_element);
                            $('#select-affordances-form').append(insert_element).append(" ");
           
                        }                  
                    }  

                    //add click handlers in the affordance list elements
                    $('a.insert-activity-element').click(function(e){
                        //e.preventDefault();
                        console.log("aff clicked");
                        var insert_element = $(this);
                        var affordance_id = $(this).attr("id").split('add-activity-insert-icon-')[1];
                                    
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: "/api/widget/" + widget_id +"/activity",
                            data: affordance_id,
                            cache: false,
                            success: function (resp) {
                                //console.log(resp);
                                if (resp['message']=="OK") {
                                    var affordance_element = $('#add-activity-'+affordance_id);
                                    $(insert_element).remove();
                                    $(affordance_element).remove();


                                    var affordance = document.createElement("a");
                                    var affordance_icon = document.createElement("i");
                                    $(affordance_icon).attr("class", "icon-ok-circle icon-white");
                                    $(affordance).attr("class", "btn btn-warning");
                                    $(affordance).text($(affordance_element).text());
                                    $(affordance).attr("href", "/activity/" + affordance_id);
                                    $(affordance).prepend(affordance_icon);
                                    $("#widget-useful-for").prepend(affordance).prepend(" ");    
                                } else {
                                    $('#widget-tags').append("1 - there was an error in your addition");
                                    console.log('activity addition error:');
                                    console.log(resp);
                                }
                            },
                            error: function () {
                                $('#widget-tags').append("2 - there was an error in your addition");
                                console.log('activity addition - ajax error');
                            }
                        });
                    });
                },
                error: function () {
                    $('#widget-useful-for').append("There was an error while retrieving available affordances");
                }
            });

    });



    
    //
    // add write a review handler
    //
    $('#write-a-review-anchor').click(function () {
        $(this).replaceWith('<div style="width:100%;" id="write-a-review">' + 
        		'<label>Your review:</label>' + 
        		'<div class="controls">' + 
        		'<textarea class="input-xlarge" id="review-textarea" rows="3"></textarea>' + 
        		'</div><button type="submit" class="btn btn-primary">Submit</button>' + 
        		'<button id="edit-description-cancel" class="btn">Cancel</button>' + 
        		'</div>');

        $('#write-a-review button[type="submit"]').click(function () {
            //get info from hidden fields
            var reviewText = $('#review-textarea').val();
            var userid = $('#logged-in-user-id').val();
            var gravatarImg = $('#logged-in-user-gravatar-img').val();
            var username = $('#logged-in-user-name').val();
            
            var review  = {};
            review.comment = $('#review-textarea').val();
            review.userId = $('#logged-in-user-id').text();
            
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url:"/api/widget/"+widget_id+"/comment",
                data: JSON.stringify(review),
                cache: false,
                beforeSend: function(x) {
                    if (x && x.overrideMimeType) {
                        x.overrideMimeType("application/json;charset=UTF-8");
                    }
                },
                success: function (resp) {
                    if (resp['message'] === "OK") {
                        $('#user-reviews').append('<div style="">' + '<div class="row-fluid">' + '  <div class="span1">' + '        <img src="http://www.gravatar.com/avatar/' + gravatarImg + '?s=35&amp;d=identicon">' + '        <h5><a href="profile.jsp?id=' + userid + '">' + username + '</a></h5>' + '  </div>' + ' <div class="span11">' + '       <div class="review-item-info-wrapper"></div>' + '       <p class="review-content-text">' + reviewText + '</p>' + '      <h6>just now</h6>' + '  </div>' + '</div>' + '</div>');
                        $('#write-a-review').remove();
                    } else {
                        console.log('error');
                        //$('#widget-description').append("1there was an error in your update");
                    }
                },
                
                error: function () {
                    console.log('error');
                }
            });
        });
        $('#widget-description #edit-description-cancel').click(function () {
            $('#widget-description').replaceWith('<dl id="widget-description">' + current_desc + '</dl>');
        });
    });
    var widgetUri;
    
    //
    // Load the widget profile
    //
    $.getJSON('/api/widget/' + widget_id, function (data) {
        widgetUri = data.widgetProfile.uri;
        
        //
        // Show metadata
        //
        $("#widget_name").text(
        data.widgetProfile.name);
        if (data.uploadedBy) {
            $("#upload-info").text("Uploaded by " + data.uploadedBy.username);
        }
        
        //
        // Show w3c or opensocial logo
        //
        if (data.widgetProfile.type === "W3C Widget") {
            $('#widget-type-logo-holder img').attr("src","/images/W3C_Logo.png");
            $('#widget-type-logo-holder p').html("This widget complies with the <a href=\"http://www.w3.org/TR/widgets/\">W3C widget</a> standard");
        } else {
            $('#widget-type-logo-holder img').attr("src","/images/OpenSocial-logo.png");
            $('#widget-type-logo-holder p').html("This widget complies with the <a href=\"http://docs.opensocial.org/display/OS/Home\">OpenSocial standard</a>");
        
        }


        //
        // Show average rating
        //
        var average_rating;
        if (data.averageRating) {
            average_rating = data.averageRating;
        }else {
            average_rating = 0;
        }
        $('#widget-rating').raty({
                readOnly    : true,
                start       : average_rating,
                starOff : 'star-off.png',
                starOn  : 'star-on.png',
                path    : '/images',
            });


        //
        // Show description
        //
        if (data.widgetProfile.description) {
            $('#widget-description').text(data.widgetProfile.description);
        }
        
        //
        // Show preview instance if available
        //
        if (data.renderInfo) {
            $('#widget-preview').html(data.renderInfo);
            $('#embedModal .modal-body pre').text((data.renderInfo));
            $('#modal-body-lti').html("<p>For Basic LTI, copy the following information to your Basic LTI consumer:</p><p>URL: http://widgets.open.ac.uk:8080/lti/" + data.widgetProfile.id + "</p><p>Consumer Key: TEST</p><p>Consumer Secret: TEST</p>");
            //show embed code as a modal
            $('#embedModal').modal({
                show: false
            });
            $('#embedModal-link').click(function () {
                $('#embedModal').modal('toggle');
            });
        }
        
        //
        // Show tags
        //
        if (data.widgetProfile.tags) {
            var tags = data.widgetProfile.tags;
            for (var i = 0; i < tags.length; i++) {
                var tag = document.createElement("a");
                var tag_icon = document.createElement("i");
                $(tag_icon).attr("class", "icon-tag icon-white");
                $(tag).attr("class", "btn btn-info");
                $(tag).text(tags[i].tagtext);
                $(tag).attr("href", "/tag/" + tags[i].id);
                $(tag).prepend(tag_icon);
                $("#widget-tags").append(tag).append(" ");
            }
        }
        
        //
        // Show activities
        //
        if (data.widgetProfile.activities) {
            var affordances = data.widgetProfile.activities;
            for (var i = 0; i < affordances.length; i++) {
                var affordance = document.createElement("a");
                var affordance_icon = document.createElement("i");
                var affordance_id = affordances[i].id;
                $(affordance_icon).attr("class", "icon-ok-circle icon-white");
                $(affordance).attr("class", "btn btn-warning");
                $(affordance).text(affordances[i].activitytext);
                $(affordance).attr("href", "/activity/" + affordance_id);
                $(affordance).prepend(affordance_icon);
                $("#widget-useful-for").append(affordance).append(" ");
            }
        }


        //
        // Load similar widget profiles
        //
        $.getJSON('/api/widget/' + widget_id + "/similar", function (data) {
            for (var i = 0; i < data.length; i++) {
                $('#related-widgets').append(''+renderWidget(data[i].id, data[i].icon, data[i].name, data[i].description));	        
            }
        });
        
        //
        // Load reviews
        //
        $.getJSON('/api/widget/' + widget_id +'/comments', function (reviews) {
            for (var i = 0; i < reviews.length; i++) {
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
                $(img).attr("src", "http://www.gravatar.com/avatar/205e460b479e2e5b48aec05710c08d50?s=35&d=identicon");
                $(pic).append(img);
                $(pic).append("<h5><a href='#'>" + reviews[i].user + "</a></h5>");
                $(wrapper).append(pic);
                //
                // Date, stars and comment
                //
                var item = document.createElement("div");
                $(item).attr("class", "span11");
                var iteminfo = document.createElement("div");
                $(iteminfo).attr("class", "review-item-info-wrapper");
                for (var stars = 0; stars < reviews[i].rating; stars++) {
                    $(iteminfo).append("<i class='icon-star'></i>");
                }
                $(item).append(iteminfo);
                var itemtext = document.createElement("p");
                $(itemtext).attr("class", "review-content-text");
                $(itemtext).text(reviews[i].text);
                $(item).append(itemtext);
                $(item).append("<h6>" + reviews[i].time + "</h6>");
                $(wrapper).append(item);
                $(li).append(wrapper);
                $(li).appendTo("#user-reviews").fadeIn("slow");
            }
        });
    });
});