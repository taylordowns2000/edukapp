package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.MetadataRenderer;
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
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(req.getServletContext());
		List<Widgetprofile> widgetProfiles = widgetProfileService.findWidgetProfile(query, "en", 10, 0);
		
		for (Widgetprofile widgetProfile:widgetProfiles){
			resp.getWriter().append(MetadataRenderer.render(widgetProfile));
		}
		resp.getWriter().flush();
	}
	
	

}
