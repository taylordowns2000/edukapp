<h3>logged in as box</h3>
<%
Useraccount loggedinuser = (Useraccount)session.getAttribute("logged-in-user");
out.print(loggedinuser.getEmail());
%>
<a href="<% out.print(request.getContextPath().toString()); %>/logout">logout</a>