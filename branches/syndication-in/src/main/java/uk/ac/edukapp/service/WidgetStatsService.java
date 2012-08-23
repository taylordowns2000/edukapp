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

public class WidgetStatsService extends AbstractService {

	static Logger logger = Logger.getLogger(WidgetStatsService.class.getName());

	private UserRateService userRateService;

	public WidgetStatsService(ServletContext ctx) {
		super(ctx);
		this.userRateService = new UserRateService(ctx);
	}

	/**
	 * Get the current stats for a widget
	 * 
	 * @param widgetProfile
	 * @return a WidgetStats object for this widget
	 */
	public WidgetStats getStats(Widgetprofile widgetProfile) {

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
			// TODO implement
			// widgetStats.setAverageRating(userRateService.getAverageRating(widgetProfile));
			
			widgetStats.setTotalRatings(userRateService
					.getRatingCount(widgetProfile));
			
			//
			// Share stats, and pull in external stats
			//
			if (SpawsServerConfiguration.getInstance().isEnabled()){
				publishStats(widgetProfile, widgetStats);
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
	
	private void publishStats(Widgetprofile widgetProfile, WidgetStats widgetStats){
		//
		// TODO
		//
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

}
