<!-- Top Navbar -->

    <div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand" href="#">Edukapp</a>
				<div class="nav-collapse">
					<%
					  if (isAuthenticated) {
					    
					    Useraccount loggedinuser = (Useraccount)session.getAttribute("logged-in-user");
					    
					%>
					<p class="navbar-text pull-right">
						Logged in as <a href="profile.jsp?userid=<% out.print(loggedinuser.getId());%>"><% out.print(loggedinuser.getUsername());%></a><a style="margin-left:5px;" href="/logout">logout</a>
					</p>
					<%
					  } else {
					%>
                    <form class="navbar-search pull-left"><input type="text" class="search-query" placeholder="Search"></form>

	                <%@ include file="login_form.jsp"%>
					
					<%
					  }
					%>
				</div>
			</div>
		</div>
	</div>
<!-- /Top Navbar -->