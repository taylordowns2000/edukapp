<!-- Top Navbar -->

<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand" href="/index.jsp">Edukapp</a>
				<div class="nav-collapse">
				
				    <shiro:authenticated>
				        <ul class="nav">
						  <li><a href="/user/<shiro:principal property="id"/>"><i class="icon icon-user icon-white"></i><shiro:principal property="username"/></a></li>
						  <li><a style="margin-left:5px;" href="/logout">Log out</a></li>
				        </ul>
					</shiro:authenticated>

				    <shiro:notAuthenticated>
				        <%@ include file="login_form.jsp"%>
					    <ul class="nav">
				         <li id="login_link"><a href="#" onclick="$('#login_form').css('display','inline');$('#login_link').hide();">Log in</a></li>
                         <li><a href="/register.jsp">Register</a></li>
                        </ul>
                    </shiro:notAuthenticated>
                    
				    <form class="navbar-search pull-right" action="/search_results.jsp" method="get"><input type="text" id="q" name="q" class="search-query" placeholder="Search"></form>
				</div>
			</div>
		</div>
	</div>
<!-- /Top Navbar -->