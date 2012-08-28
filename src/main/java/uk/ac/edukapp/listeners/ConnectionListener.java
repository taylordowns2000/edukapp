package uk.ac.edukapp.listeners;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.edukapp.service.SyndicationScheduler;

public class ConnectionListener implements ServletContextListener {


  private static final String SYNDICATION_SCHEDULER_ATTRIBUTE = "syndicationScheduler";
  private static final String SYNDICATION_URLS_PARAM = "syndicationUrls";

  //init EntityManagerFactory and store it in servlet context
    public void contextInitialized(ServletContextEvent servletContextEvent) {
      final ServletContext servletContext = servletContextEvent.getServletContext();

      EntityManagerFactory emf = Persistence.createEntityManagerFactory("edukapp");
      servletContext.setAttribute("emf", emf);

      final String syndicationUrls = servletContext.getInitParameter(SYNDICATION_URLS_PARAM);
      if (syndicationUrls != null) {
        SyndicationScheduler syndicationScheduler = new SyndicationScheduler(servletContext);

        syndicationScheduler.initWithConfig(syndicationUrls);
        syndicationScheduler.startPeriodicSyndication();

        servletContext.setAttribute(SYNDICATION_SCHEDULER_ATTRIBUTE, syndicationScheduler);
      }
    }
 
    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf =
            (EntityManagerFactory)e.getServletContext().getAttribute("emf");
        emf.close();

      if (e.getServletContext().getInitParameter(SYNDICATION_URLS_PARAM) != null) {
        SyndicationScheduler syndicationScheduler = (SyndicationScheduler) e.getServletContext().getAttribute(SYNDICATION_SCHEDULER_ATTRIBUTE);
        syndicationScheduler.stopSchedule();
      }
    }
}

