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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;

public class UserReviewService extends AbstractService {
	
	public UserReviewService(ServletContext ctx){
		super(ctx);
	}
	
	public void publishUserReview(Useraccount userAccount, byte rating, Widgetprofile widgetProfile){
		Userreview userReview = new Userreview();
		userReview.setRating(rating);
		userReview.setWidgetProfile(widgetProfile);
		userReview.setUserAccount(userAccount);
		
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(userReview);
		entityManager.getTransaction().commit();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Userreview> getUserReviewsForWidgetProfile(Widgetprofile widgetProfile){
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		Query wpQuery = entityManager.createNamedQuery("Userreview.findForWidgetProfile");
		wpQuery.setParameter("widgetprofile", widgetProfile);
		List<Userreview> reviews = (List<Userreview>)wpQuery.getResultList();
		return reviews;
	}

}
