package uk.ac.edukapp.listeners;

import javax.persistence.*;
import javax.servlet.*;

import uk.ac.edukapp.schedulers.StatisticsSyndicationScheduler;
import uk.ac.edukapp.server.configuration.SpawsServerConfiguration;
 
public class ConnectionListener implements ServletContextListener {

    //init EntityManagerFactory and store it in servlet context
    public void contextInitialized(ServletContextEvent e) {        
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("edukapp");
        
        e.getServletContext().setAttribute("emf", emf);
        
        //
        // Start the stats syndication scheduler
        //
        if (SpawsServerConfiguration.getInstance().isEnabled())
            new StatisticsSyndicationScheduler(e.getServletContext(), SpawsServerConfiguration.getInstance().getInterval());
    }
 
    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf =
            (EntityManagerFactory)e.getServletContext().getAttribute("emf");
        emf.close();
    }
}

