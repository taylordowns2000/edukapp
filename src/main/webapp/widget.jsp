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
<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/widget.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<link rel="stylesheet" href="css/bootstrap.css" type="text/css" />
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
													$(tag).attr("class","small blue tag");
													$(tag).text(tags[i].tagtext);
													$(tag).attr("href","/tags/"	+ tags[i].id);
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
<body>

	<%
	  //--------------------------------
	  // deduce whether user is logged in
	  //--------------------------------
	  EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
	      .getAttribute("emf");
	  boolean isAuthenticated = JspUtils.isAuthenticated(session, request, emf);
	  if (isAuthenticated) {
	%>
	<%@ include file="static/logged-in-as-box.jsp"%>
	<%
	  } else {
	%>
	<%@ include file="static/login-box.jsp"%>
	<%
	  }
	%>

	<%@ include file="static/header.html"%>


	<div id="page-wrapper">

		<div id="main-content-wrapper">
			<div id="sidebar">
				<ul>
					<li id="sidebar-embed-list-item"><div class="img-holder">&nbsp;</div>
						<span>Embed</span>
						<div class="clear"></div></li>
					<li id="sidebar-download-list-item"><div class="img-holder">&nbsp;</div>
						<span>Download</span>
						<div class="clear"></div></li>
					<li id="sidebar-tag-list-item"><div class="img-holder">&nbsp;</div>
						<span>Tag</span>
						<div class="clear"></div></li>
					<li id="sidebar-review-list-item"><div class="img-holder">&nbsp;</div>
						<span>Review</span>
						<div class="clear"></div></li>
				</ul>
			</div>

			<div id="main">

				<h1 id="widget_name"></h1>
				<p id="upload-info" class="upload-info">no upload info available</p>

				<div class="clear"></div>

				<div id="first-row-boxes">
					<div id="widget-left">
						<div id="widget-preview">screenshot or preview instance</div>
						<div id="widget-stats-bar">
							<div id="widget-usage">usage</div>
							<div id="widget-rating">rating</div>
							<div class="clear"></div>
						</div>
					</div>
					<div id="widget-right">
						<h2>Description:</h2>
						<div id="widget-description-text">Lorem Ipsum is simply
							dummy text of the printing and typesetting industry. Lorem Ipsum
							has been the industry's standard dummy text ever since the 1500s,
							when an unknown printer took a galley of type and scrambled it to
							make a type specimen book. It has survived not only five
							centuries, but also the leap into electronic typesetting,
							remaining essentially unchanged. It was popularised in the 1960s
							with the release of Letraset sheets containing Lorem Ipsum
							passages, and more recently with desktop publishing software like
							Aldus PageMaker including versions of Lorem Ipsum.</div>

						<h2>Tagged as:</h2>
						<div id="widget-tags"></div>

						<h2>Useful for:</h2>
						<div id="widget-useful-for">
							<a href="#" class="btn">collaboration</a> <a href="#"
								class="btn">learning</a> <a href="#"
								class="btn">fun</a>
						</div>
					</div>
					<div class="clear"></div>
				</div>
				<!-- end of first-row-boxes -->

				<div class="clear"></div>

				<div id="second-row-boxes">
					<div id="user-reviews">
						<h2>User reviews</h2>
						<ul id="user-reviews-list">
						</ul>
					</div>
					<div id="related-widgets">
						<h2>Related widgets</h2>
					</div>
					<div class="clear"></div>
				</div>
				<!-- end of second-row-boxes -->
				<div class="clear"></div>

			</div>
			<div class="clear"></div>
		</div>

	</div>
	<!-- end of page-wrapper -->



	<%@ include file="static/footer.html"%>


</body>
</html>