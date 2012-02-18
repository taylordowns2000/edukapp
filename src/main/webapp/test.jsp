
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.*,
	uk.ac.edukapp.service.*,
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
	  WidgetProfileService ws = new WidgetProfileService(request.getServletContext());
	  	  
	  Widgetprofile widgetProfile = ws.findWidgetProfileById("1");
	  
	  out.print(widgetProfile.getName());
	  
	  WidgetDescription desc = widgetProfile.getDescription(); 
	  
	  out.print(desc.getDescription());	  
	
	%>

	
</body>
</html>