<%@page import="uk.ac.edukapp.renderer.WidgetRenderer"%>
<%@page import="uk.ac.edukapp.model.Widgetprofile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,uk.ac.edukapp.util.*,uk.ac.edukapp.model.Useraccount,java.util.*,javax.persistence.*,javax.persistence.EntityManager,javax.persistence.EntityManagerFactory,org.apache.commons.logging.Log,org.apache.commons.logging.LogFactory,uk.ac.edukapp.renderer.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="scripts/featured.js"></script>
</head>
<body>

	<%@ include file='components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="about.jsp"><i class="icon-question-sign"></i>About
								EDUKApp</a></li>
						<li><a href="upload.jsp"><i class="icon-upload"></i>Submit
								a widget</a></li>

					</ul>
				</div>
			</div>
			<div class="span9">
				<div class="hero-unit" style="position: relative;">
					<h1>EDUKApp</h1>
					<p><u>Ed</u>ucational <u>UK</u>-wide <u>app</u> store</p>
					<div class="span6">
						<span>
						
						Edukapp is a uk-wide, cross-university widget and app store. 
						In other words, edukapp is a repository and community site, 
						that focuses on collecting and promoting widgets and apps for learning and teaching. 
						The store offers discovery services and one-click deployment of widgets into 
						(personal) learning environments, while promoting their capabilities and features. 
						It will offer to end users a sophisticated recommendation engine to advise on widget 
						use according to user needs and interaction history. The widget store supports educators 
						in selecting building blocks for personal learning environments. It supports developers 
						with social requirements engineering and analytics.
						
						 </span>
						<p style="margin-top: 10px;">
							<a class="btn btn-primary btn-large" href="about.jsp">Learn more »</a>
						</p>
					</div>

					<div class="span4" id="intro-screencast">

						<iframe
							src="http://player.vimeo.com/video/36818561?title=0&amp;byline=0&amp;portrait=0&amp;color=ffffff"
							width="401" height="228" frameborder="0" webkitAllowFullScreen
							mozallowfullscreen allowFullScreen></iframe>
						<div class="clear" style="clear: both;"></div>
					</div>
					<div class="clear" style="clear: both;"></div>

				</div>
				<div id="main">	
				</div>
				<%@ include file="components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>