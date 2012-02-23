package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.util.MD5Util;

/**
 * Servlet implementation class RegisterServlet
 */

public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private Log log;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public LoginServlet() {
    super();
    log = LogFactory.getLog(LoginServlet.class);
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // get parameters
    String username = null;
    String password = null;
    String remember = null;

    username = request.getParameter("username");
    password = request.getParameter("password");
    remember = request.getParameter("remember");

    EntityManagerFactory factory = (EntityManagerFactory) getServletContext()
        .getAttribute("emf");

    EntityManager em = factory.createEntityManager();
    em.getTransaction().begin();

    Query q = em.createQuery("SELECT u " + "FROM Useraccount u "
        + "WHERE u.username=?1");

    q.setParameter(1, username);

    Useraccount ua = null;
    try {
      ua = (Useraccount) q.getSingleResult();
    } catch (javax.persistence.NoResultException e) {
      // no results
    }

    if (ua != null) {// user exists

      String salt = ua.getSalt();
      String inputHashedPassword = MD5Util.md5Hex(password + salt);

      if (ua.getPassword().equals(inputHashedPassword)) {// correct password

        if (remember != null && remember.equals("on")) {
          // produce remember me token, store in user bean and cookie
          UUID token = UUID.randomUUID();
          Cookie rememberCookie = new Cookie("edukapp-remember",
              token.toString());
          rememberCookie.setPath("/");
          rememberCookie.setMaxAge(30 * 24 * 60 * 60);
          response.addCookie(rememberCookie);
          ua.setToken(token.toString());
          em.persist(ua);
        }
        //em.getTransaction().commit();

        HttpSession session = request.getSession();
        session.setAttribute("authenticated", "true");
        session.setAttribute("logged-in-user", ua);

        log.info("update account info last seen field");
        
        try {
          log.info("user id ="+ua.getId());
          Accountinfo ai = (Accountinfo) em.createQuery(
              "SELECT a " + "FROM Accountinfo a WHERE a.id=?1")
              .setParameter(1, ua.getId())
              .getSingleResult();
          java.util.Date date = new java.util.Date();
          Timestamp now = new Timestamp(date.getTime());
          ai.setLastseen(now);

          em.persist(ai);
          em.getTransaction().commit();
        } catch (Exception e) {
          log.info("exception");
e.printStackTrace();
        }

        String from = request.getParameter("from");
        
        if (from.equals("/login.jsp")){
          response.sendRedirect("/index.jsp");
        }else {
          response.sendRedirect(from);
        }

      } else {// user wrong authentication
        em.getTransaction().commit();
        doForward(request, response, "/login.jsp?wrong=true");
      }

    } else {// user does not exist
      em.getTransaction().commit();
      doForward(request, response, "/login.jsp?wrong=true");
    }
  }

  private void doForward(HttpServletRequest request,
      HttpServletResponse response, String jsp) throws ServletException,
      IOException {

    log.info("doFroward() before dispatching");
    // RequestDispatcher dispatcher =
    // getServletContext().getRequestDispatcher(jsp);
    // dispatcher.forward(request, response);
    response.sendRedirect(request.getContextPath() + jsp);

  }

}
