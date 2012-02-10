
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.*,
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
<title>Insert title here</title>
</head>
<body>

	<%
	  EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
	      .getAttribute("emf");
	  EntityManager em = emf.createEntityManager();

	  Query q = em.createQuery("SELECT act " + "FROM Useractivity act "
	      + "WHERE (act.objectId = ?1 AND act.activity='uploaded')");
	  q.setParameter(1, 1051);

	  //get the widget profile
	  Useractivity uactivity = null;
	  Useractivity result = null;
	  Useraccount uploader = null;
	  List<Useractivity> results = (List<Useractivity>) q.getResultList();
	  if (results != null && results.size() > 0) {
	    result = results.get(0);
	  }
	  
	  if (result != null) {
	    List<Useraccount> userResultList = (List<Useraccount>) em
	        .createQuery("SELECT u FROM Useraccount u WHERE u.id=?1")
	        .setParameter(1, result.getSubjectId())
	        .getResultList();
	    if (userResultList != null && userResultList.size() > 0) {
		    uploader = userResultList.get(0);
		  }
	  }
	  
	  out.print(uploader.getEmail());
	%>

	xairetai!!!
</body>
</html>