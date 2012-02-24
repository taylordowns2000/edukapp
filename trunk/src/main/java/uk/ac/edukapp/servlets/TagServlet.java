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

import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.TagService;

/**
 * Tags api endpoint
 * 
 * @author anastluc
 * @author scottw
 * 
 */
public class TagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		String tagid = req.getParameter("id");
		String operation = req.getParameter("operation");

		//
		// Check for required parameters
		//
		if (tagid == null || tagid.trim().length() == 0) {
			resp.sendError(400, "id is empty");
			return;
		}
		if (operation == null || operation.trim().length() == 0) {
			resp.sendError(400,"operation is empty");
			return;
		}

		//
		// Create a TagService instance
		//
		TagService tagService = new TagService(getServletContext());

		//
		// "Popular" is a "magic" tag name that returns the most popular tags
		//
		if (tagid.equalsIgnoreCase("popular")) {
			MetadataRenderer.render(resp.getOutputStream(),
					tagService.getPopularTags());
			return;
		}

		//
		// Get the tag
		//
		Tag tag = tagService.getTag(tagid);

		//
		// If it doesn't exist, return 404.
		//
		if (tag == null) {
			resp.sendError(404);
			return;
		}
		
		System.out.println("tag:"+tag.getTagtext()+", operation:"+operation);

		OutputStream out = resp.getOutputStream();
		if (operation.equals("getName")) {

			//
			// Render metadata
			//
			MetadataRenderer.render(out, tag);
			
		} else if (operation.equals("getWidgets")) {

			//
			// Find and render matching widgets
			//
			MetadataRenderer.render(out, tag.getWidgetprofiles());
		}
		out.flush();
		out.close();

	}

}
