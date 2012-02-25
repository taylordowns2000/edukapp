<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%@ include file='/components/imports.jsp'%>
<%@ include file='/components/login_check.jsp'%>

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
						<li><a href="#"><i class="icon-question-sign"></i>Help topic: uploading</a>
						</li>
					</ul>
				</div>
            </div>
            <div class="span9">
		
                <shiro:authenticated>
				
				<div class="tabbable">
                  <ul class="nav nav-tabs">
                      <li class="active"><a href="#1" data-toggle="tab">W3C Widget</a></li>
                      <li><a href="#2" data-toggle="tab">OpenSocial Gadget</a></li>
                  </ul>
                  <div class="tab-content">
                    <div class="tab-pane active" id="1">
						  <form method="post"
							action="/upload"
							name="upform" enctype="multipart/form-data"
							class="well">
							
							<label>Select a widget archive (.zip/.wgt) to upload:</label>
							<input type="file" name="uploadfile" size="50"/>
							
				             <button type="submit" name="Submit" value="Publish">Publish</button>
						  </form>					
                    </div>
                    <div class="tab-pane" id="2">
						  <form 
						   method="post" 
						   action="/upload" 
						   name="gadgetupform"
						   class="well">
						   
							<label>Enter the url of gadget (.xml)</label>
							<input type="text" name="uploadurl" size="50"/>
									
				            <label>Gadget name:</label>
				            <input type="text" name="gadget-name" size="50"/>

				             <button type="submit" name="Submit" value="Publish">Publish</button>
						  </form>
                     </div>
                  </div>
               </div>
                </shiro:authenticated>

			<shiro:notAuthenticated>
			<div class="alert alert-error">
			  <p>You need to log in to upload a widget.</p>
			</div>
            </shiro:notAuthenticated>
            
		</div>
      </div>
	</div>

	<%@ include file="components/footer.jsp"%>

</body>
</html>