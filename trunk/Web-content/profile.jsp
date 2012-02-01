<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.Useraccount,
	uk.ac.edukapp.model.Widgetprofile,
	uk.ac.edukapp.renderer.Renderer,
	java.util.*,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"%>
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
String widget_id = request.getParameter("widget_id");

EntityManager em = emf.createEntityManager();
//em.getTransaction().begin();

Query q = em.createQuery("SELECT u " + "FROM Widgetprofile u "
        + "WHERE u.id=?1");

    q.setParameter(1, Integer.parseInt(widget_id));

    Widgetprofile wid = null;
    try {
      wid = (Widgetprofile) q.getSingleResult();
    }catch (javax.persistence.NoResultException e){
      //no results
    }
    if (wid!=null) {
    	out.print(Renderer.render(em, wid));
    }

%>


		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>