package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.WidgetProfileService;

public class WidgetServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String id = req.getParameter("id");
		String uri = req.getParameter("uri");
		Widgetprofile widgetProfile = null;
		
		
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(req.getServletContext());
		if (id != null && id.trim().length() > 0) widgetProfile = widgetProfileService.findWidgetProfileById(id);
		if (uri != null && uri.trim().length() > 0) widgetProfile = widgetProfileService.findWidgetProfileByUri(uri);
		
		
		List<Widgetprofile> widgetProfiles = new ArrayList<Widgetprofile>();
		
		if (widgetProfile != null) widgetProfiles.add(widgetProfile);
		
		OutputStream out = resp.getOutputStream();
		MetadataRenderer.render(out, widgetProfiles);
		out.flush();
		out.close();
	}

	
	
}
