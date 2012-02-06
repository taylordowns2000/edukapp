<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.Useraccount,java.util.*,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="css/layout.css00000000000" type="text/css" />
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/main.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<title>EDUKApp</title>
</head>
<body>
	<div id="page-wrapper">
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

		<div id="main-content-wrapper">
			<%@ include file="static/sidebar.jsp"%>

			<div id="main">
				<div id="first-row-boxes">
				<div id="intro-text">
				<h2>Edukapp</h2>
				<p>EDUkation UK wide App store</p>
				<div id="intro-text-container">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ac lacus dui, sit amet placerat enim. Integer euismod pulvinar velit id porta. Fusce varius nisl sit amet velit molestie nec commodo sem malesuada. Morbi orci mi, consequat ac bibendum non, porttitor eget nisl. Donec sollicitudin metus sit amet nibh sagittis dictum. Praesent porttitor bibendum quam a auctor. Proin blandit dictum faucibus. Sed purus urna, cursus quis blandit eget, consequat vitae nisl. Mauris id molestie nulla. In feugiat, odio in porta volutpat, ante magna ultricies diam, at iaculis est mauris congue mauris. Mauris eget turpis nec turpis tempor congue ac eget ligula. Phasellus ultricies interdum enim, tempor luctus augue posuere eget.
				</div>
				</div>
				<div id="intro-screencast">
				screencast
				</div>
				</div>
				<div id="widget-slider">
					<div id="widget-slider-left-btn"></div>
					<div id="widget-slider-container">
						<div class="widget-box">widget a</div>
						<div class="widget-box">widget b</div>
						<div class="widget-box">widget c</div>
						<div class="widget-box">widget d</div>
						<div class="widget-box">widget e</div>
						<div class="widget-box">widget g</div>
						<div class="widget-box">widget h</div>
						<div class="clear"></div>
						<div id="widget-slider-control-buttons"></div>
					</div>
					<div id="widget-slider-right-btn"></div>
				</div>
			</div>
		</div>

		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>