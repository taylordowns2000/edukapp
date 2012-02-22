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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.LtiRenderer;
import uk.ac.edukapp.service.WidgetProfileService;

/**
 *  Servlet for basic LTI embed requests
 *  
 * @author scottw
 * 
 */
public class LtiServlet extends HttpServlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, String[]> parameters = request.getParameterMap();

		//
		// We use this mapping as BasicLTI assumes one "tool provider" per
		// endpoint URL
		// We map the value onto the actual widget id (IRI)
		//
		String widgetId = getResourceId(request); 

		WidgetProfileService widgetProfileService = new WidgetProfileService(getServletContext());
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);

		//
		// Get the instance url
		//
		String widgetUrl = LtiRenderer.getInstance().render(widgetProfile, parameters);

		//
		// Redirect to the instance URL
		//
		response.sendRedirect(widgetUrl);

	}
	
	/**
	 * Utility method for identifying the resource part of the URL
	 * note that pathinfo starts with a / for some reason
	 * @param the request
	 * @return the resource name
	 */
	private static String getResourceId(HttpServletRequest request) {
		String path = request.getPathInfo(); // may be null, plain name or name plus
		// params
		if (path == null) {
			return null;
		}
		// extract REST name
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		int p = path.indexOf('/');
		if (p > 0) {
			path = path.substring(0, p); // name isolated
		}
		if (path != null)
			path = path.trim();
		return path;
	}

}
