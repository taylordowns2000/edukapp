
<%@ page
	import="java.io.*,org.apache.wookie.connector.framework.*,uk.ac.edukapp.util.*,uk.ac.edukapp.model.*,uk.ac.edukapp.service.*,java.util.*,javax.persistence.*,javax.persistence.EntityManager,javax.persistence.EntityManagerFactory,org.apache.commons.logging.Log,org.apache.commons.logging.LogFactory"%>
<%
	PrintWriter pw = response.getWriter();

	//get parameters	
	String newTag = request.getParameter("newTag");
	String widgetid = request.getParameter("id");

	if (newTag == null || newTag.equals("") || widgetid == null
			|| widgetid.equals("")) {
		pw.append("not all parameters are set");
	}

	EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
			.getAttribute("emf");
	EntityManager entityManager = emf.createEntityManager();
	WidgetProfileService ws = new WidgetProfileService(
			request.getServletContext());

	Widgetprofile widget = ws.findWidgetProfileById(widgetid);

	

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
		entityManager.getTransaction().begin();
		entityManager.persist(tag);
		entityManager.getTransaction().commit();
	}

	List<Tag> widget_tags = (List<Tag>) widget.getTags();

	if (widget_tags == null) {
		widget_tags = new ArrayList<Tag>();
	}

	// widget_tags.contains(newTag) fails - even after implementing equals() and
	// hash_code() in Tag, WidgetProfile
	// so use this primitive way
	boolean contains = false;
	for (Tag t : widget_tags) {
		if (t.getTagtext().equals(tag.getTagtext())
				&& t.getId() == tag.getId()) {
			contains = true;
		}
	}

	if (contains) {

		pw.append("tag already assigned for this widget");

	} else {

		widget_tags.add(tag);
		widget.setTags(widget_tags);

		entityManager.getTransaction().begin();
		entityManager.merge((widget));
		entityManager.getTransaction().commit();
		
		pw.append("addition done"+tag.getId());

	}
%>