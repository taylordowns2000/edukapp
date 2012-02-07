package uk.ac.edukapp.util;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uk.ac.edukapp.model.Accountinfo;
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
          
          //update last seen field in accountinfo table to NOW
          try {
            Accountinfo ai = (Accountinfo) em.createQuery(
                "SELECT a " + "FROM Accountinfo a WHERE a.id=?1")
                .setParameter(1, ua.getId())
                .getSingleResult();
            java.util.Date date = new java.util.Date();
            Timestamp now = new Timestamp(date.getTime());
            ai.setLastseen(now);

            em.persist(ai);
          } catch (Exception e) {

          }
          
          em.getTransaction().commit();
          
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
