package uk.ac.edukapp.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Useraccount;
/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    //get parameters
    String username = null;
    String email   = null;
    String password = null;
    
    username = request.getParameter("username");
    email = request.getParameter("email");
    password = request.getParameter("password");
    
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("edukapp");
    EntityManager em = factory.createEntityManager();

    /*-----------*/
    em.getTransaction().begin();

    Useraccount ua = new Useraccount();
    ua.setUsername(username);
    ua.setEmail(email);
    ua.setPassword(password);
    em.persist(ua);

    em.getTransaction().commit();
    /*----------*/

    em.close();
    factory.close();
    
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

}
