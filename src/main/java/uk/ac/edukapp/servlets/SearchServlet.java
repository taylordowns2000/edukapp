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

import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.service.WidgetProfileService;

/**
 * Search API endpoint
 * TODO replace with Wink/JAX-RS
 * @author scott.bradley.wilson@gmail.com
 *
 */
public class SearchServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String query = req.getParameter("q");
		
		int offset;
		int result_size;
		try {
			String nResults = req.getParameter("resultsize");
			if ( nResults == null || nResults.trim().length() == 0){
			    nResults = req.getParameter("rows");
			    if ( nResults == null || nResults.trim().length() == 0){
			        nResults = "10";
			    }
			} 
			result_size = Integer.parseInt(nResults);
			String start = req.getParameter("start");
			if (start == null || start.trim().length() == 0) start = "0";
			offset = Integer.parseInt(start);
		} catch (NumberFormatException e) {
			offset = 0;
			result_size = 10;
		}
		resp.setHeader("Content-Type", "application/json");

		WidgetProfileService widgetProfileService = new WidgetProfileService(getServletContext());
		SearchResults searchResults = widgetProfileService.searchWidgetProfilesOrderedByRelevance(query, "en", result_size, offset);

		OutputStream out = resp.getOutputStream();
		MetadataRenderer.render(out, searchResults);
		out.flush();
		out.close();
	}
	
	

}
