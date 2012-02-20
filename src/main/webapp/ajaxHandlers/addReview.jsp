<%@ page
	import="java.io.*,org.apache.wookie.connector.framework.*,uk.ac.edukapp.util.*,uk.ac.edukapp.model.*,uk.ac.edukapp.service.*,java.util.*,javax.persistence.*,java.sql.Timestamp,javax.persistence.EntityManager,javax.persistence.EntityManagerFactory"%>
<%
	PrintWriter pw = response.getWriter();


	EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
			.getAttribute("emf");
	EntityManager entityManager = emf.createEntityManager();

	//get parameters
	String userid = request.getParameter("userid");
	String reviewText = request.getParameter("reviewText");
	String widgetid = request.getParameter("id");

	if (userid == null || userid.equals("") || widgetid == null
			|| widgetid.equals("") || reviewText == null
			|| reviewText.equals("")) {
		pw.append("not all parameters are set");
	}

	Useraccount user = entityManager.find(Useraccount.class, userid);
	Widgetprofile widget = entityManager.find(Widgetprofile.class,
			widgetid);

	if (user == null) {
		pw.append("user is null");
	}
	if (widget == null) {
		pw.append("widget is null");
	}

	Comment c = new Comment();
	c.setCommenttext(reviewText);
	try {
		entityManager.getTransaction().begin();
		entityManager.persist(c);
		entityManager.getTransaction().commit();
	} catch (Exception e) {
		pw.append("there was an error while writing to db");
	}

	Userreview ur = new Userreview();
	ur.setComment(c);
	byte b = 0;
	ur.setRating(b);
	ur.setUserAccount(user);
	ur.setWidgetProfile(widget);
	java.util.Date date = new java.util.Date();
	Timestamp now = new Timestamp(date.getTime());
	ur.setTime(now);
	try {
		entityManager.getTransaction().begin();
		entityManager.persist(ur);
		entityManager.getTransaction().commit();
	} catch (Exception e) {
		pw.append("there was an error while writing to db");
	}
	pw.append("update done");
%>