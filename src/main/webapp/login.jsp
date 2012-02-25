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
						<li><a href="#"><i class="icon-question-sign"></i>Something
								here...</a></li>
					</ul>
				</div>
			</div>
			<div class="span9">

	<div id="formWrapper">
		<h2>Login page</h2>

		<form type="POST" action="/login">
		
		
			<div>
				<label for="usr_name" >userID</label> <input autocomplete="off" id="name" name="username"
					type="text" /> <span id="nameInfo">What's your name?</span>
			</div>
			<div>
				<label for="usr_password">password</label> <input autocomplete="off" id="pass"
					name="password" type="password" /> <span id="passInfo">Your
					password</span>
			</div>
			
			<div>				
				<span>remember?<span><input style="width:20px;float:left;" type="checkbox" name="remember"/>
			</div>
			
			<div>
				<input id="login" name="login" type="submit" value="login" />
			</div>
		</form>
		<script>
		    var wrong = getParameterByName("wrong");
		    if (wrong){
		       var message = document.createElement("p");
		       $(message).attr("class","alert alert-error");
		       $(message).text("Wrong login or non-existing user. Please try again!");
		       $(message).appendTo("#formWrapper").fadeIn();
		    }
		</script>

		<p>
			Not a user yet? Go ahead and <a href="register.jsp">register</a>
		</p>

	</div>
			
			<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>

