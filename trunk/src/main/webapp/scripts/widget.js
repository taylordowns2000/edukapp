$(document).ready(

function () {
    "use strict";
    
    //
    // Get the current widget id
    //
    var widget_id = $('#widgetid').val();
    
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
                url: "updateWidget?id=" + widget_id + "&operation=description&newText=" + newText,
                cache: false,
                success: function (resp) {
                    console.log("s" + resp + "s");
                    if (resp === "update done") {
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
        $('#add-tag-form').submit(function () {
            var newTag = $('#add-tag-form input[type="text"]').val();
            $.ajax({
                url: "ajaxHandlers/addTag.jsp?id=" + widget_id + "&newTag=" + newTag,
                cache: false,
                success: function (resp) {
                    console.log("s" + resp + "s");
                    if (resp.substring(0, 13) === "addition done") {
                        var tag_db_id = resp.substring(13);
                        console.log(resp.substring(13));
                        $('#add-tag-form').remove();
                        $('#widget-tags').append('<a class="btn btn-info" href="/tags/' + tag_db_id + '"><i class="icon-tag icon-white"></i>' + newTag + '</a>');
                    } else {
                        $('#widget-tags').append("1 - there was an error in your addition");
                    }
                },
                error: function () {
                    $('#widget-tags').append("2 - there was an error in your addition");
                }
            });
        });
        console.log("done");
    });
    
    //
    // add write a review handler
    //
    $('#write-a-review-anchor').click(function () {
        $(this).replaceWith('<div style="width:100%;" id="write-a-review">' + '<label>Your review:</label>' + '<div class="controls">' + '<textarea class="input-xlarge" id="review-textarea" rows="3"></textarea>' + '</div><button type="submit" class="btn btn-primary">Submit</button>' + '<button id="edit-description-cancel" class="btn">Cancel</button>' + '</div>');
        $('#write-a-review button[type="submit"]').click(function () {
            //get info from hidden fields
            var reviewText = $('#review-textarea').val();
            var userid = $('#logged-in-user-id').val();
            var gravatarImg = $('#logged-in-user-gravatar-img').val();
            var username = $('#logged-in-user-name').val();
            $.ajax({
                url: "ajaxHandlers/addReview.jsp?id=" + widget_id + "&userid=" + userid + "&reviewText=" + reviewText,
                cache: false,
                success: function (resp) {
                    console.log("$" + resp.substring(0, 11) + "$");
                    if (resp.substring(0, 11) === "update done") {
                        $('#user-reviews').append('<div style="">' + '<div class="row-fluid">' + '	<div class="span1">' + '		<img src="http://www.gravatar.com/avatar/' + gravatarImg + '?s=35&amp;d=identicon">' + '		<h5><a href="profile.jsp?id=' + userid + '">' + username + '</a></h5>' + '	</div>' + '	<div class="span11">' + '		<div class="review-item-info-wrapper"></div>' + '		<p class="review-content-text">' + reviewText + '</p>' + '		<h6>just now</h6>' + '	</div>' + '</div>' + '</div>');
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
    $.getJSON('/api/widget?id=' + widget_id, function (data) {
        console.log(data);
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
        // Show description
        //
        if (data.widgetProfile.description) {
            $('#widget-description').text(data.widgetProfile.description.description);
        }
        
        //
        // Show preview instance if available
        //
        if (data.renderInfo) {
            $('#widget-preview').html(data.renderInfo);
            $('#embedModal .modal-body pre').text((data.renderInfo));
            $('#modal-body-lti').html("<p>For Basic LTI, copy the following information to your Basic LTI consumer:</p><p>URL: http://localhost:8080/lti/" + data.widgetProfile.id + "</p><p>Consumer Key: TEST</p><p>Consumer Secret: TEST</p>");
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
                $(tag).attr("href", "/tags/" + tags[i].id);
                $(tag).prepend(tag_icon);
                $("#widget-tags").append(tag).append(" ");
            }
        }
        
        //
        // Load similar widget profiles
        //
        $.getJSON('/similar?uri=' + widgetUri, function (similar) {
            for (var i = 0; i < similar.length; i++) {
                $("<li class='span2'>" + '<div class="thumbnail">' + '	<a href="widget.jsp?id=' + similar[i].id + '">' + similar[i].name + '</a>' + '	<div class="caption">' + '		<h5>' + similar[i].description.description + '</h5>' + '		<p></p>' + '		<p><a href="widget.jsp?id=' + similar[i].id + '" class="btn btn-primary">Read more >></a></p>' + '	</div>' + '</div>' + '</li>').hide().appendTo("#related-widgets").fadeIn("slow");
            }
        });
        
        //
        // Load reviews
        //
        $.getJSON('/api/review?uri=' + widgetUri, function (reviews) {
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