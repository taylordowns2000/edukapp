
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

	  Query q = em.createQuery("SELECT w FROM Widgetprofile w WHERE w.id=901");
	  Widgetprofile widgetProfile2=(Widgetprofile)q.getSingleResult();
	  
	  Widgetprofile widgetProfile = ws.findWidgetProfileById("901");
	  
	  out.print(widgetProfile.getName());
// 	  out.print(widgetProfile2.getName());
	  
// 	  List<Tag> tags = new ArrayList<Tag>();
// 	  Tag tag1 = new Tag();
// 	  tag1.setTagtext("test5");
	  
	  
// 	  em.persist(tag1);
	  
// 	  tags.add(tag1);
// 	  widgetProfile.setTags(tags);
	  
	  //em.persist(widgetProfile);
	  List<Tag> tags = widgetProfile.getTags();
	  
	  for(Tag t:tags){
	    out.print(t.getTagtext());
	  }
	  
	  
	 
	  
	  
	  
	
	%>

	
</body>
</html>