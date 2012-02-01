<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="javax.persistence.*,
java.util.*,
uk.ac.edukapp.util.MD5Util,
uk.ac.edukapp.roughtests.Person,
uk.ac.edukapp.model.*"%>
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

%>
	
	xairetai!!!
</body>
</html>