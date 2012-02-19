		<%
		  //--------------------------------
		  // deduce whether user is logged in
		  //--------------------------------
		  EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		  boolean isAuthenticated = JspUtils.isAuthenticated(session, request, emf);
		%>