<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="scripts/widget_partial.js"></script>
<script src="scripts/search.js"></script>

</head>



<body>
<input id="widgetid" type="hidden" value="<%=request.getParameter("id")%>" />
	<%@ include file='components/header.jsp'%>


	<div class="container-fluid">
		<div class="row-fluid">

			<div class="span12">
				<div class="row-fluid">
				    <ul class="breadcrumb">
                        <li><a href="index.jsp">Home</a> <span class="divider">/</span></li>
                        <li class="active">Search<span class="divider">/</span></li>  
                    </ul>				
				</div>
				
				<div class="row-fluid">
				    <div class="span12">
					       <ol id="search_results" class="thumbnails"></ol>   
				    </div>
				</div>

				 <%@ include file='components/footer.jsp'%>
			</div>
		
		</div>
	</div>
	
  
</body>
</html>