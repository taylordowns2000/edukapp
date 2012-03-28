<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%@ include file='/components/imports.jsp'%>
<%@ include file='/components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="/scripts/search.js"></script>

</head>



<body>
<input id="widgetid" type="hidden" value="<%=request.getParameter("id")%>" />
	<%@ include file='/components/header.jsp'%>


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						 <li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>						
						<li><a href="#"><i class="icon-question-sign"></i>Help topic: searching</a>
						</li>
					</ul>
				</div>
				<!--/.well -->
			</div>
			<!--/span-->
			<div class="span9">
				<div class="row-fluid">
				
				<ul class="breadcrumb">
  <li>
    <a href="/index.jsp">Home</a> <span class="divider">/</span>
  </li>
  <li class="active">
    Search<span class="divider">/</span>
  </li>  
</ul>				
				</div>
				<div class="row-fluid">
					<div class="span6">
					  <ol id="search_results" class="thumbnails">
					  
					  </ol>
					  
				      
					</div>
					<!--/span-->
					<div class="span6">		  
					</div>
					<!--/span-->
				</div>
				<!--/row-->
				 <%@ include file='/components/footer.jsp'%>
			</div>
			<!--/span-->			
		</div>
		<!--/row-->
		<hr>
	</div>
	
  
</body>
</html>