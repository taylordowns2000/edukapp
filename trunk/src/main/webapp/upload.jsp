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

				<%
				  if (isAuthenticated) {
				%>

				<div id="upload-form">
					<form method="post"
						action="http://localhost:8080/wookie/admin/WidgetAdminServlet?operation=UPLOADWIDGET"
						name="upform" enctype="multipart/form-data">
						<p>Select a widget archive (.zip/.wgt) to upload:</p>

						<p>
							<input type="file" name="uploadfile" size="50" class="button" />
						</p>
						<p>
							<input class="button" type="button" name="Submit" value="Publish" />
							<input class="button" type="reset" name="Reset" value="Clear" />
						</p>

					</form>
				</div>

				<div id="upload-gadget-form"></div>
			</div>

			<%
			  } else {//user not logged in
			%>
			<h2>Sorry</h2>
			<p>You need to log in to upload a widget.</p>
			<%
			  }
			%>
		</div>
	</div>





	<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>