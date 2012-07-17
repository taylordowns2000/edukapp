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

import org.apache.shiro.SecurityUtils;

import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.renderer.MetadataRenderer;
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

		//
		// Create a TagService instance
		//
		TagService tagService = new TagService(getServletContext());

		OutputStream out = resp.getOutputStream();

		String id = req.getParameter("id");
		
		if (id == null){
			
		} 
		
		else if (id.equals("all")){
			// TODO - to implemet in TAgService
			// MetadataRenderer.render(resp.getOutputStream(),
			// tagService.getAllTags());
			Message msg = new Message();
			msg.setMessage("not yet implemeted");
			MetadataRenderer.render(out, msg);
			return;
		} 
		
		else if (id.equals("popular")){
			MetadataRenderer.render(out, tagService.getPopularTags());
			return;			
		}
		else {
			Tag tag = tagService.getTag(id);
			
			if (req.getParameter("namesonly") != null){
				//
				// Render metadata
				//
				MetadataRenderer.render(out, tag);				
			} else {
				//
				// Find and render matching widgets
				//
				MetadataRenderer.render(out, tag.getWidgetprofiles());				
			}
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
		
		//
		// POST always requires a valid user login
		//
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		if (userAccount == null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		//
		// Create a TagService instance
		//
		TagService tagService = new TagService(getServletContext());

		OutputStream out = resp.getOutputStream();

		String text = req.getParameter("text");

		//
		// Check for required parameters
		//
		if (text == null || text.trim().length() == 0) {
			resp.sendError(400, "text is empty");
			return;
		}

		try {
			if (tagService.insertTag(text)){
				resp.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);				
			}
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		out.flush();
		out.close();

	}

}
