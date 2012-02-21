<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="uk.ac.edukapp.util.JspUtils,
	uk.ac.edukapp.model.Useraccount,
	javax.persistence.EntityManagerFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%@ include file='components/imports.jsp'%>
<%@ include file='components/login_check.jsp'%>

<title>EDUKApp</title>
<script src="scripts/widget.js"></script>

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
						<li><a href="#"><i class="icon-share-alt"></i>Embed</a>
						</li>
						<li><a href="#"><i class="icon-download"></i>Download</a>
						</li>
						<li><a href="#"><i class="icon-tag"></i>Tag</a>
						</li>
						<li><a href="#"><i class="icon-comment"></i>Review</a>
						</li>
					</ul>
				</div>
				<!--/.well -->
			</div>
			<!--/span-->
			<div class="span9">
				<div class="row-fluid">
					<h1 id="widget_name">Widget name</h1>
					<p id="upload-info" class="upload-info">no upload info
						available</p>
				</div>
				<div class="row-fluid">
					<div class="span6">

						<div id="widget-preview">
						</div>
						
						<p id="widget-stats-bar" class="label">
							<span id="widget-usage">
                                <i class="icon-download icon-white"></i> 3 downloads <br>
                                <i class="icon-share-alt icon-white"></i>16 embeds<br>
                            </span>
							<span id="widget-rating">average rating <i class="icon-star icon-white"></i><i class="icon-star icon-white"></i><i class="icon-star icon-white"></i> (1 rating)</span>
						</p>


					</div>
					<!--/span-->
					<div class="span5" style="float:right;">
						<h6>Descripiton:</h6>
						<dl id="widget-description">Lorem Ipsum is simply dummy text of the printing and
							typesetting industry. Lorem Ipsum has been the industry's
							standard dummy text ever since the 1500s, when an unknown printer
							took a galley of type and scrambled it to make a type specimen
							book. It has survived not only five centuries, but also the leap
							into electronic typesetting, remaining essentially unchanged. It
							was popularised in the 1960s with the release of Letraset sheets
							containing Lorem Ipsum passages, and more recently with desktop
							publishing software like Aldus PageMaker including versions of
							Lorem Ipsum.
						</dl>
						<a id="edit-widget-information" href="#">edit</a>
						<h6>Tagged as:</h6>
						<div id="widget-tags">
							
						</div>
						<a id="add-tag" href="#">add tag</a>

						<h6>Useful for:</h6>
						<div id="widget-useful-for">
							<a href="#" class="btn btn-warning"><i class="icon-ok-circle icon-white"></i> collaboration</a> 
                            <a href="#" class="btn btn-warning"><i class="icon-ok-circle icon-white"></i> learning</a> 
                            <a href="#" class="btn btn-warning"><i class="icon-ok-circle icon-white"></i> fun</a>
						</div>
						
						<h6>Related widgets</h6>
                        <ul id="related-widgets" class="thumbnails"></ul>
					</div>
					<!--/span-->
				</div>
				<div class="row-fluid">
					<div id="user-reviews" class="span6">
						<h6>User reviews</h6>
                        <p id="write-a-review-anchor"><a href="#write-a-review">Write a review</a></p>
						<div id="user-reviews-list">
                        </div>
					</div>
                    <div class="span4">&nbsp;</div>
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