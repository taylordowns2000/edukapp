<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.persistence.*,java.util.*,uk.ac.edukapp.roughtests.Person"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	  EntityManagerFactory factory = Persistence.createEntityManagerFactory("edukapp");
	  EntityManager em = factory.createEntityManager();

	  /*-----------*/
	  em.getTransaction().begin();

	  Person p = new Person();
	  p.setName("TestUser");
	  p.setSalary(100.19f);
	  em.persist(p);

	  em.getTransaction().commit();
	  /*----------*/

	  em.close();
	  factory.close();
	%>
</body>
</html>