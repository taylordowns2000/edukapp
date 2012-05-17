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

import uk.ac.edukapp.model.WidgetStats;
import uk.ac.edukapp.model.Widgetprofile;

public class WidgetStatsService extends AbstractService{
	
	private UserRateService userRateService;
	
	public WidgetStatsService(ServletContext ctx) {
		super(ctx);
		this.userRateService = new UserRateService(ctx);
	}
	
	/**
	 * Get the current *LOCAL* stats for a widget
	 * In future we'll use SPAWS to collate this with other edukapp instances
	 * @param widgetProfile
	 * @return
	 */
	public WidgetStats getStats(Widgetprofile widgetProfile){
		EntityManager em = getEntityManagerFactory().createEntityManager();
		WidgetStats widgetStats = em.find(WidgetStats.class, widgetProfile.getId());
		em.close();
		
		widgetStats.setAverageRating(userRateService.getAverageRating(widgetProfile));
		widgetStats.setTotalRatings(userRateService.getRatingCount(widgetProfile));
		
		return widgetStats;
	}

}
