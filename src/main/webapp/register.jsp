<%@page import="uk.ac.edukapp.renderer.WidgetRenderer"%>
<%@page import="uk.ac.edukapp.model.Widgetprofile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,uk.ac.edukapp.model.Useraccount,java.util.*,
	javax.persistence.*,javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory,
	uk.ac.edukapp.renderer.*"%>
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

	<%@ include file='/components/header.jsp'%>

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
				<div id="formWrapper">
					<h2>Register</h2>
					<form method="POST" action="/register">
						<div>
							<label for="name">Name</label> <input autocomplete="off"
								id="name" name="name" type="text" /> <span id="nameInfo">What's
								your real name?</span>
						</div>
						<div>
							<label for="userID">userID</label> <input autocomplete="off"
								id="userID" name="username" type="text" /> <span id="userInfo">what's
								your desired ID ,you will need it to log in</span>
						</div>
						<div>
							<label for="email">E-mail</label> <input autocomplete="off"
								id="email" name="email" type="text" /> <span id="emailInfo">Valid
								E-mail please</span>
						</div>
						<div>
							<label for="pass1">Password</label> <input autocomplete="off"
								id="pass1" name="password" type="password" /> <span
								id="pass1Info">At least 5 characters: letters, numbers
								and '_'</span>
						</div>
						<div>
							<label for="pass2">Confirm Password</label> <input
								autocomplete="off" id="pass2" name="pass2" type="password" /> <span
								id="pass2Info">Confirm password</span>
						</div>
						<div>
							<input id="register" name="register" type="submit"
								value="register" />
						</div>
					</form>
				</div>

				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>
















</body>
</html>