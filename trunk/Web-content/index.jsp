<%@page import="uk.ac.edukapp.model.Useraccount"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="uk.ac.edukapp.util.*,javax.persistence.*,javax.persistence.EntityManager,javax.persistence.EntityManagerFactory"%>
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
		%>

		<%
		  }
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


		welcome page of edukapp...



		<%
		  //String s= (String)session.getAttribute("a");
		%>

		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>