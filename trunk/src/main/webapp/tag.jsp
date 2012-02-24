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
<script src="/scripts/tag.js"></script>
</head>
<body>
	<%
	  String tagid = request.getParameter("id");
	%>
	<input id="tagid" type="hidden" value="<%=tagid%>"></input>
	<%@ include file='/components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="#"><i class="icon-question-sign"></i>Something
								here...</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<h1>Tag</h1>


				<ul id="widget-tagged-as-results" class="thumbnails">

				</ul>


				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>