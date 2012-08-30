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
package uk.ac.edukapp.schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.bolton.spaws.ParadataManager;
import uk.ac.bolton.spaws.model.ISubmission;
import uk.ac.bolton.spaws.model.ISubmitter;
import uk.ac.bolton.spaws.model.impl.Stats;
import uk.ac.bolton.spaws.model.impl.Submission;
import uk.ac.edukapp.model.WidgetStats;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.server.configuration.SpawsServerConfiguration;
import uk.ac.edukapp.server.configuration.StoreConfiguration;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;

/**
 * Scheduler for syndicating statistical paradata for widgets
 */
public class StatisticsSyndicationScheduler {
	
	static Logger logger = Logger.getLogger(StatisticsSyndicationScheduler.class.getName());
	
	private ServletContext context;
	
	/**
	 * Create a new single-use scheduler that starts immediately
	 * @param context
	 */
	public StatisticsSyndicationScheduler(ServletContext context){
		this.context = context;
		Executors.newSingleThreadScheduledExecutor().schedule(publisher, 10, TimeUnit.SECONDS);
	}
	
	/**
	 * Create a recurring scheduler; the interval between runs is specified in days
	 * @param context
	 * @param days
	 */
	public StatisticsSyndicationScheduler(ServletContext context, int days){
		this.context = context;
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(publisher, 1, days, TimeUnit.DAYS);
	}
	
	final Runnable publisher = new Runnable(){
		public void run() {
			try {
				logger.info("Publishing all statistics: job started");
				
				//
				// Get current set of widgets
				//
				List<Widgetprofile> widgets = new WidgetProfileService(context).getAllWidgets();
				
				WidgetStatsService widgetStatsService = new WidgetStatsService(context);
				ArrayList<ISubmission> submissions = new ArrayList<ISubmission>();
				ISubmitter submitter = SpawsServerConfiguration.getInstance().getSubmitter();
				
				for (Widgetprofile widgetProfile: widgets){
					WidgetStats widgetStats = widgetStatsService.getStats(widgetProfile, false);
					//
					// Create stats object
					//
					Stats stats = new Stats();
					stats.setDownloads(widgetStats.getDownloads());
					stats.setEmbeds(widgetStats.getEmbeds());
					stats.setViews(widgetStats.getViews());
					//
					// Add context information for the stats - in this case the detail page. Probably need
					// some way of doing this that is more abstract and works with different front ends.
					//
					stats.setContextUrl(StoreConfiguration.getInstance().getLocation()+"/widget/"+widgetProfile.getId());
					
					//
					// Create submission and add it to the set to be published
					//
					ISubmission submission = new Submission(submitter, null, stats, widgetProfile.getWidId());
					submissions.add(submission);
				}
				
				//
				// Publish all stats
				//
				ParadataManager paradataManager = SpawsServerConfiguration.getInstance().getParadataManager();
				paradataManager.publishSubmissions(submissions);
				
				logger.info("Publishing all statistics: job completed. "+submissions.size()+ " paradata records uploaded");

				
			} catch (Exception e) {
				logger.error("Error publishing statistics", e);
			}
		}
	};

}
