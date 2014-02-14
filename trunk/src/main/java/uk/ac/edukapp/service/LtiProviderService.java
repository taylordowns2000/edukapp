package uk.ac.edukapp.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.LtiProvider;

public class LtiProviderService extends AbstractService {

	public LtiProviderService(ServletContext context){
		super(context);
	}
	
	public LtiProvider getLtiProviderForConsumerKey(String consumerKey){
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query query = em.createNamedQuery("ltiprovider.findByConsumerKey");
		query.setParameter("key", consumerKey);
		LtiProvider ltiProvider = null;
		try {
			List<?> r = query.getResultList();
			if ( !r.isEmpty()) {
				ltiProvider = (LtiProvider)r.get(0);
			}
		} catch (Exception e) {
			ltiProvider = null;
		}
		em.close();
		return ltiProvider;
	}
}
