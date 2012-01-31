<%@page import="uk.ac.edukapp.model.Useraccount"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	java.util.*,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/layout.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/main.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<title>Edukapp</title>
</head>
<body>
	<div id="page-wrapper">

		<%
		  //--------------------------------
		  // deduce whether user is logged in
		  //--------------------------------
		  String isAuthenticated = (String) session.getAttribute("authenticated");

		  if (isAuthenticated != null && isAuthenticated.equals("true")) {
		%>
		<p>session attributes are set!</p>
		<%@ include file="static/logged-in-as-box.jsp"%>
		<%
		  } else {
		    //check if remember cookie is set
		    Cookie[] cookies = request.getCookies();

		    String token = null;
		    if (cookies != null) {
		      token = ServletUtils
		          .getCookieValue(cookies, "edukapp-remember", null);
		    }
		    out.println("token:" + token);

		    EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
		        .getAttribute("emf");
		    EntityManager em = emf.createEntityManager();

		    if (token != null) {
		      Useraccount ua = null;
		      em.getTransaction().begin();
		      Query q = em.createQuery("SELECT u " + "FROM Useraccount u "
		          + "WHERE u.token=?1");
		      q.setParameter(1, token);
		      try {
		        ua = (Useraccount) q.getSingleResult();
		      } catch (Exception e) {
		        //user not found
		      }
		      if (ua != null) {
		        session.setAttribute("authenticated", "true");
		        session.setAttribute("logged-in-user", ua);
		        out.println("logged in as " + ua.getUsername());
		%>
		<p>session was not set but remember cookie was there</p>
		<%@ include file="static/logged-in-as-box.jsp"%>
		<%
		  } else {
		%>
		<p>session was not set but remember cookie was there but user for
			this token did not exist</p>
		<%@ include file="static/login-box.jsp"%>
		<%
		  }
		    } else {
		%>
		<p>session was not set and remember cookie was NOT there EITHER</p>
		<%@ include file="static/login-box.jsp"%>
		<%
		  }
		  }
		  //----------------------------------------
		  //  end of deducing is user logged in
		  //----------------------------------------
		%>

		<%@ include file="static/header.html"%>

		<%
		  isAuthenticated = (String) session.getAttribute("authenticated");
		  if (isAuthenticated != null && isAuthenticated.equals("true")) {
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
		  
		  conn.setCurrentUser(new User("lucas","lucasPass"));
		  WidgetInstance ins = conn.getOrCreateInstance("http://www.opera.com/widgets/bubbles");
		  
		  
		%>

		welcome page of edukapp...


<%
out.println("<iframe src=\""+ins.getUrl()+"\" />");
%>




		<%
		  //String s= (String)session.getAttribute("a");
		%>

		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>