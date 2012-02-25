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
						<li><a href="#"><i class="icon-question-sign"></i>Something
								here...</a></li>
					</ul>
				</div>
			</div>
			<div class="span9">
				<h2>About EDUKApp</h2>
				<p>This project is about a cross-university widget store, i.e. a
					repository and community site that focuses on collecting and
					promoting widgets for learning and teaching. The store will be
					accompanied with discovery services and will offer easy integration
					of widgets to personal learning environments while promoting widget
					capabilities and features. It will offer to end users a
					sophisticated recommendation engine will promote widgets according
					to each users needs and interaction history. Widget store shall be
					used by educators as a (centralized) point to focus on any of their
					initiatives for creating a personal learning environment while for
					developers a socially-driver platform for widget development.
					Widget developers will benefit from a framework in place that will
					be able to monitor widget usage; valuable to detect patterns of
					activity in a personal learning environment. 
					</p>
					<p>
					The functional
					requirements of this project include: Social annotation: Each
					widget shares its own profile page and user generated meta-data
					(comments/tags/rating). Functionality to annotate widgets with
					learning activities they afford will be needed. (annotating
					affordances, tagging, rating, comments) Socially-driven
					development: Widget store allows its community of users to submit
					and vote on requirements for the further or new development of
					widgets (requirements voting, feedback, management) age tracking:
					widget store tracks how widgets are utilized, in which context and
					which functionalities are used. Widget Bundles: widgets are grouped
					in collections which support specific tasks; bundles of
					arrangements supporting a particular work flow are required.
					Recommendation engine: an integrated information filtering system
					to provide suggestions both content based (similar widgets) and
					user social circle based (what other users have selected). 
					</p>
					<p>
					While the widget store is not yet public, it is under full development
					and you can checkout the backend code (or contribute) from the
					public code <a href="http://code.google.com/p/edukapp">repository</a></p>
				<%@ include file="/components/footer.jsp"%>
			</div>

		</div>
		<!-- end of page-wrapper -->
</body>
</html>