package uk.ac.edukapp.listeners;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.edukapp.service.SyndicationScheduler;

public class ConnectionListener implements ServletContextListener {


  private static final String SYNDICATION_SCHEDULER_ATTRIBUTE = "syndicationScheduler";

  //init EntityManagerFactory and store it in servlet context
    public void contextInitialized(ServletContextEvent servletContextEvent) {
      final ServletContext servletContext = servletContextEvent.getServletContext();

      EntityManagerFactory emf = Persistence.createEntityManagerFactory("edukapp");
      servletContext.setAttribute("emf", emf);

      final SyndicationScheduler syndicationScheduler = new SyndicationScheduler(servletContext);
      servletContext.setAttribute(SYNDICATION_SCHEDULER_ATTRIBUTE, syndicationScheduler);
      syndicationScheduler.startPeriodicSyndication();
    }
 
    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf =
            (EntityManagerFactory)e.getServletContext().getAttribute("emf");
        emf.close();

      SyndicationScheduler syndicationScheduler = (SyndicationScheduler) e.getServletContext().getAttribute(SYNDICATION_SCHEDULER_ATTRIBUTE);
      syndicationScheduler.stopSchedule();
    }
}

