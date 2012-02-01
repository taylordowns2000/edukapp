package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.util.MD5Util;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public RegisterServlet() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String username = null;
    String email = null;
    String password = null;

    username = request.getParameter("username");
    email = request.getParameter("email");
    password = request.getParameter("password");
    
    // TO-DO add salt 
    //need to store it in db -> need to alter table/entity
    //String salt = null;
    
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
    String hashedPassword = MD5Util.md5Hex(password+salt);
    ua.setPassword(hashedPassword);
    ua.setSalt(salt);
    em.persist(ua);

    em.getTransaction().commit();
    /*----------*/

    em.close();
    factory.close();
    
    doForward(request, response, "/index.jsp");
    
  }
  
  private void doForward(HttpServletRequest request, HttpServletResponse response, String jsp) throws ServletException, IOException{
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
    dispatcher.forward(request, response);
  }

}
