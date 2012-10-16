<!-- Top Navbar -->

<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
			
			    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                
				<a class="brand" href="index.jsp">Edukapp</a>
				<div class="nav-collapse">
				
				    <shiro:authenticated>
				        <ul class="nav">
				          <div id="logged-in-user-id" style="display:none"><shiro:principal property="id"/></div>
						  <li><a href="user/<shiro:principal property="id"/>"><i class="icon icon-user icon-white"></i><span id="logged-in-user-name"><shiro:principal property="username"/></span></a></li>
						  <input type="hidden" id="logged-in-user-gravatar-img" value="http://placehold.it/64">
						  <li><a style="margin-left:5px;" href="logout">Log out</a></li>
				        </ul>
					</shiro:authenticated>

				    <shiro:notAuthenticated>
				        <%@ include file="login_form.jsp"%>
					    <ul class="nav">
				         <li id="login_link"><a href="#" onclick="$('#login_form').css('display','inline');$('#login_link').hide();">Log in</a></li>
                         <li><a href="register.jsp">Register</a></li>
                        </ul>
                    </shiro:notAuthenticated>
                    
				    <form class="navbar-search pull-right" action="search_results.jsp" method="get"><input type="text" id="q" name="q" class="search-query" placeholder="Search"></form>
				</div>
			</div>
		</div>
	</div>
<!-- /Top Navbar -->