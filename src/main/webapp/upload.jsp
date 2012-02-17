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
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/main.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<script src="scripts/jquery.min.js"></script>
<script src="scripts/jquery-ui-1.8.17.custom.min.js"></script>
<script>
	$(document).ready(function() {
		$('#upload-forms-tabs').tabs();
	});
</script>
<style>
.ui-tabs {
	position: relative;
	padding: .2em;
	zoom: 1;
}
/* position: relative prevents IE scroll bug (element with position: relative inside container with overflow: auto appear as "fixed") */
.ui-tabs .ui-tabs-nav {
	margin: 0;
	padding: .2em .2em 0;
}

.ui-tabs .ui-tabs-nav li {
	list-style: none;
	float: left;
	position: relative;
	top: 1px;
	margin: 0 .2em 1px 0;
	border-bottom: 0 !important;
	padding: 0;
	white-space: nowrap;
}

.ui-tabs .ui-tabs-nav li a {
	float: left;
	padding: .5em 1em;
	text-decoration: none;
}

.ui-tabs .ui-tabs-nav li.ui-tabs-selected {
	margin-bottom: 0;
	padding-bottom: 1px;
}

.ui-tabs .ui-tabs-nav li.ui-tabs-selected a,.ui-tabs .ui-tabs-nav li.ui-state-disabled a,.ui-tabs .ui-tabs-nav li.ui-state-processing a
	{
	cursor: text;
}

.ui-tabs .ui-tabs-nav li a,.ui-tabs.ui-tabs-collapsible .ui-tabs-nav li.ui-tabs-selected a
	{
	cursor: pointer;
}
/* first selector in group seems obsolete, but required to overcome bug in Opera applying cursor: text overall if defined elsewhere... */
.ui-tabs .ui-tabs-panel {
	display: block;
	border-width: 0;
	padding: 1em 1.4em;
	background: none;
}

.ui-tabs .ui-tabs-hide {
	display: none !important;
}
</style>
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

				<%
				  if (isAuthenticated) {
				%>
				<div id="upload-forms-tabs">
					<ul>
						<li><a href="#upload-form">W3C widget</a></li>
						<li><a href="#upload-gadget-form">Gadget</a></li>
					</ul>
					<div class="clear"></div>
					<div id="upload-form">
						<form method="post"
							action="/upload"
							name="upform" enctype="multipart/form-data">
							<p>Select a widget archive (.zip/.wgt) to upload:</p>

							<p>
								<input type="file" name="uploadfile" size="50" class="button" />
							</p>
							<p>
								<input class="button" type="submit" name="Submit"
									value="Publish" /> <input class="button" type="submit"
									name="Reset" value="Clear" />
							</p>
						</form>
					</div>

					<div id="upload-gadget-form">
						<form method="post" action="/upload" name="gadgetupform">
							<p>Enter the url of gadget (.xml)</p>
							<p>
								Gadget name: <input type="text" name="gadget-name" size="50"
									class="button" />
							</p>
							<p>
								Url: <input type="text" name="uploadurl" size="50"
									class="button" />
							</p>
							<p>
								<input class="button" type="submit" name="Submit"
									value="Publish" />
							</p>
						</form>
					</div>
				</div>
				<div class="clear"></div>



			</div>
			<div class="clear"></div>

			<%
			  } else {//user not logged in
			%>
			<h2>Sorry</h2>
			<p>You need to log in to upload a widget.</p>
			<%
			  }
			%>
		</div>
		<div class="clear"></div>
	</div>





	<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>