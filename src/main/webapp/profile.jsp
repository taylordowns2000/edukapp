<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<base href="../"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>
<script src="scripts/profile.js"></script>

<title>EDUKApp</title>
</head>
<body>    
        <%@ include file='components/header.jsp'%>

        <div class="container-fluid">
		  <div class="row-fluid">
		  
			 <div class="span3">
				<div class="well sidebar-nav">
				   <ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						 <li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>
						</li>
					</ul>
				</div>
            </div>
            
            <div class="span9">
                <div id="user-profile"></div>
                <div id="user-activity"></div>
            </div>
		</div>
	</div>


	<%@ include file="/components/footer.jsp"%>

</body>
</html>