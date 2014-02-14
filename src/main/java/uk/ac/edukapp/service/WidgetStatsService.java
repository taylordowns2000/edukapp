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

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.bolton.spaws.ParadataManager;
import uk.ac.bolton.spaws.model.IStats;
import uk.ac.bolton.spaws.model.ISubmission;
import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.WidgetStats;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.server.configuration.SpawsServerConfiguration;
import uk.ac.edukapp.util.Message;

public class WidgetStatsService extends AbstractService {

	static Logger logger = Logger.getLogger(WidgetStatsService.class.getName());

	private UserRateService userRateService;

	public WidgetStatsService(ServletContext ctx) {
		super(ctx);
		this.userRateService = new UserRateService(ctx);
	}
	
	/**
	 * Get the current stats for a widget (including external stats)
	 * @param widgetProfile
	 * @return
	 */
	public WidgetStats getStats(Widgetprofile widgetProfile){
		return getStats(widgetProfile, true);
	}
	
	
	public void updateWidgetRatings(Widgetprofile widgetProfile) {
		WidgetStats widgetStats = null;
		
		Number averageRating = userRateService.getAverageRating(widgetProfile);
		Number totalRatings = userRateService.getRatingCount(widgetProfile);
		
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		widgetStats = em.find(WidgetStats.class, widgetProfile.getId());
		widgetStats.setAverageRating(averageRating);
		widgetStats.setTotalRatings(totalRatings.longValue());
		em.persist(em.merge(widgetStats));
		em.getTransaction().commit();
		em.close();
		
		Cache cache = Cache.getInstance();
		cache.put("widgetStats:" + widgetProfile.getId(), widgetStats, SpawsServerConfiguration.getInstance().getCacheDuration());
	}

	/**
	 * Get the current stats for a widget
	 * 
	 * @param widgetProfile
	 * @param includeExternalStats
	 * @return a WidgetStats object for this widget
	 */
	public WidgetStats getStats(Widgetprofile widgetProfile, boolean includeExternalStats) {

		WidgetStats widgetStats = null;

		Cache cache = Cache.getInstance();
		widgetStats = (WidgetStats) cache.get("widgetStats:"
				+ widgetProfile.getId());

		if (widgetStats == null) {
			logger.debug("creating new cached stats");
			EntityManager em = getEntityManagerFactory().createEntityManager();
			widgetStats = em.find(WidgetStats.class, widgetProfile.getId());
			em.close();

			//
			widgetStats.setAverageRating(userRateService.getAverageRating(widgetProfile));
			
			widgetStats.setTotalRatings(userRateService
					.getRatingCount(widgetProfile));
			
			//
			// Pull in external stats
			//
			if (SpawsServerConfiguration.getInstance().isEnabled() && includeExternalStats){
				widgetStats = includeExternalStats(widgetProfile, widgetStats);
			}

			//
			// Cache for one hour - it may make more sense to cache for longer
			//
			cache.put("widgetStats:" + widgetProfile.getId(), widgetStats, SpawsServerConfiguration.getInstance().getCacheDuration());
			
		} else {
			logger.debug("using cached stats");
		}

		return widgetStats;
	}
	
	/**
	 * Include external statistics as well as local statistics, where these are available
	 * @param widgetProfile the profile to match against
	 * @param widgetStats the initial (local) stats
	 * @return updated widget stats
	 */
	private WidgetStats includeExternalStats(Widgetprofile widgetProfile, WidgetStats widgetStats){
		try {
			ParadataManager paradataManager = SpawsServerConfiguration.getInstance().getParadataManager();
			for (ISubmission submission: paradataManager.getExternalStats(widgetProfile.getWidId())){
				IStats stats = (IStats)submission.getAction();
				widgetStats.setDownloads(widgetStats.getDownloads() + stats.getDownloads());
				widgetStats.setEmbeds(widgetStats.getEmbeds() + stats.getEmbeds());
				widgetStats.setViews(widgetStats.getViews() + stats.getViews());
			}
		} catch (Exception e) {
			logger.error("problem retrieving external stats using SPAWS", e);
		}
		
		return widgetStats;
	}
	
	
	public Message addToEmbeds ( Widgetprofile widgetProfile ) {
		Message m = new Message ();
		
		WidgetStats ws = widgetProfile.getWidgetStats();
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		ws.setEmbeds(ws.getEmbeds()+1);
		em.persist(em.merge(ws));
		em.getTransaction().commit();
		em.close();
		
		m.setMessage("OK");
		return m;
	}
	
	
	public Message addToViews ( Widgetprofile widgetProfile ) {
		Message m = new Message ();

		WidgetStats ws = widgetProfile.getWidgetStats();
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		ws.setViews(ws.getViews()+1);
		em.persist(em.merge(ws));
		em.getTransaction().commit();
		em.close();
		
		m.setMessage("OK");
		
		return m;
	}
	
	
	public Message addToDownloads ( Widgetprofile widgetProfile ) {
		Message m = new Message ();

		WidgetStats ws = widgetProfile.getWidgetStats();
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		ws.setDownloads(ws.getDownloads()+1);
		em.persist(em.merge(ws));
		em.getTransaction().commit();
		em.close();
		
		m.setMessage("OK");
		
		return m;	
	}



}
