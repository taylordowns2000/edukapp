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

public class SimilarServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = req.getParameter("uri");
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(req.getServletContext());
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);
		List<Widgetprofile> widgetProfiles = widgetProfileService.findSimilarWidgetsProfiles(widgetProfile, "en");
		
		for (Widgetprofile similarWidgetProfile:widgetProfiles){
			resp.getWriter().append(MetadataRenderer.render(similarWidgetProfile));
		}
		resp.getWriter().flush();
	}
	
	

}
