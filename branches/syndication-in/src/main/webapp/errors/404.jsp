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

<%@ include file='/components/imports.jsp'%>
<title>EDUKApp</title>
</head>
<body>

	<%@ include file='/components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<div id="error-content">
					<h1>404</h1>
					<p>Oops, something went wrong.</p>
					<p>Please make sure the url is correct, or try searching for what you want using the search bar above.</p>
					<p>If the problem persists plase inform this site's administrator: l.anastasiou@open.ac.uk</p>
				</div>
				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>