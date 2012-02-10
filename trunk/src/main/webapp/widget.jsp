<%@page import="uk.ac.edukapp.model.Widgetprofile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.*,
	java.util.*,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/widget.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<title>EDUKApp</title>
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

		<%
		  String widgetId = request.getParameter("id");

		  if (widgetId != null) {

		    int widget_id = Integer.parseInt(widgetId);

		    EntityManager em = emf.createEntityManager();

		    Query q = em.createQuery("SELECT u " + "FROM Widgetprofile u "
		        + "WHERE u.id=?1");
		    q.setParameter(1, widget_id);

		    //get the widget profile
		    Widgetprofile widgetProfile = null;
		    try {
		      widgetProfile = (Widgetprofile) q.getSingleResult();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

		    if (widgetProfile != null) {

		      //get the uploader user
		      Useractivity uactivity = null;
		      Useractivity result = null;
		      Useraccount uploader = null;
		      Query q2 = em.createQuery("SELECT act " + "FROM Useractivity act "
		          + "WHERE (act.objectId = ?1 AND act.activity='uploaded')");
		      q2.setParameter(1, widget_id);
		      List<Useractivity> results = (List<Useractivity>) q2.getResultList();
		      if (results != null && results.size() > 0) {
		        result = results.get(0);
		      }

		      if (result != null) {
		        List<Useraccount> userResultList = (List<Useraccount>) em
		            .createQuery("SELECT u FROM Useraccount u WHERE u.id=?1")
		            .setParameter(1, result.getSubjectId()).getResultList();
		        if (userResultList != null && userResultList.size() > 0) {
		          uploader = userResultList.get(0);
		        }
		      }
		%>

		<div id="main-content-wrapper">
			<%@ include file="static/sidebar.jsp"%>

			<div id="main">

				<h1>
					<%
					  if (widgetProfile.getName() != null)
					        out.print(widgetProfile.getName());
					%>
				</h1>
				<p class="upload-info">
					<%
					  if (uploader != null) {
					%>
					uploaded by <a
						href="profile.jsp?userid=<%out.print(uploader.getId());%>"> <%
   out.print(uploader.getUsername());
 %> </a> on
					<%
   out.print(result.getTime());
 %>
					<%
					  } else {
					%>
					no upload info available
					<%
					  }
					%>
				</p>

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
						<div id="widget-tags">
							<a href="#" class="small blue tag">Small</a> <a href="#"
								class="small blue tag">blue</a> <a href="#"
								class="small blue tag">button</a>

						</div>
						<h2>Useful for:</h2>
						<div id="widget-useful-for">
							<a href="#" class="small blue tag">collaboration</a> <a href="#"
								class="small blue tag">learning</a> <a href="#"
								class="small blue tag">fun</a>
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
							<li>
								<div class="review-item-wrapper">
									<div class="review-item-pic">
										<img
											src="http://www.gravatar.com/avatar/205e460b479e2e5b48aec05710c08d50?s=35&d=identicon" />
									</div>
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
									<div class="clear"></div>
								</div>
							</li>
							<li>
								<div class="review-item-wrapper">
									<div class="review-item-pic">
										<img
											src="http://www.gravatar.com/avatar/205e434b479e2e5b48aec05710c08d50?s=35&d=identicon" />
									</div>
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
								</div>
							</li>
						</ul>
					</div>
					<div id="related-widgets">
						<h2>Related widgets</h2>
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
			<div class="clear"></div>
		</div>

		<%
		  } else {
		      out.print("widget profile is null");
		    }
		  } else {
		    out.print("widget id is null");
		  }
		%>



	</div>
	<!-- end of page-wrapper -->



	<%@ include file="static/footer.html"%>


</body>
</html>