package uk.ac.edukapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Functionality;
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.WidgetFunctionality;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.util.Message;

public class FunctionalityService extends AbstractService {

	public FunctionalityService ( ServletContext ctx) {
		super(ctx);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Functionality>getAllFunctionalities() {
		List<Functionality> functionalities;
		functionalities = (List<Functionality>) Cache.getInstance().get("AllFunctionalties");
		if ( functionalities == null ) {
			EntityManager em = this.getEntityManagerFactory().createEntityManager();
			functionalities = em.createQuery("SELECT a FROM Functionality a").getResultList();
			Cache.getInstance().put("AllFunctionalities", functionalities);
			em.close();
		}
		return functionalities;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Functionality>getFunctionalitiesForLevel( int level ) {
		try {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query q = em.createNamedQuery("Functionality.findByLevel");
		q.setParameter("level", level);
		em.close();
		return q.getResultList();
		}
		catch ( NoResultException e ) {
			return new ArrayList<Functionality>();
		}
	}
	
	
	public Functionality getFunctionalityForId ( String id ) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Functionality functionality = em.find(Functionality.class, id );
		em.close();
		return functionality;
	}
	
	public WidgetFunctionality addFunctionalityToWidget ( Widgetprofile widgetProfile, Functionality functionality, int relevance ){
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		WidgetFunctionality widgetFunctionality = null;
		// check to see if it exists
		try {
			Query q = em.createNamedQuery("widget_functionality.select");
			q.setParameter("wid", widgetProfile);
			q.setParameter("fid", functionality);
			widgetFunctionality = (WidgetFunctionality) q.getSingleResult();
			if ( widgetFunctionality != null ) {
				widgetFunctionality.setRelevance(relevance);
			}
		}
		catch ( NoResultException exp ) {
			// this is good
			widgetFunctionality = new WidgetFunctionality();
			widgetFunctionality.setFunctionality(functionality);
			widgetFunctionality.setWidgetProfile(widgetProfile);
			widgetFunctionality.setRelevance(relevance);
			em.persist(widgetFunctionality);	
		}
		em.getTransaction().commit();
		em.close();
		return widgetFunctionality;
	}
	
	
	public Message removeFunctionalityFromWidget (Widgetprofile widgetProfile, Functionality functionality ){
 		
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Query q = em.createNamedQuery("widget_functionality.select");
		q.setParameter("wid", widgetProfile);
		q.setParameter("fid", functionality);

		WidgetFunctionality wf = (WidgetFunctionality) q.getSingleResult();
		Message msg = new Message();
		if ( wf != null ) {
			em.remove(wf);
			msg.setMessage("OK");
		}
		else {
			msg.setMessage("Link between widget: "+widgetProfile.getId()+" and functionality: "+functionality.getId()+" could not be deleted");
		}
		em.getTransaction().commit();
		em.close();
		return msg;
	}
	

	
	

}
