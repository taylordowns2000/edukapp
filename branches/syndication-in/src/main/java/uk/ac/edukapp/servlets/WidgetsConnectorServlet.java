package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.WidgetProfileService;

public class WidgetsConnectorServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		OutputStream out = resp.getOutputStream();

		//
		// Return full metadata
		//
		WidgetProfileService wps = new WidgetProfileService(getServletContext());
		List<Widgetprofile> allWidgets = wps.getAllWidgets();

		MetadataRenderer.render(out, allWidgets);

		out.flush();
		out.close();
	}

}
