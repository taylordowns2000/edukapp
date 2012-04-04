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
import uk.ac.edukapp.renderer.Renderer;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.util.Message;

/**
 * API for getting widgets
 * 
 * @author scottw
 * 
 */
public class WidgetServlet extends HttpServlet {

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

		String id = req.getParameter("id");
		String uri = req.getParameter("uri");

		if ((id == null || id.trim().length() == 0)
				&& (uri == null || uri.trim().length() == 0)) {
			resp.sendError(400, "id/uri is empty");
			return;
		}

		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());
		OutputStream out = resp.getOutputStream();

		if (method.equals("GET")) {

			String operation = req.getParameter("operation");
			if (operation == null || operation.trim().length() == 0) {
				resp.sendError(400, "operation is empty");
				return;
			}

			if (operation.equals("getWidget")) {

				Widgetprofile widgetProfile = null;

				ExtendedWidgetProfile extendedWidgetProfile = new ExtendedWidgetProfile();
				if (id != null && id.trim().length() > 0)
					widgetProfile = widgetProfileService
							.findWidgetProfileById(id);
				if (uri != null && uri.trim().length() > 0)
					widgetProfile = widgetProfileService
							.findWidgetProfileByUri(uri);

				if (widgetProfile != null)
					extendedWidgetProfile.setWidgetProfile(widgetProfile);
				if (widgetProfile != null)
					extendedWidgetProfile.setRenderInfo(Renderer
							.render(widgetProfile));
				if (widgetProfile != null) {
					ActivityService activityService = new ActivityService(
							getServletContext());
					extendedWidgetProfile.setUploadedBy(activityService
							.getUploadedBy(widgetProfile));
				}
				MetadataRenderer.render(out, extendedWidgetProfile);
			} else if (operation.equals("getName")) {

			} else if (operation.equals("getRender")) {

			} else if (operation.equals("getTags")) {

			} else if (operation.equals("getActivities")) {

			} else if (operation.equals("getDescription")) {

			} else if (operation.equals("getComments")) {

			} else if (operation.equals("getRatings")) {

			} else if (operation.equals("getRating")) {

			} else if (operation.equals("getStats")) {

			} else if (operation.equals("getActivity")) {

			}

		} else if (method.equals("POST")) {
			String operation = req.getParameter("operation");
			if (operation == null || operation.trim().length() == 0) {
				resp.sendError(400, "operation is empty");
				return;
			}
			if (operation.equals("update-description")) {
				String text = req.getParameter("text");
				if (text == null || text.trim().length() == 0) {
					resp.sendError(400, "text is empty");
					return;
				}

				Message msg = null;

				if (id != null && id.trim().length() > 0) {
					msg = widgetProfileService.updateDescription(id, text);
				}
				if (uri != null && uri.trim().length() > 0) {
					msg = widgetProfileService.updateDescription(uri, text);
				}
				MetadataRenderer.render(out, msg);

			} else if (operation.equals("add-tag")) {
				String tagtext = req.getParameter("tagtext");
				String tagid = req.getParameter("tagid");
				if ((tagtext == null || tagtext.trim().length() == 0)
						&& (tagid == null || tagid.trim().length() == 0)) {
					resp.sendError(400, "tagid/tagtext is empty");
					return;
				}

				Message msg = null;

				if (id != null && id.trim().length() > 0) {
					if (tagtext != null && tagtext.trim().length() > 0) {
						msg = widgetProfileService.addTag(id, tagtext);
					}
					if (tagid != null && tagid.trim().length() > 0) {
						msg = widgetProfileService.addTag(id, tagid);
					}
				}
				if (uri != null && uri.trim().length() > 0) {
					if (tagtext != null && tagtext.trim().length() > 0) {
						msg = widgetProfileService.addTag(uri, tagtext);
					}
					if (tagid != null && tagid.trim().length() > 0) {
						msg = widgetProfileService.addTag(uri, tagid);
					}
				}
				MetadataRenderer.render(out, msg);

			} else if (operation.equals("remove-tag")) {

			} else if (operation.equals("add-activity")) {

			} else if (operation.equals("remove-activity")) {

			} else if (operation.equals("add-comment")) {

			} else if (operation.equals("remove-comment")) {

			} else if (operation.equals("add-rating")) {

			} else if (operation.equals("remove-rating")) {

			} else if (operation.equals("add-comment")) {

				String userid = req.getParameter("userid");
				String text = req.getParameter("text");

				if (userid == null || userid.trim().length() == 0) {
					resp.sendError(400, "userid is empty");
					return;
				}
				if (text == null || text.trim().length() == 0) {
					resp.sendError(400, "text is empty");
					return;
				}

				Message msg = null;

				msg = widgetProfileService.addComment(id, text, userid);
				MetadataRenderer.render(out, msg);

			} else if (operation.equals("remove-comment")) {

			}
		} else if (method.equals("PUT")) {

		} else if (method.equals("DELETE")) {

		}

		out.flush();
		out.close();
	}

}
