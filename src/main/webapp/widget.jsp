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
<script src="/scripts/widget_partial.js"></script>
<script src="/scripts/widget.js"></script>
<script src="/scripts/bootstrap-modal.js"></script>
<script src="/scripts/jquery.raty.min.js"></script>
</head>



<body>
	<input id="widgetid" type="hidden"
		value="<%=request.getParameter("id")%>" />
	<%@ include file='components/header.jsp'%>


	<div class="container-fluid" style="display:none" id="widget_info">
		<div class="row-fluid">
			
			<div class="span4">
				<div class="row-fluid" style="position:relative;">
					<div class="span9">
					<img id="widget_icon" src="/images/default-icon.png" class="widget-icon pull-left">
					<h1 id="widget_name">Widget name</h1>
					<p id="upload-info" class="upload-info">no upload info
						available</p>
					</div>
				</div>
				
				<div class="row-fluid">
						<div id="widget-preview"></div>
				</div>
				
				<div class="modal hide" id="embedModal">
				    <div class="modal-header">
				        <a class="close" data-dismiss="modal">×</a>
				        <h3>Embed widget</h3>
				        <p>Use the following code</p>
				    </div>
				    <div class="modal-body">
				        <pre class="prettyprint linenums"></pre>
				        <p id="modal-body-lti"></p>
				    </div>
				    <div class="modal-footer">
				        <!-- <a href="#" class="btn btn-primary">Save changes</a>--> 
				        <a href="#" class="btn" data-dismiss="modal">Close</a>
				    </div>
				</div> 
					
				<!-- Actions -->		
				<p>
				    <a id="embedModal-link" data-toggle="modal" data-target="#embedModal" href="#embedModal" class="btn btn-primary"> <i class="icon-share-alt icon-white"></i> Embed</a>		
				    <a href="#" class="btn btn-primary"><i class="icon-download icon-white"></i> Download</a></li>
				    <a id="widget-open-fully-link" href="#" class="btn btn-primary"><i class="icon-fullscreen icon-white"></i> Full screen</a></li>
				</p>

				<div class="row-fluid">
						<h3>Description:</h3>
						<dl id="widget-description">Lorem Ipsum is simply dummy text
							of the printing and typesetting industry. Lorem Ipsum has been
							the industry's standard dummy text ever since the 1500s, when an
							unknown printer took a galley of type and scrambled it to make a
							type specimen book. It has survived not only five centuries, but
							also the leap into electronic typesetting, remaining essentially
							unchanged. It was popularised in the 1960s with the release of
							Letraset sheets containing Lorem Ipsum passages, and more
							recently with desktop publishing software like Aldus PageMaker
							including versions of Lorem Ipsum.
						</dl>
						<shiro:authenticated><a id="edit-widget-information" href="#">edit</a></shiro:authenticated>
				</div>
				

            </div>	
            
            <div class="span4">
                <br>	
				<div id="user-reviews">
				    <h3>User reviews</h3>
				    <shiro:authenticated><p id="write-a-review-anchor"><a href="#write-a-review">Write a review</a></p></shiro:authenticated>
				    <div id="user-reviews-list"></div>
				</div>
            </div>
			 
            <div class="span4">
			 
				<div class="well">
						<h3>Info</h3>
						<p>3 downloads </p> 
						<p>16 embeds</p>
						
						<br>
						<h3>Ratings</h3>

				        <p>average rating:<span id="widget-rating"></span></p>
							<shiro:authenticated>
				        <p>your rating:<span id="rating-module"></span></p>
							</shiro:authenticated>
							<shiro:notAuthenticated>
				        <p><i>Login to rate</i></p>
							</shiro:notAuthenticated>
				
                        <br>
				        <h3>Tags</h3>
						<div id="widget-tags"></div>
						<shiro:authenticated><a id="add-tag">add tag</a></shiro:authenticated>
                
                        <br>
						<h3>Useful for</h3>
						<div id="widget-useful-for"></div>
						<shiro:authenticated><a id="add-affordance">add affordance</a></shiro:authenticated>
						
						<br>
						<h3>Format</h3>
				        <div id="widget-type"></div>
						
						<shiro:hasRole name="admin">
							<p><a href="#"><i class="icon-remove"></i>Delete widget</a></p>
						</shiro:hasRole>
					</ul>
				</div>
			</div>	
			
			</div>		

            <div class="row-fluid">				
                    <h3>Related widgets</h3>
					<ul id="related-widgets" class="thumbnails"></ul>
            </div>

            <%@ include file='components/footer.jsp'%>

		</div>
	</div>


</body>
</html>