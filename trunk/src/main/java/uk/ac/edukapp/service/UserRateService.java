package uk.ac.edukapp.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userrating;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.util.Message;

public class UserRateService extends AbstractService {

	public UserRateService(ServletContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	public List<Userrating> getRatingsForWidgetProfile(
			Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query wpQuery = entityManager
				.createNamedQuery("Userrating.findForWidgetProfile");
		wpQuery.setParameter("widgetprofile", widgetProfile);
		List<Userrating> ratings = (List<Userrating>) wpQuery.getResultList();
		entityManager.close();
		return ratings;
	}

	public Number getAverageRating(Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query q = entityManager.createNamedQuery("Userrating.getAverageValue");
		q.setParameter("widgetprofile", widgetProfile);

		Number average = (Number) q.getSingleResult();
		if (average == null) average = 0.0;

		return average;
	}
	
	public Long getRatingCount(Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query q = entityManager.createNamedQuery("Userrating.getCount");
		q.setParameter("widgetprofile", widgetProfile);

		Long count = (Long) q.getSingleResult();

		return count;
	}

	public Message publishUserRate(String rating, Useraccount userAccount,
			Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();

		Message msg = new Message();

		try {
			entityManager.getTransaction().begin();

			//
			// check if user rating exists - if yes update
			//
			Userrating userRating = null;
			Query q = entityManager
					.createNamedQuery("Userrating.findForWidgetAndUser");
			q.setParameter("widgetprofile", widgetProfile);
			q.setParameter("useraccount", userAccount);

			List<Userrating> rats = (List<Userrating>) q.getResultList();
			if (rats.size() > 0) {
				userRating = rats.get(0);
			}

			//
			// Create the review
			//
			if (userRating == null) {
				userRating = new Userrating();
				userRating.setUserAccount(userAccount);
				userRating.setWidgetProfile(widgetProfile);
			}

			int rat = Integer.parseInt(rating);
			byte rate = (byte) rat;

			userRating.setRating(rate);
			userRating.setTime(new Date());

			entityManager.persist(userRating);

			entityManager.getTransaction().commit();

			msg.setMessage("OK");
			
			//
			// Remove cached stats for this widget profile
			//
			Cache.getInstance().remove("widgetStats:"+widgetProfile.getId());

		} catch (Exception e) {
			msg.setMessage("error:" + e.getMessage());
			e.printStackTrace();
		}
		return msg;

	}

}
