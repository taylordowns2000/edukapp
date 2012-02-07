package uk.ac.edukapp.listeners;

import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import uk.ac.edukapp.model.Useraccount;
 
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

