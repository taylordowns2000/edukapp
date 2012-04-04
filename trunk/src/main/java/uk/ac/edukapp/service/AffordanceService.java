/*  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.edukapp.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.util.Message;

/**
 * Tag service
 * 
 * @author anastluc
 * 
 */
public class AffordanceService extends AbstractService {

	static Logger logger = Logger.getLogger(AffordanceService.class.getName());

	public AffordanceService(ServletContext servletContext) {
		super(servletContext);
	}

	/**
	 * Get the most popular tags
	 * 
	 * @return a List of tags
	 */
	@SuppressWarnings("unchecked")
	public List<Activity> getPopularActivities() {

		List<Activity> activities;

		//
		// By preference we load this from the cache rather
		// than from JPA
		//
		activities = (List<Activity>) Cache.getInstance().get(
				"PopularActivities");

		if (activities == null) {
			EntityManager entityManager = getEntityManagerFactory()
					.createEntityManager();
			TypedQuery<Activity> query = entityManager.createNamedQuery(
					"Activity.popular", Activity.class);
			activities = query.setMaxResults(10).getResultList();
			entityManager.close();

			//
			// Add to cache with a max lifetime of one hour
			//
			Cache.getInstance().put("PopularActivities", activities, 3200);
			logger.debug("Loaded popular activities from JPA");
		} else {
			logger.debug("loaded popular activities from cache");
		}
		return activities;
	}

	/**
	 * Get the full set of activities
	 * 
	 * @return a List of Activity
	 */
	@SuppressWarnings("unchecked")
	public List<Activity> getAllActivities() {
		List<Activity> activities;

		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		activities = entityManager.createQuery("Select a from Activity a")
				.getResultList();

		return activities;
	}

	/**
	 * Get an activity
	 * 
	 * @param activityid
	 * @return
	 */
	public Activity getActivity(String activityid) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Activity activity = null;

		try {

			//
			// if activityid integer then get instance
			//
			int id = Integer.parseInt(activityid);
			activity = entityManager.find(Activity.class, id);
		} catch (NumberFormatException e) {

			//
			// if a string then use named query to get instance
			//
			TypedQuery<Activity> query = entityManager.createNamedQuery(
					"Activity.findByName", Activity.class);
			query.setParameter("activityname", activityid);
			activity = query.getSingleResult();
		}

		//
		// Fetch widgetProfiles so these are available
		// in the detached entity
		//
		activity.getWidgetprofiles();

		entityManager.close();
		return activity;
	}

	/**
	 * Insert an activity
	 * 
	 * @param activitytext
	 * @return
	 */
	public Message insertActivity(String activitytext) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();

		// check if the activity text already exists
		// any better way to do this?
		TypedQuery<Activity> query = entityManager.createNamedQuery(
				"Activity.findByName", Activity.class);
		query.setParameter("activityname", activitytext);
		try {
			List<Activity> result = query.getResultList();// getSingleResult();
			if (result.size() > 0) {
				Message msg = new Message();
				msg.setMessage("exists");
				msg.setId("-1");
				return msg;
			}
		} catch (Exception e) {
			// if something goes wrong
			Message msg = new Message();
			msg.setMessage("error" + e.getMessage());
			return msg;
		}

		// execute the insert
		Activity activity = new Activity();
		activity.setActivitytext(activitytext);
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(activity);
			entityManager.getTransaction().commit();
			entityManager.close();

			Message msg = new Message();
			msg.setId("" + activity.getId());
			msg.setMessage("OK");

			return msg;
		} catch (Exception e) {
			// if something goes wrong
			Message msg = new Message();
			msg.setMessage("error" + e.getMessage());
			return msg;
		}

	}
}
