<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="/scripts/widget_partial.js"></script>
<script src="/scripts/activity.js"></script>
</head>
<body>
	<%
		String activityid = request.getParameter("id");
	%>
	<input id="activityid" type="hidden" value="<%=activityid%>"></input>
	<%@ include file='/components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>

						</li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<ul class="breadcrumb">
					<li><a href="#">Home</a> <span class="divider">/</span></li>
					<li class="active">Activity<span class="divider">/</span>
					</li>
				</ul>

				<ul id="widgets-with-activity" class="thumbnails">

				</ul>


				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>