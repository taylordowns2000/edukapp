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
<<<<<<< .mine
<link rel="stylesheet" href="css/layout.css00000000000" type="text/css" />
=======
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
>>>>>>> .r18
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

				<h1>Widget title</h1>
				<p class="upload-info">
					uploaded by <a href="#">username</a> 2 weeks ago
				</p>
				<div id="first-row-boxes">
					<div id="widget-left">
						<div id="widget-preview">screenshot or preview instance</div>
						<div id="widget-stats-bar">
							<div id="widget-usage">usage</div>
							<div id="widget-rating">rating</div>
						</div>
					</div>
					<div id="widget-right">
						<h2>Description:</h2>
						<div id="widget-description-text"></div>
						<h2>Tagged as:</h2>
						<div id="widget-tags"></div>
						<h2>Useful for:</h2>
						<div id="widget-useful-for"></div>
					</div>
				</div>
				<!-- end of first-row-boxes -->

				<div id="second-row-boxes">
					<div id="user-reviews">
						<ul id="user-reviews-list">
							<li>
								<div class="review-item-wrapper">
									<div class="review-item-pic"></div>
									<div class="review-item-content">
										<div class="review-item-info-wrapper">
											<p>
												<a href="#">username</a> 11 Jan,2012
											</p>
											<p>stars</p>
										</div>
										<div class="review-content-text">texztxxx lorem ipsum,
											the lazy dox jumped the quick brown fox</div>
										<div class="clear"></div>
									</div>
								</div></li>
							<li>
								<div class="review-item-wrapper">
									<div class="review-item-pic"></div>
									<div class="review-item-content">
										<div class="review-item-info-wrapper">
											<p>
												<a href="#">username</a> 11 Jan,2012
											</p>
											<p>stars</p>
										</div>
										<div class="review-content-text">texztxxx lorem ipsum,
											the lazy dox jumped the quick brown fox</div>
										<div class="clear"></div>
									</div>
								</div></li>
						</ul>
					</div>
					<div id="related-widgets">
						<div class="related-widget-wrapper">widget a</div>
						<div class="related-widget-wrapper">widget b</div>
						<div class="related-widget-wrapper">widget c</div>
						<div class="related-widget-wrapper">widget d</div>
						<div class="related-widget-wrapper">widget e</div>
						<div class="related-widget-wrapper">widget f</div>
						<div class="related-widget-wrapper">widget g</div>
						<div class="related-widget-wrapper">widget h</div>
					</div>
					<div class="clear"></div>
				</div>
				<!-- end of second-row-boxes -->
				<div class="clear"></div>

			</div>
		</div>





		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>