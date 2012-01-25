<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="javax.persistence.*,java.util.*,uk.ac.edukapp.roughtests.Person,uk.ac.edukapp.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	  EntityManagerFactory factory = Persistence
	      .createEntityManagerFactory("edukapp");
	  EntityManager em = factory.createEntityManager();

	  /*-----------*/
	  em.getTransaction().begin();

	  Tag t = new Tag();
	  t.setTagtext("test tag text2");

	  Widgetprofile wp = new Widgetprofile();
	  wp.setName("test widget name2");
	  byte b = 0;
	  wp.setW3cOrOs(b);
	  wp.setWidId("http://widget-url");

	  List<Tag> tagsss = wp.getTags();
	  if (tagsss == null)
	    tagsss = new ArrayList<Tag>();
	  tagsss.add(t);
	  wp.setTags(tagsss);

	  em.persist(t);
	  em.persist(wp);

	  em.getTransaction().commit();
	  /*----------*/

	  em.close();
	  factory.close();
	%>
	xairetai!!!
</body>
</html>