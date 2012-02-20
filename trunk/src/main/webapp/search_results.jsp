<%@page import="uk.ac.edukapp.renderer.WidgetRenderer"%>
<%@page import="uk.ac.edukapp.model.Widgetprofile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="uk.ac.edukapp.util.JspUtils,
	uk.ac.edukapp.model.Useraccount,
	uk.ac.edukapp.renderer.*,
	javax.persistence.EntityManagerFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="scripts/search.js"></script>

</head>



<body>
<input id="widgetid" type="hidden" value="<%=request.getParameter("id")%>" />
	<%@ include file='components/header.jsp'%>


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header">Sidebar</li>
						<li><a href="#"><i class="icon-question-sign"></i>Help topic: searching</a>
						</li>
					</ul>
				</div>
				<!--/.well -->
			</div>
			<!--/span-->
			<div class="span9">
				<div class="row-fluid">
					<h1>Results</h1>
					<p id="search_results_info"></p>
				</div>
				<div class="row-fluid">
					<div class="span6">
					  <ol id="search_results">
					  
					  </ol>
					  
				      <div class="pagination">
                        <ul>
                            <li><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>
                        </ul>
                      </div>
					</div>
					<!--/span-->
					<div class="span6">		  
					</div>
					<!--/span-->
				</div>
				<!--/row-->
				 <%@ include file='components/footer.jsp'%>
			</div>
			<!--/span-->			
		</div>
		<!--/row-->
		<hr>
	</div>
	
  
</body>
</html>