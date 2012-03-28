<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>

</head>
<body>

	<%@ include file='/components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						 <li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>						
					</ul>
				</div>
			</div>
			
			<div class="span9" id="main">

		      <h2>Log in</h2>

		      <form method="POST" class="well" action="/login">
		
                <label for="usr_name" >User name</label>
                <input autocomplete="off" id="name" name="username" type="text" placeholder="user name">

                <label for="usr_password">password</label> 
                <input autocomplete="off" id="pass" name="password" type="password" placeholder="password">
			
                <label for="rememberMe" class="checkbox">remember me
				    <input style="width:20px;float:left;" id="rememberMe" type="checkbox" name="rememberMe">
			     </label>
                <button class="btn" name="login" type="submit">Log in</button>

		      </form>

		  <p>Not a user yet? Go ahead and <a href="register.jsp">register</a></p>

        <%@ include file="/components/footer.jsp"%>
        </div>

    </div>
    
    <script>
		  var wrong = getParameterByName("wrong");
		  if (wrong){
		      var message = document.createElement("p");
		      $(message).attr("class","alert alert-error");
		      $(message).text("Wrong login or non-existing user. Please try again!");
		      $(message).appendTo("#main").fadeIn();
		  }
    </script>
</body>
</html>

