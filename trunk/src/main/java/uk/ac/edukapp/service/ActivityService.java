/*
 *  (c) 2012 University of Bolton
 *  
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.model.Useractivity;
import uk.ac.edukapp.util.Message;

/**
 * Service for user activity (rating/updating/tagging/...)
 * 
 * DO NOT CONFUSE with widget activity (affordance)
 * 
 * @author scottw
 * 
 */
public class ActivityService extends AbstractService {

	final Logger logger = Logger.getLogger(ActivityService.class.getName());

	public ActivityService(ServletContext servletContext) {
		super(servletContext);
	}

	public List<Useractivity> getUploaded(Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query query = entityManager.createNamedQuery("Useractivity.uploaded");
		query.setParameter("objectId", widgetProfile.getId());
		@SuppressWarnings("unchecked")
		List<Useractivity> results = (List<Useractivity>) query.getResultList();
		entityManager.close();
		return results;
	}

	public Useraccount getUploadedBy(Widgetprofile widgetProfile) {
		List<Useractivity> activities = getUploaded(widgetProfile);
		if (activities.size() == 0)
			return null;
		Useractivity userActivity = activities.get(0);
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Useraccount userAccount = entityManager.find(Useraccount.class,
				userActivity.getSubjectId());
		entityManager.close();
		return userAccount;
	}

	public void addUserActivity(int subject_id, String activity, int object_id)
			throws Exception {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		entityManager.getTransaction().begin();
		Useractivity ua = new Useractivity();
		ua.setSubjectId(subject_id);
		ua.setActivity(activity);
		ua.setObjectId(object_id);
		java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
		ua.setTime(now);
		entityManager.persist(ua);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

}
