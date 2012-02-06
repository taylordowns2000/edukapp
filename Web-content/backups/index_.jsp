<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.Useraccount,
	java.util.*,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"
	%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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

		<%
		  if (isAuthenticated) {
		    Useraccount loggedinUser = (Useraccount) session
		        .getAttribute("logged-in-user");
		    out.println("user mail:" + loggedinUser.getEmail());
		  }
		%>


		<%
		  WookieConnectorService conn = new WookieConnectorService(
		      "http://localhost:8080/wookie/", "TEST", "myshareddata");

		  HashMap<String, Widget> availableWookieWidgets = conn
		      .getAvailableWidgets();
		  Iterator it = availableWookieWidgets.keySet().iterator();
		  while (it.hasNext()) {
		    out.println(it.next() + "<br/>");
		  }
		  Iterator it2 = availableWookieWidgets.entrySet().iterator();
		  while (it2.hasNext()) {

		    Map.Entry pairs = (Map.Entry) it2.next();
		    //  it2.remove(); // avoids a ConcurrentModificationException

		    Widget widget = (Widget) pairs.getValue();
		    out.print(pairs.getKey() + "<br/>");
		    out.print(widget.getIdentifier() + "<br/>");
		    out.print(widget.getTitle() + "<br/>");
		    out.print(widget.getDescription() + "<br/>");
		    out.print(widget.getIcon().toString() + "<br/>");
		    out.print("<br/><br/><br/>");
		  }

		  conn.setCurrentUser(new User("lucas", "lucasPass"));
		  WidgetInstance ins = conn
		      .getOrCreateInstance("http://www.opera.com/widgets/bubbles");
		%>

		welcome page of edukapp...


		<%
		  out.println("<iframe src=\"" + ins.getUrl() + "\" />");
		%>




		<%
		  //String s= (String)session.getAttribute("a");
		%>

		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>