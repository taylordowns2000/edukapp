<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>



	<div id="formWrapper">
		<h2>Login page</h2>

		<form type="post" action="handlers/loginhandler.jsp">
			<div>
				<label for="usr_name">userID</label> <input id="name" name="username"
					type="text" /> <span id="nameInfo">What's your name?</span>
			</div>
			<div>
				<label for="usr_password">password</label> <input id="pass"
					name="password" type="password" /> <span id="passInfo">Your
					password</span>
			</div>
			<div>
				<input id="login" name="login" type="submit" value="login" />
			</div>
		</form>
		<p>
			Not a user yet? Go ahead and <a href="register.jsp">register</a>
		</p>

	</div>



</body>
</html>