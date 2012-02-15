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

package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.ExtendedWidgetProfile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.WidgetProfileService;

/**
 * API for getting widgets
 * @author scottw
 *
 */
public class WidgetServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String id = req.getParameter("id");
		String uri = req.getParameter("uri");
		Widgetprofile widgetProfile = null;
		
		ExtendedWidgetProfile extendedWidgetProfile = new ExtendedWidgetProfile();
	
		//
		// Widget profile
		//
		WidgetProfileService widgetProfileService = new WidgetProfileService(req.getServletContext());
		if (id != null && id.trim().length() > 0) widgetProfile = widgetProfileService.findWidgetProfileById(id);
		if (uri != null && uri.trim().length() > 0) widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);
		if (widgetProfile != null) extendedWidgetProfile.setWidgetProfile(widgetProfile);
		
		//
		// Uploaded By
		//
		if (widgetProfile != null){
			ActivityService activityService = new ActivityService(req.getServletContext());
			extendedWidgetProfile.setUploadedBy(activityService.getUploadedBy(widgetProfile));
		}
		
		OutputStream out = resp.getOutputStream();
		MetadataRenderer.render(out, extendedWidgetProfile);
		out.flush();
		out.close();
	}

	
	
}
