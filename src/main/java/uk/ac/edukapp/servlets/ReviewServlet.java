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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.UserReviewService;
import uk.ac.edukapp.service.WidgetProfileService;

public class ReviewServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = req.getParameter("uri");
		if (uri != null && uri.trim().length() >0){
			//
			// Get widgetProfile
			//
			WidgetProfileService widgetProfileService = new WidgetProfileService(req.getServletContext());
			Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);
			if (widgetProfile == null){
				resp.sendError(404);
			} else {
				//
				// Get reviews
				//
				UserReviewService userReviewService = new UserReviewService(req.getServletContext());
				List<Userreview> reviews = userReviewService.getUserReviewsForWidgetProfile(widgetProfile); 
				OutputStream out = resp.getOutputStream();
				MetadataRenderer.render(out, reviews);
				out.flush();
				out.close();
			}

		}
	}

	
	
}
