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

import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.AffordanceService;
import uk.ac.edukapp.service.TagService;
import uk.ac.edukapp.util.Message;

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

		String method = req.getParameter("method");
		if (method == null || method.trim().length() == 0) {
			resp.sendError(400, "method is empty");
			return;
		}

		//
		// Create a TagService instance
		//
		TagService tagService = new TagService(getServletContext());

		OutputStream out = resp.getOutputStream();

		if (method.equals("GET")) {

			String operation = req.getParameter("operation");
			if (operation == null || operation.trim().length() == 0) {
				resp.sendError(400, "operation is empty");
				return;
			}

			if (operation.equals("all")) {
				// TODO - to implemet in TAgService
				// MetadataRenderer.render(resp.getOutputStream(),
				// tagService.getAllTags());
				Message msg = new Message();
				msg.setMessage("not yet implemeted");
				MetadataRenderer.render(out, msg);
				return;
			} else if (operation.equals("popular")) {
				MetadataRenderer.render(out, tagService.getPopularTags());
				return;
			} else if (operation.equals("getName")
					|| operation.equals("getWidgets")) {
				String tagid = req.getParameter("id");

				//
				// Check for required parameters
				//
				if (tagid == null || tagid.trim().length() == 0) {
					resp.sendError(400, "id is empty");
					return;
				}
				Tag tag = tagService.getTag(tagid);

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

			}

		} else if (method.equals("POST")) {

		} else if (method.equals("PUT")) {

			String text = req.getParameter("text");

			//
			// Check for required parameters
			//
			if (text == null || text.trim().length() == 0) {
				resp.sendError(400, "text is empty");
				return;
			}

			Message msg = tagService.insertTag(text);
			// Message msg = new Message();
			// msg.setMessage("not yet implemeted");

		} else if (method.equals("DELETE")) {

		}

		out.flush();
		out.close();

	}

}
