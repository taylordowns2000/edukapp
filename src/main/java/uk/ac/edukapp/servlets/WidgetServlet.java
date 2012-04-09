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

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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

		String part = req.getParameter("part");
		OutputStream out = resp.getOutputStream();
		
		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		if (widgetProfile == null) resp.sendError(HttpServletResponse.SC_NOT_FOUND);

		//
		// Return full metadata
		//
		if (part == null|| part.isEmpty()) {
			ExtendedWidgetProfile extendedWidgetProfile = new ExtendedWidgetProfile();
			extendedWidgetProfile.setWidgetProfile(widgetProfile);
			extendedWidgetProfile.setRenderInfo(Renderer.render(widgetProfile));
			ActivityService activityService = new ActivityService(getServletContext());
			extendedWidgetProfile.setUploadedBy(activityService.getUploadedBy(widgetProfile));
			MetadataRenderer.render(out, extendedWidgetProfile);
		}
		
		// Get parts
		
		else if (part.equals("name")){
			
		} 
		else if (part.equals("render")){
			
		}
		else if (part.equals("tags")){
			
		}
		else if (part.equals("activities")){
			
		}
		else if (part.equals("description")){
			
		}
		else if (part.equals("comments")){
			
		}
		else if (part.equals("ratings")){
			
		}
		else if (part.equals("rating")){
			
		}
		else if (part.equals("stats")){
			
		}
		else if (part.equals("activity")){
			
		}
		out.flush();
		out.close();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String body = IOUtils.toString(req.getInputStream());
		if (body == null || body.trim().length() == 0){
			resp.sendError(400, "no tag specified");
			return;
		}
		
		System.out.println("Message body ="+body);
		
		String part = req.getParameter("part");
		
		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		if (widgetProfile == null) resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		
		OutputStream out = resp.getOutputStream();

		WidgetProfileService widgetProfileService = new WidgetProfileService(getServletContext());
	
		if (part.equals("tag")){
			Message msg = null;
			msg = widgetProfileService.addTag(widgetProfile, body);
			MetadataRenderer.render(out, msg);
		}
		else if (part.equals("comment")){
			//
			// TODO
			//
			// 1. validate the userId
			// 2. check that the current shiro principal matches the user id
			// 3. Map the JSON onto an actual Comment bean or DTO
			Message msg = null;
			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(body);
			String userId = json.findValue("userId").asText();
			String text = json.findValue("comment").asText();
			
			//
			// TODO Create a direct method using the profile and a new comment object
			//
			msg = widgetProfileService.addComment(String.valueOf(widgetProfile.getId()), text, userId);
			MetadataRenderer.render(out, msg);
		}
		out.flush();
		out.close();
	}				

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String part = req.getParameter("part");
		
		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = getWidgetProfile(req);
		if (widgetProfile == null) resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(getServletContext());
		
		OutputStream out = resp.getOutputStream();
		
		if (part.equals("description")){
			// Update description
			
			String body = IOUtils.toString(req.getInputStream());
			if (body == null || body.trim().length() == 0){
				resp.sendError(400, "no tag specified");
				return;
			}

			Message msg = null;

			msg = widgetProfileService.updateDescription(widgetProfile.getWidId(), body);
			
			MetadataRenderer.render(out, msg);
			
		}
		out.flush();
		out.close();
	}


	private Widgetprofile getWidgetProfile(HttpServletRequest request){
		String id = request.getParameter("id");
		String uri = request.getParameter("uri");
		
		//
		// Validate
		//
		if ((id == null || id.trim().length() == 0)
				&& (uri == null || uri.trim().length() == 0)) {
			return null;
		}

		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());

		//
		// Get widget resource
		//
		Widgetprofile widgetProfile = null;
		if (id != null && id.trim().length() > 0)
			widgetProfile = widgetProfileService.findWidgetProfileById(id);
		if (uri != null && uri.trim().length() > 0)
			widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);
		
		return widgetProfile;
	}
}
