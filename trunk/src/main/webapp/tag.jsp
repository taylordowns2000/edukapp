<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<base href="../"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="scripts/widget_partial.js"></script>
<script src="scripts/tag.js"></script>
</head>
<body>
	<%
	  String tagid = request.getParameter("id");
	%>
	<input id="tagid" type="hidden" value="<%=tagid%>"></input>
	<%@ include file='components/header.jsp'%>

	<div class="container-fluid">
		<div class="row-fluid">
		
			<div class="span12">
				<div class="row-fluid">
                    <ul class="breadcrumb">
                        <li><a href="#">Home</a> <span class="divider">/</span></li>
                        <li class="active">Tag<span class="divider">/</span></li>  
                    </ul>
                </div>


            <div class="row-fluid">
                <div class="span12">
                    <ol id="widget-tagged-as-results" class="thumbnails"></ol>
                </div>
            </div>


				<%@ include file="components/footer.jsp"%>
			</div>
		</div>

</body>
</html>