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
import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.model.Comment;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.server.configuration.SpawsServerConfiguration;
import uk.ac.edukapp.server.configuration.StoreConfiguration;

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

	/**
	 * Publishes a user review to an LR Node using SPAWS
	 * @param review the review to publish
	 */
	private void publishUserReviewUsingSpaws(Userreview review){
		try {
			
			ParadataManager manager = SpawsServerConfiguration.getInstance().getParadataManager();
			
			//
			// Create actor information, including profile page url
			//
			Actor actor = new Actor(review.getUserAccount().getUsername());
			actor.setUrl(StoreConfiguration.getInstance().getLocation()+"/user/"+review.getUserAccount().getId());
			
			//
			// Create submission
			//
			ISubmission submission = new Submission(actor, new Review(review.getComment().getCommenttext()), review.getWidgetProfile().getWidId());
			submission.setSubmitter(SpawsServerConfiguration.getInstance().getSubmitter());
			
			//
			// Publish submission
			//
			ArrayList<ISubmission> submissions = new ArrayList<ISubmission>();
			submissions.add(submission);
			manager.publishSubmissions(submissions);
			
		} catch (Exception e) {
			logger.error("Problem publishing review to SPAWS server", e);
		}
	}
	
	/**
	 * Get external reviews for the widget posted on other sites, and add them to the list of reviews
	 * 
	 * @param profile the widget the review is for
	 * @param reviews the list of current reviews
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void getExternalReviews(Widgetprofile profile, List<Userreview> reviews) throws Exception{
		
		//
		//
		//
		ArrayList<Userreview> externalReviews = new ArrayList<Userreview>();
		
		//
		// Fetch the reviews
		// 
		ParadataManager manager = SpawsServerConfiguration.getInstance().getParadataManager();
		List<ISubmission> submissions = manager.getExternalSubmissions(profile.getWidId(), IReview.VERB);
		

		//
		// Check for cached reviews
		//
		Object obj = Cache.getInstance().get("externalReviews" + profile.getId());
		if (obj != null){
			externalReviews = (ArrayList<Userreview>) obj;
			logger.debug("using cached external reviews for widget "+profile.getId());
		} else {
			
			//
			// Create a transient Userreview object graph for each review, including
			// useraccount and accountinfo. This information isn't persisted, though it can
			// be cached
			//
			for (ISubmission submission: submissions){
				Userreview review = new Userreview();
				Comment comment = new Comment();
				comment.setCommenttext(submission.getAction().getContent());

				Useraccount user = new Useraccount();
				user.setUsername(submission.getActor().getName());

				Accountinfo accountInfo = new Accountinfo();
				accountInfo.setWebsite(submission.getActor().getUrl());
				user.setAccountInfo(accountInfo);

				review.setComment(comment);
				review.setWidgetProfile(profile);
				review.setTime(submission.getUpdated());
				review.setUserAccount(user);

				externalReviews.add(review);
			}
			//
			// Cache external reviews
			//
			Cache.getInstance().put("externalReviews"+profile.getId(), externalReviews, SpawsServerConfiguration.getInstance().getCacheDuration());
			logger.debug("caching external reviews for widget "+profile.getId());

		}

		//
		//  Merge
		//
		reviews.addAll(externalReviews);
	}

}
