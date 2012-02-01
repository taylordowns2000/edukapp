package uk.ac.edukapp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uk.ac.edukapp.model.Useraccount;

public class JspUtils {

  public static boolean isAuthenticated(HttpSession session,
      HttpServletRequest request, EntityManagerFactory emf) {

    String isAuthenticated = (String) session.getAttribute("authenticated");

    if (isAuthenticated != null && isAuthenticated.equals("true")) {
      return true;
    } else {
      // check if remember cookie is set
      Cookie[] cookies = request.getCookies();

      String token = null;
      if (cookies != null) {
        token = ServletUtils.getCookieValue(cookies, "edukapp-remember", null);
      }

      EntityManager em = emf.createEntityManager();

      if (token != null) {
        //cookie is there but need to check if is associated to a user
        Useraccount ua = null;
        em.getTransaction().begin();
        Query q = em.createQuery("SELECT u " + "FROM Useraccount u "
            + "WHERE u.token=?1");
        q.setParameter(1, token);
        try {
          ua = (Useraccount) q.getSingleResult();
        } catch (Exception e) {
          // user not found
        }
        if (ua != null) {
          session.setAttribute("authenticated", "true");
          session.setAttribute("logged-in-user", ua);
          return true;
        } else {
          return false;
        }
      } else {
        //no remember-me cookie found 
        return false;
      }
    }

  }
}
