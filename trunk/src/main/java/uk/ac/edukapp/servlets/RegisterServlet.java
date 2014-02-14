package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.model.LtiProvider;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.util.MD5Util;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Log log;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		log = LogFactory.getLog(RegisterServlet.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get parameters
		String username = null;
		String email = null;
		String password = null;
		String realname = null;

		username = request.getParameter("username");
		email = request.getParameter("email");
		password = request.getParameter("password");
		realname = request.getParameter("name");

		// TO-DO add salt
		// need to store it in db -> need to alter table/entity
		// String salt = null;

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("edukapp");
		EntityManager em = factory.createEntityManager();

		/*-----------*/
		em.getTransaction().begin();

		Useraccount ua = new Useraccount();
		ua.setUsername(username);
		ua.setEmail(email);

		UUID token = UUID.randomUUID();
		String salt = token.toString();
		String hashedPassword = MD5Util.md5Hex(salt + password);
		ua.setPassword(hashedPassword);
		ua.setSalt(salt);
		ua.setToken("02");
		em.persist(ua);

		try {
			log.info("ua id:" + ua.getId());
			Accountinfo ai = new Accountinfo();
			ai.setId(ua.getId());
			ai.setRealname(realname);
			java.util.Date date = new java.util.Date();
			Timestamp now = new Timestamp(date.getTime());
			ai.setJoined(now);
			em.persist(ai);
		} catch (Exception e) {
			log.info("got an exception");
			e.printStackTrace();
		}

		//
		// Create an LTI Provider for this account
		//
		LtiProvider ltiProvider = new LtiProvider(ua);
		em.persist(ltiProvider);

		em.getTransaction().commit();
		/*----------*/

		em.close();
		factory.close();

		//
		// log the user join to UserActivity table
		//
		addUserRegisterActivity(ua.getId());

		doForward(request, response, "/index.jsp");

	}

	private void doForward(HttpServletRequest request,
			HttpServletResponse response, String jsp) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}

	private void addUserRegisterActivity(int userId) {
		try {
			ActivityService avtivityServie = new ActivityService(
					getServletContext());
			avtivityServie.addUserActivity(userId, "joined", 0);
		} catch (Exception e) {
			log.error("adding to user activity table failed");
			e.printStackTrace();
		}
	}
}
