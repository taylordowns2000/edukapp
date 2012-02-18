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
<script src="scripts/widget.js"></script>

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
<input id="widgetid" type="hidden" value="<%=request.getParameter("id")%>" />
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

					<form type="POST" action="login" class="navbar-text pull-right form-inline">
						<input autocomplete="off" id="name" name="username" type="text" class="input-small" placeholder="Email">
						<input autocomplete="off" id="pass"	name="password" type="password" class="input-small" placeholder="Password">
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
						<li><a href="#"><i class="icon-download"></i>Download</a>
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
						<dl id="widget-description">Lorem Ipsum is simply dummy text of the printing and
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
						<a id="edit-widget-information" href="#">edit</a>
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
					<ul class="unstyled">
						<li><i class="icon-home"></i>Home</li>
						<li><i class="icon-upload"></i>Submit a widget</li>
						<li>About</li>
						<li>Contact</li>
						<li>Blog</li>
					</ul>
				</div>
				<div id="footer-types" class="span3">
					<h3><i class="icon-ok-circle"></i>Activities</h3>
					<ul class="unstyled">
						<li class="btn-small">alpha</li>
						<li class="btn-small">beta</li>
						<li class="btn-small">gamma</li>
						<li class="btn-small">delta</li>
						<li class="btn-small">epsilon</li>
					</ul>
					<h4 class="see-more">
						<a href="#">see more..</a>
					</h4>
				</div>
				<div id="footer-tags" class="span3">
				<h3><i class="icon-tags"></i>Tags</h3>
				<ul class="unstyled">
						<li class="btn-small">tag-alpha</li>
						<li class="btn-small">beta-tag</li>
						<li class="btn-small">gamma-tag</li>
						<li class="btn-small">tag-delta</li>
						<li class="btn-small">tag_epsilon</li>
					</ul>
				</div>
			</div>
		</footer>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<hr>
	</div>
</body>
</html>