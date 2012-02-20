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
<script src="scripts/jquery.anythingslider.min.js"></script>
<link rel="stylesheet" href="css/anythingslider.css" />
<script>
	// DOM Ready
	$(function() {
		$('#widget-slider').anythingSlider({
			expand : false,
			buildNavigation : false,
			buildStartStop : false,

		});
	});
</script>
</head>
<body>

	<%@ include file='components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="#"><i class="icon-question-sign"></i>Something
								here...</a></li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<div class="hero-unit" style="position: relative;">
					<h1>EDUKApp</h1>
					<p>Education UK wide App store</p>
					<div class="span8">
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
							<a class="btn btn-primary btn-large">Learn more »</a>
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


					<div id="widget-slider-wrapper">

						<style>
#slider {
	width: 700px;
	height: 690px;
}

.widget-wrapper {
	float: left;
}
</style>


						<%
							List<Widgetprofile> featuredWidgets = null;

							EntityManager em = emf.createEntityManager();
							Query q = em.createQuery("SELECT u " + "FROM Widgetprofile u "
									+ "WHERE u.featured=1");

							Widgetprofile wid = null;

							featuredWidgets = (List<Widgetprofile>) q.getResultList();
							if (featuredWidgets != null) {
						%>



						<ul id="widget-slider">
							<%
							out.print("homw many:"+featuredWidgets.size());
								for (int i = 0; i < featuredWidgets.size()/2; i++) {
							%>
							<li class="panel" style="height: 200px;">
								<%
									//show 2 widgets per page
											for (int j = i*2; (j < i + 2) && (j < featuredWidgets.size()-1); j++) {

												Widgetprofile w = (Widgetprofile) featuredWidgets
														.get(j);
												String iframe = Renderer.renderById(
														session.getServletContext(), "" + w.getId());//,400,250);
												out.print("<div class='widget-wrapper'>");
												out.print(iframe);

												out.print("<div class='widget-short-info'>");
												out.print("<a href='widget.jsp?id=" + w.getId() + "'>"
														+ w.getName() + "</a>");
												out.print("widget name,rating,stats");
												out.print("<div class='clear' style='clear:both;'></div>");
												out.print("</div>");
												out.print("</div>");

											}
								%>
								<div class="clear" style="clear: both;"></div>


							</li>
							<%
								}
							%>
						
					</div>

					<%
						} else {
							out.print("no featured widgets");
						}
					%>

					<!-- 
				<div id="widget-slider">
				
				
				
					<div id="widget-slider-left-btn">
						&lt;
						<div class="clear"></div>
					</div>
					<div id="widget-slider-container">
						
						<div class="widget-box">
							widget a
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget b
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget c
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget d
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget e
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget g
							<div class="clear"></div>
						</div>
						<div class="widget-box">
							widget h
							<div class="clear"></div>
						</div>

						<div class="clear"></div>
						<div id="widget-slider-control-buttons">o o o o</div>
					</div>

					<div id="widget-slider-right-btn">
						&gt;
						<div class="clear"></div>
					</div>
				</div>
			</div>
			-->
					<div class="clear"></div>
				</div>
				<%@ include file="components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>