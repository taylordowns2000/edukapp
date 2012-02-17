<%@page import="uk.ac.edukapp.model.Widgetprofile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="uk.ac.edukapp.util.JspUtils,
	uk.ac.edukapp.model.Useraccount,
	javax.persistence.EntityManagerFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.css" type="text/css" />
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
</style>
<link rel="stylesheet" href="css/bootstrap-responsive.css"
	type="text/css" />
<title>EDUKApp</title>
<script>
$(document).ready(function(){

  var widgetUri;
  //
  // Load the widget profile
  //
  $.getJSON('/widget?id=<%=request.getParameter("id")%>',function(data) {
	   console.log(data);
	   
											widgetUri = data.widgetProfile.uri;

											//
											// Show metadata
											//
											$("#widget_name").text(data.widgetProfile.name);
											if (data.uploadedBy) {
												$("#upload-info").text("Uploaded by "+ data.uploadedBy.username);
											}

											//
											// Show tags
											//
											if (data.widgetProfile.tags) {
												var tags = data.widgetProfile.tags;
												for ( var i = 0; i < tags.length; i++) {
													var tag = document.createElement("a");
													var tag_icon = document.createElement("i");
													$(tag_icon).attr("class","icon-tag");
													$(tag).attr("class","btn");
													$(tag).text(tags[i].tagtext);
													$(tag).attr("href","/tags/"	+ tags[i].id);
													$(tag).prepend(tag_icon);
													$("#widget-tags").append(tag);
												}
												
												
											}

											//
											// Load similar widget profiles
											//
											$.getJSON('/similar?uri='+ widgetUri,function(similar) {
												for ( var i = 0; i < similar.length; i++) {
													$("<div>"+ similar[i].name+ "</div>").hide()
																			.appendTo("#related-widgets")
																			.fadeIn("slow");
												};
											});

											//
											// Load reviews
											//
											$.getJSON('/review?uri='+ widgetUri,function(reviews) {
												for ( var i = 0; i < reviews.length; i++) {
													var li = document.createElement("li");
													$(li).hide();
													var wrapper = document.createElement("div");
													$(wrapper).attr("class","review-item-wrapper");

																	var pic = document
																			.createElement("div");
																	$(pic)
																			.attr(
																					"class",
																					"review-item-pic");
																	var img = document
																			.createElement("img");
																	$(img)
																			.attr(
																					"src",
																					"http://www.gravatar.com/avatar/205e460b479e2e5b48aec05710c08d50?s=35&d=identicon");
																	$(pic)
																			.append(
																					img);
																	$(wrapper)
																			.append(
																					pic);

																	var item = document
																			.createElement("div");
																	$(item)
																			.attr(
																					"class",
																					"review-item-content");

																	var iteminfo = document
																			.createElement("div");
																	$(iteminfo)
																			.attr(
																					"class",
																					"review-item-info-wrapper");
																	$(iteminfo)
																			.append(
																					"<p><a href='#'>"
																							+ reviews[i].user
																							+ "</a> "
																							+ reviews[i].time
																							+ "</p>");
																	$(iteminfo)
																			.append(
																					"<p> "
																							+ reviews[i].rating
																							+ " stars</p>");
																	$(item)
																			.append(
																					iteminfo);

																	var itemtext = document
																			.createElement("div");
																	$(itemtext)
																			.attr(
																					"class",
																					"review-content-text");
																	$(itemtext)
																			.text(
																					reviews[i].text);
																	$(item)
																			.append(
																					itemtext);

																	$(wrapper)
																			.append(
																					item);

																	var clear = document
																			.createElement("div");
																	$("clear")
																			.attr(
																					"class",
																					"clear");
																	$(wrapper)
																			.append(
																					clear);

																	$(li)
																			.append(
																					wrapper);
																	$(li)
																			.appendTo(
																					"#user-reviews ul")
																			.fadeIn(
																					"slow");
																}
																;
															});

										});

					});
</script>

</head>

<%
  //--------------------------------
  // deduce whether user is logged in 
  //--------------------------------
  EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
      .getAttribute("emf");
  boolean isAuthenticated = JspUtils.isAuthenticated(session, request, emf);
%>


