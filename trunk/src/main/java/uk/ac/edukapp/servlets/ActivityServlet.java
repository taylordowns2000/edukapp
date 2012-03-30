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
import uk.ac.edukapp.service.ActivityService;
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
public class ActivityServlet extends HttpServlet {

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

		//
		// Create a AffordanceService instance
		//
		AffordanceService affordanceService = new AffordanceService(
				getServletContext());

		if (method.equals("GET")) {

			String operation = req.getParameter("operation");
			if (operation == null || operation.trim().length() == 0) {
				resp.sendError(400, "operation is empty");
				return;
			}

			if (operation.equals("all")) {
				MetadataRenderer.render(resp.getOutputStream(),
						affordanceService.getAllActivities());
				return;
			} else if (operation.equals("popular")) {
				MetadataRenderer.render(resp.getOutputStream(),
						affordanceService.getPopularActivities());
				return;
			} else if (operation.equals("getName")
					|| operation.equals("getWidgets")) {
				String activityid = req.getParameter("id");

				//
				// Check for required parameters
				//
				if (activityid == null || activityid.trim().length() == 0) {
					resp.sendError(400, "id is empty");
					return;
				}
				Activity activity = affordanceService.getActivity(activityid);
				OutputStream out = resp.getOutputStream();
				if (operation.equals("getName")) {
					//
					// Render metadata
					//
					MetadataRenderer.render(out, activity);
				} else if (operation.equals("getWidgets")) {
					//
					// Find and render matching widgets
					//
					MetadataRenderer.render(out, activity.getWidgetprofiles());
				}

				out.flush();
				out.close();
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
			OutputStream out = resp.getOutputStream();

			Message msg = affordanceService.insertActivity(text);

			MetadataRenderer.render(out, msg);
			out.flush();
			out.close();

		} else if (method.equals("DELETE")) {

		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
