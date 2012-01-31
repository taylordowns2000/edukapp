<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="javax.persistence.*,java.util.*,uk.ac.edukapp.util.MD5Util,uk.ac.edukapp.roughtests.Person,uk.ac.edukapp.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	
	String username="anastluc";
	String hashedPassword="7694f4a66316e53c8cdd9d9954bd611d";
	
	  EntityManagerFactory factory = Persistence
        .createEntityManagerFactory("edukapp");
    EntityManager em = factory.createEntityManager();
    
    Query q = em.createQuery("SELECT u "+
        "FROM Useraccount u "+
        "WHERE u.username=?1 AND u.password=?2");
    
    q.setParameter(1,username);
    q.setParameter(2,hashedPassword);
    
    List<Useraccount> result =q.getResultList();
	
	if (result.size()>0) out.print("found a result!");
	else out.print("found nada");
	
		Iterator<Useraccount> it = result.iterator();
while (it.hasNext()){
  Useraccount u = it.next();
  out.println("found:"+u.getEmail()+" "+u.getUsername()+" "+u.getPassword());
}
		
	  em.close();
	  factory.close();
	%>
	xairetai!!!
</body>
</html>