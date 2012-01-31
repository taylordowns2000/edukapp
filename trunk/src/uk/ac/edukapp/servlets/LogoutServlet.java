package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.security.SecureRandom;
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

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.util.MD5Util;

/**
 * Servlet implementation class RegisterServlet
 */

public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private Log log;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public LogoutServlet() {
    super();
    log = LogFactory.getLog(LogoutServlet.class);
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null) {
      log.info("Session is null");
    }

    session.invalidate();

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (int i = 0; i < cookies.length; i++) {
        cookies[i].setMaxAge(0);
        response.addCookie(cookies[i]);
      }
    }

    log.info("logout finished");
    doForward(request, response, "/index.jsp");
    
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null) {
      log.info("Session is null");
    }

    session.invalidate();

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (int i = 0; i < cookies.length; i++) {
        cookies[i].setMaxAge(0);
        response.addCookie(cookies[i]);
      }
    }

    log.info("logout finished");
    doForward(request, response, "/index.jsp");
  }


  
  
  private void doForward(HttpServletRequest request,
      HttpServletResponse response, String jsp) throws ServletException,
      IOException {
    // RequestDispatcher dispatcher = getServletContext()
    // .getRequestDispatcher(jsp);
    // dispatcher.forward(request, response);
    response.sendRedirect(request.getContextPath() + jsp);
  }

}
