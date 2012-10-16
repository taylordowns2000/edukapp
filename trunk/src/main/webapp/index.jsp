<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

        <%@ include file='components/imports.jsp'%>
        <%@ include file='components/login_check.jsp'%>

        <title>EDUKApp</title>
        <script src="scripts/featured.js"></script>
    </head>
    <body>

        <%@ include file='components/header.jsp'%>

        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
                    <div class="hero-unit" style="position: relative;">
                        <h1>EDUKApp</h1>
                        <p><u>Ed</u>ucational <u>UK</u>-wide <u>app</u> store</p>
                        
                        <div class="span6">
                            <span>
                                Edukapp is a UK wide, cross university widget and app store,
                                focussing on collecting and promoting widgets and apps for teaching and learning.
                                As it develops, it will offer the UK HE community a central point for:
                            </span>
                            <ul>
                                <li>discovery of teaching and learning related widgets and apps</li>
                                <li>one-click deployment of widgets into (personal) learning environments</li>
                                <li>growing recommendation and analytics functionality for both educators and developers.</li>
                            </ul>
                            <span>
                                More information is available <a href="http://widgets.open.ac.uk:8080/about.jsp">here</a>
                            </span>
                            <span>
                                You can learn more about MUPPLEs by watching the <a href="http://www.teleurope.eu/pg/podcasts/channels/131661">MUPPLE lecture series</a>
                            </span>

                            <p style="margin-top: 10px;">
                                <a class="btn btn-primary btn-large" href="/about.jsp">Learn more »</a>
                            </p>
                        </div>

                        <div class="span4" id="intro-screencast">
                            <iframe src="http://player.vimeo.com/video/25706885?title=0&amp;byline=0&amp;portrait=0&amp;color=ffffff" width="401" height="228" frameborder="0" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>
                            <div class="clear" style="clear: both;"></div>
                        </div>
                        <div class="clear" style="clear: both;"></div>

                    </div>  
                </div>
            </div>
            <div class="row-fluid">
                    <div class="span8">
                        <div id="main"></div>
                    </div>
                    <div class="span4 promotion">
                        <h1>got something to share?</h1>
                        <p>Upload your W3C widget or opensocial gadget and share it with the world!</p>
                        <a href="upload.jsp" class="btn btn-warning btn-large">Upload</a>
                    </div>
            </div>
                    
            <%@ include file="components/footer.jsp"%>

        </div>
    </body>
</html>