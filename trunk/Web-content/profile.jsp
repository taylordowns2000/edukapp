
<%
  // profile page for user
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.wookie.connector.framework.*,
	uk.ac.edukapp.util.*,
	uk.ac.edukapp.model.Useraccount,
	uk.ac.edukapp.model.Accountinfo,
	java.util.*,
	java.sql.Timestamp,
	javax.persistence.*,
	javax.persistence.EntityManager,
	javax.persistence.EntityManagerFactory,
	org.apache.commons.logging.Log,
	org.apache.commons.logging.LogFactory"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<<<<<<< .mine
<link rel="stylesheet" href="css/layout.css00000000000" type="text/css" />
=======
<link rel="stylesheet" href="css/reset.css" type="text/css" />
<link rel="stylesheet" href="css/layout.css" type="text/css" />
>>>>>>> .r18
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/main.css" type="text/css" />
<link rel="stylesheet" href="css/footer.css" type="text/css" />
<title>EDUKApp</title>
</head>
<body>
	<div id="page-wrapper">
		<%
		  //--------------------------------
		  // deduce whether user is logged in
		  //--------------------------------
		  EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
		      .getAttribute("emf");
		  boolean isAuthenticated = JspUtils.isAuthenticated(session, request, emf);
		  if (isAuthenticated) {
		%>
		<%@ include file="static/logged-in-as-box.jsp"%>
		<%
		  } else {
		%>
		<%@ include file="static/login-box.jsp"%>
		<%
		  }
		%>

		<%@ include file="static/header.html"%>

		<div id="main-content-wrapper">
			<%@ include file="static/sidebar.jsp"%>

			<%
			  String userid = request.getParameter("userid");
			  int user_id = Integer.parseInt(userid);

			  Accountinfo ai = null;
			  Useraccount ua = null;
			  try {
			    EntityManager em = emf.createEntityManager();

			    ua = (Useraccount) em
			        .createQuery("SELECT u " + "FROM Useraccount u WHERE u.id=?1")
			        .setParameter(1, user_id).getSingleResult();

			    ai = (Accountinfo) em
			        .createQuery("SELECT a " + "FROM Accountinfo a WHERE a.id=?1")
			        .setParameter(1, user_id).getSingleResult();
			  } catch (Exception e) {

			  }

			  if (ai == null) {
			    out.print("account info is null");
			  } else {
			    out.print("Account inf0:" + ai.getId() + " " + ai.getShortbio() + " "
			        + ai.getWebsite() + " " + ai.getLastseen() + " " + ai.getJoined());
			  }
			%>






			<div id="main">

				<div id="account-info-bar">
					<div id="account-info-bar-left">
						<h2><%=ua.getUsername()%></h2>
						<div id="user-account-gravatar"></div>
						<div id="user-details">
							<ul id="user-details-list">
								<li>
									<div class="list-item-wrapper">
										<div class="list-item-left">email:</div>
										<div class="list-item-right"><%=ua.getEmail()%></div>
										<div class="clear"></div>
									</div></li>
								<li>
									<div class="list-item-wrapper">
										<div class="list-item-left">real name:</div>
										<div class="list-item-right"><%=ai.getRealname()%></div>
										<div class="clear"></div>
									</div></li>
								<li>
									<div class="list-item-wrapper">
										<div class="list-item-left">website:</div>
										<div class="list-item-right"><%=ai.getWebsite()%></div>
										<div class="clear"></div>
									</div></li>
								<li>
									<div class="list-item-wrapper">
										<div class="list-item-left">joined:</div>
										<div class="list-item-right">
											<%
											  if (ai.getJoined() != null) {
											    out.print(ai.getJoined().toString());
											  } else {
											    out.print("N/A");
											  }
											%>
										</div>
										}
										<div class="clear"></div>
									</div></li>
								<li>
									<div class="list-item-wrapper">
										<div class="list-item-left">last seen:</div>
										<div class="list-item-right">
											<%
											  if (ai.getLastseen() != null) {
											    out.print(ai.getLastseen().toString());
											  }else {
											    out.print("N/A");
											  }
											%>
										</div>
										}
										<div class="clear"></div>
									</div></li>
							</ul>
						</div>
						<div class="clear"></div>
					</div>
					<div id="account-info-bar-right">
						<div id="short-bio-wrapper">
							<h2>Short bio</h2>
							<span>edit</span>
							<textarea name="short-bio" id="" cols="30" rows="10">
							<%=ai.getShortbio()%>
							</textarea>
						</div>
					</div>
				</div>


				<div id="account-activities-bar">
					<div id="user-recent-activities">
					<h2>Recent activity</h2>
					<div id="recent-activity-box">
						<ul>
							<li>
								<div class="activity-item-wrapper">
									<div class="activity-date">date1</div>
									<div class="activity-action">rated</div>
									<div class="activity-description">teleruoep flasmeeting gadget</div>
								</div>
							</li>
							<li>
								<div class="activity-item-wrapper">
									<div class="activity-date">date2</div>
									<div class="activity-action">uploaded</div>
									<div class="activity-description">google maps gagdet</div>
								</div>
							</li>
							<li>
								<div class="activity-item-wrapper">
									<div class="activity-date">date3</div>
									<div class="activity-action">commented</div>
									<div class="activity-description">google docs widget</div>
								</div>
							</li>
							<li>
								<div class="activity-item-wrapper">
									<div class="activity-date">date4</div>
									<div class="activity-action">created bundle</div>
									<div class="activity-description">google collab gadgets</div>
								</div>
							</li>
						</ul>
					</div>
					</div>
					<div id="user-bundles">
					<h2>Widget bundles</h2>
					<div id="user-bundles-box">
						<ul id="user-bundles-list">
							<li>
								<div class="bundle-wrapper">
									<h4 class="bundle-header">bundle A</h4>
									<ul class="bundle-widgets-list">
										<li>
											<div class="widget-part-of-bundle-item">widget A.1</div>
										</li>
										<li>
											<div class="widget-part-of-bundle-item">widget A.2</div>
										</li>
										<li>
											<div class="widget-part-of-bundle-item">widget A.3</div>
										</li>
									</ul>
								</div>
							</li>
							<li>
								<div class="bundle-wrapper">
									<h4 class="bundle-header">bundle B</h4>
									<ul class="bundle-widgets-list">
										<li>
											<div class="widget-part-of-bundle-item">widget B.1</div>
										</li>
										<li>
											<div class="widget-part-of-bundle-item">widget B.2</div>
										</li>
										<li>
											<div class="widget-part-of-bundle-item">widget B.3</div>
										</li>
									</ul>
								</div>
							</li>
						</ul>
					</div>
					</div>
				</div>

			</div>


		</div>





		<%@ include file="static/footer.html"%>

	</div>
	<!-- end of page-wrapper -->
</body>
</html>