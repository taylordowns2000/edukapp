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
					<p>Education UK wide App store</p>
					<div class="span6">
						<span> Lorem ipsum dolor sit amet, consectetur adipiscing
							elit. Nunc ac lacus dui, sit amet placerat enim. Integer euismod
							pulvinar velit id porta. Fusce varius nisl sit amet velit
							molestie nec commodo sem malesuada. Morbi orci mi, consequat ac
							bibendum non, porttitor eget nisl. Donec sollicitudin metus sit
							amet nibh sagittis dictum. Praesent porttitor bibendum quam a
							auctor. Proin blandit dictum faucibus. Sed purus urna, cursus
							quis blandit eget, consequat vitae nisl. Mauris id molestie
							nulla. In feugiat, odio in porta volutpat, ante magna ultricies
							diam, at iaculis est mauris congue mauris. Mauris eget turpis nec
							turpis tempor congue ac eget ligula. Phase </span>
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
					<div id="myCarousel" class="carousel slide"
						style="background: gray; color: white;">
						<!-- Carousel items -->
						<div class="carousel-inner"></div>
						<!-- Carousel nav -->
						<a class="carousel-control left" href="#myCarousel"
							data-slide="prev">&lsaquo;</a> <a class="carousel-control right"
							href="#myCarousel" data-slide="next">&rsaquo;</a>
					</div>

				</div>
				<%@ include file="components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>