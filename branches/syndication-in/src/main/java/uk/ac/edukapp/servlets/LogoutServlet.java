package uk.ac.edukapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

	  Subject currentUser = SecurityUtils.getSubject();
	  currentUser.logout();
	  response.sendRedirect("/index.jsp");
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	  doGet(request, response);
  }

}
