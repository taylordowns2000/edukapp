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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.bolton.spaws.ParadataManager;
import uk.ac.bolton.spaws.model.IReview;
import uk.ac.bolton.spaws.model.ISubmission;
import uk.ac.bolton.spaws.model.impl.Actor;
import uk.ac.bolton.spaws.model.impl.Review;
import uk.ac.bolton.spaws.model.impl.Submission;
import uk.ac.edukapp.model.Comment;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.server.configuration.SpawsServerConfiguration;

public class UserReviewService extends AbstractService {
	
	private Logger logger = Logger.getLogger(UserReviewService.class);

	public UserReviewService(ServletContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	public List<Userreview> getUserReviewsForWidgetProfile(
			Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query wpQuery = entityManager
				.createNamedQuery("Userreview.findForWidgetProfile");
		wpQuery.setParameter("widgetprofile", widgetProfile);
		List<Userreview> results = (List<Userreview>) wpQuery.getResultList();
		entityManager.close();
		
		List<Userreview> reviews = new ArrayList<Userreview>();
		reviews.addAll(results);
		
		if (SpawsServerConfiguration.getInstance().isEnabled()){
			try {
				getExternalReviews(widgetProfile, reviews);
			} catch (Exception e) {
				logger.warn("Problem loading external reviews using SPAWS:", e);
			}
		}
		
		return reviews;
	}

	public boolean publishUserReview(String text, Useraccount userAccount,
			Widgetprofile widgetProfile) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		entityManager.getTransaction().begin();

		//
		// Create the review
		//
		Userreview userReview = new Userreview();
		userReview.setUserAccount(userAccount);
		userReview.setWidgetProfile(widgetProfile);
		userReview.setTime(new Date());
		entityManager.persist(userReview);
		Comment comment = new Comment();
		comment.setCommenttext(text);
		entityManager.persist(comment);
		userReview.setComment(comment);

		entityManager.getTransaction().commit();
		
		//
		// Sync the review
		//
		if (SpawsServerConfiguration.getInstance().isEnabled())
			publishUserReviewUsingSpaws(userReview);
		
		return true;
	}

	private void publishUserReviewUsingSpaws(Userreview review){
		try {
			ParadataManager manager = SpawsServerConfiguration.getInstance().getParadataManager();
			ISubmission submission = new Submission(new Actor(review.getUserAccount().getUsername()), new Review(review.getComment().getCommenttext()), review.getWidgetProfile().getWidId());
			ArrayList<ISubmission> submissions = new ArrayList<ISubmission>();
			submissions.add(submission);
			manager.publishSubmissions(submissions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getExternalReviews(Widgetprofile profile, List<Userreview> reviews) throws Exception{
		ParadataManager manager = SpawsServerConfiguration.getInstance().getParadataManager();
		
		List<ISubmission> submissions = manager.getExternalSubmissions(profile.getWidId(), IReview.VERB);
		for (ISubmission submission: submissions){
			Userreview review = new Userreview();
			Comment comment = new Comment();
			comment.setCommenttext(submission.getAction().getContent());
			
			Useraccount user = new Useraccount();
			user.setUsername(submission.getActor().getName());
			// TODO add a URL property for profile page link
			
			review.setComment(comment);
			review.setWidgetProfile(profile);
			review.setTime(submission.getUpdated());
			review.setUserAccount(user);
			
			reviews.add(review);
		}
	}

}
