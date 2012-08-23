
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
	  EntityManager entityManager = emf.createEntityManager();
	  WidgetProfileService ws = new WidgetProfileService(request.getServletContext());
	  	  
	  Widgetprofile widget = ws.findWidgetProfileById("2");
	  

String newTag = "tag for widget2";
	  
	  
	entityManager.getTransaction().begin();

		Query q = entityManager
				.createQuery("SELECT t FROM Tag t WHERE t.tagtext=?1");
		q.setParameter(1, newTag);

		List<Tag> tags = (List<Tag>) q.getResultList();
		Tag tag = null;
		if (tags != null && tags.size() != 0) {
			tag = (Tag) tags.get(0);
		}

		if (tag == null) {
			tag = new Tag();
			tag.setTagtext(newTag);
			entityManager.persist(tag);
		}

		List<Tag> widget_tags = (List<Tag>) widget.getTags();

		if (widget_tags == null) {
			widget_tags = new ArrayList<Tag>();
		}

		
		
		
		if (widget_tags.contains(tag)) {
out.print("contains");
		} else {
			widget_tags.add(tag);
			out.print("does not contains");

// 			widget.setTags(widget_tags);
			
// 			entityManager.merge((widget));

// 			entityManager.getTransaction().commit();

		}
	  
	  
	  
	  
	%>

	
</body>
</html>