<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand" href="#">Edukapp</a>
				<div class="nav-collapse">
					<ul class="nav">
						<li class="active"><a href="#">Home</a></li>
						<li><a href="#about">About</a></li>
						<li><a href="#contact">Contact</a></li>
					</ul>
					<%
					  if (isAuthenticated) {
					    
					    Useraccount loggedinuser = (Useraccount)session.getAttribute("logged-in-user");
					    
					%>
					<p class="navbar-text pull-right">
						Logged in as <a href="profile.jsp?userid=<% out.print(loggedinuser.getId());%>"><% out.print(loggedinuser.getUsername());%></a><a style="margin-left:5px;" href="/logout">logout</a>
					</p>
					<%
					  } else {
					%>

					<form class="navbar-text pull-right form-inline">
						<input type="text" class="input-small" placeholder="Email">
						<input type="password" class="input-small" placeholder="Password">
						<button type="submit" class="btn">Go</button>
					</form>

					<%
					  }
					%>
				</div>
			</div>
		</div>
	</div>


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="#"><i class="icon-share-alt"></i>Embed</a>
						</li>
						<li><a href="#"><i class="icon-share-alt"></i>Download</a>
						</li>
						<li><a href="#"><i class="icon-tag"></i>Tag</a>
						</li>
						<li><a href="#"><i class="icon-comment"></i>Review</a>
						</li>
					</ul>
				</div>
				<!--/.well -->
			</div>
			<!--/span-->
			<div class="span9">
				<div class="row-fluid">
					<h1 id="widget_name">Widget name</h1>
					<p id="upload-info" class="upload-info">no upload info
						available</p>
				</div>
				<div class="row-fluid">
					<div class="span6">

						<div id="widget-preview">screenshot or preview instance</div>
						<div id="widget-stats-bar">
							<div id="widget-usage">usage</div>
							<div id="widget-rating">rating</div>
							<div class="clear"></div>
						</div>


					</div>
					<!--/span-->
					<div class="span6">
						<h4>Descripiton:</h4>
						<dl>Lorem Ipsum is simply dummy text of the printing and
							typesetting industry. Lorem Ipsum has been the industry's
							standard dummy text ever since the 1500s, when an unknown printer
							took a galley of type and scrambled it to make a type specimen
							book. It has survived not only five centuries, but also the leap
							into electronic typesetting, remaining essentially unchanged. It
							was popularised in the 1960s with the release of Letraset sheets
							containing Lorem Ipsum passages, and more recently with desktop
							publishing software like Aldus PageMaker including versions of
							Lorem Ipsum.
						</dl>
						<h4>Tagged as:</h4>
						<div id="widget-tags">
							
						</div>
						<h4>Useful for:</h4>
						<div id="widget-useful-for">
							<a class="btn"><i class="icon-ok-circle"></i>use1</a> <a
								class="btn"><i class="icon-ok-circle"></i>use2</a> <a
								class="btn"><i class="icon-ok-circle"></i>use3</a>
						</div>
					</div>
					<!--/span-->
				</div>
				<div class="row-fluid">
					<div class="span6">reviews</div>

					<div class="span6">similar</div>
				</div>
				<!--/row-->
			</div>
			<!--/span-->
		</div>
		<!--/row-->

		<hr>

		<footer>
			<div class="row-fluid">
				<div id="footer-logos" class="span3">
					<div id="ou-logo">
						<img width="45" src="images/ou-logo.png"
							alt="open university logo" />
					</div>
					<div id="jisc-logo">
						<img width="45" src="images/jisc-logo.png" alt="jisc logo" />
					</div>
				</div>
				<div id="footer-site-links" class="span3">
					<h3>Site links</h3>
					<ul>
						<li>Home</li>
						<li>Upload</li>
						<li>About</li>
						<li>Contact</li>
						<li>Blog</li>
					</ul>
				</div>
				<div id="footer-types" class="span3">
					<h3>Types</h3>
					<ul>
						<li>alpha</li>
						<li>beta</li>
						<li>gamma</li>
						<li>delta</li>
						<li>epsilon</li>
					</ul>
					<h4 class="see-more">
						<a href="#">see more..</a>
					</h4>
				</div>
				<div id="footer-tags" class="span3">tttt</div>
			</div>
		</footer>

	</div>





</body>
</html>