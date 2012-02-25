package uk.ac.edukapp.listeners;

import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.security.SecurityRealm;
import uk.ac.edukapp.service.UserAccountService;
 
public class ConnectionListener implements ServletContextListener {

    //init EntityManagerFactory and store it in servlet context
    public void contextInitialized(ServletContextEvent e) {        
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("edukapp");
        
        e.getServletContext().setAttribute("emf", emf);     
        
    }
 
    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf =
            (EntityManagerFactory)e.getServletContext().getAttribute("emf");
        emf.close();
    }
}

