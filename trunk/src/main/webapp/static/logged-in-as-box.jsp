<div id="login-box">
<%
Useraccount loggedinuser = (Useraccount)session.getAttribute("logged-in-user");
%>
<a href="profile.jsp?userid=<% out.print(loggedinuser.getId());%>"><% out.print(loggedinuser.getUsername());%></a>

<a href="<% out.print(request.getContextPath().toString()); %>/logout">logout</a>
</div>