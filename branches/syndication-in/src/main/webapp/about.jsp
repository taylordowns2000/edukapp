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
						<li><a href="/index.jsp"><i class="icon-home"></i>Home</a></li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<h2>About EDUKApp</h2>
				<p>This project is about creating a cross-university widget
					store, i.e. a repository and community site that focuses on
					collecting and promoting widgets for learning and teaching.</p>
				<p>The store will be accompanied with discovery services and
					will offer easy integration of widgets to personal learning
					environments while promoting widget capabilities and features. For
					end users, it will offer a sophisticated recommendation engine
					which will promote widgets based on user’ needs and interaction
					history.</p>
				<p>The main audience for the store is anticipated to be from the
					HE teaching and learning community. It is envisioned that the store
					will be both place to share developing initiatives and widgets, and
					also a place to gather learning analytics around widget usage.</p>
				<p>Functional requirements:</p>
				<p>Social annotation: Each widget shares its own profile page
					and user generated meta-data (comments/tags/rating). Functionality
					to annotate widgets with learning activities they afford will be
					needed. (annotating affordances, tagging, rating, comments)</p>
				<p>Socially-driven development: The store allows its community
					of users to submit and vote on requirements for the further or new
					development of widgets (requirements voting, feedback, management)
					: the store tracks how widgets are utilized, in which context and
					which functionalities are used.</p>
				<p>Widget Bundles: widgets are grouped in ‘collections’ which
					support specific tasks</p>
				Recommendation engine: an integrated information filtering system to
				provide suggestions for both content based (similar widgets) and
				user social circle based (i.e..what other users have selected).
				</p>

				<p>
					While the widget store is not yet public, it is under full
					development and you can checkout the back-end code (or contribute)
					from the public code <a href="http://code.google.com/p/edukapp">repository</a>
				</p>
				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>