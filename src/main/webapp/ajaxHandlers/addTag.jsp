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

 			widget.setTags(widget_tags);
			
 			entityManager.merge((widget));

			entityManager.getTransaction().commit();

		}
	  
	  
	  
	  
	%>