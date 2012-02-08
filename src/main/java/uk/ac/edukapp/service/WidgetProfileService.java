package uk.ac.edukapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.repository.Widget;

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

/**
 * Services for Widget Profiles
 * @author scott.bradley.wilson@gmail.com
 */
public class WidgetProfileService extends AbstractService{
	
	public WidgetProfileService(ServletContext ctx){
		super(ctx);
	}
	
	public List<Widgetprofile> findWidgetProfile(String query, String language, int rows, int offset){
		WidgetService widgetService = new WidgetService();
		List<Widget> widgets = widgetService.findWidgets(query, language, rows, offset);
		List<Widgetprofile> widgetProfiles = new ArrayList<Widgetprofile>();
		
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		
		//
		// For each Widget returned from the search index, correlate with
		// an Edukapp WidgetProfile, if one exists. Otherwise, construct a
		// new profile for it and persist it.
		//
		for (Widget widget:widgets){
			Widgetprofile widgetProfile = null;
			
			//
			// Find matching profile
			// TODO replace with a named query
			//
			try {
				widgetProfile = (Widgetprofile) entityManager.createQuery("SELECT w FROM Widgetprofile w WHERE w.widId = '"+widget.getUri()+"'").getSingleResult();
			} catch (NoResultException e) {
				
				//
				// Create a new profile
				//
				widgetProfile = new Widgetprofile();
				widgetProfile.setName(widget.getTitle());
				widgetProfile.setWidId(widget.getUri());
				entityManager.persist(widgetProfile);
			}
			
			widgetProfiles.add(widgetProfile);
		}
		
		entityManager.getTransaction().commit();
		
		return widgetProfiles;
	}

}
