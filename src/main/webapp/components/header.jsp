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
					
				    <ul class="nav">
						<li><a href="profile.jsp?userid=<% out.print(loggedinuser.getId());%>"><i class="icon icon-user icon-white"></i><% out.print(loggedinuser.getUsername());%></a></li>
						<li><a style="margin-left:5px;" href="/logout">Log out</a></li>
				    </ul>

					<%
					  } else {
					%>
				
				    <%@ include file="login_form.jsp"%>

					<ul class="nav">
				        <li id="login_link"><a href="#" onclick="$('#login_form').css('display','inline');$('#login_link').hide();">Log in</a></li>
                        <li><a href="register.jsp">Register</a></li>
                    </ul>
                    
					<%
					  }
					%>
				    <form class="navbar-search pull-right"><input type="text" class="search-query" placeholder="Search"></form>
				</div>
			</div>
		</div>
	</div>
<!-- /Top Navbar -->