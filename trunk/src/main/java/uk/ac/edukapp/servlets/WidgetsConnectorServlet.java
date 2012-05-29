package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userrating;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.ExtendedWidgetProfile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.renderer.Renderer;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.UserRateService;
import uk.ac.edukapp.service.UserReviewService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;
import uk.ac.edukapp.util.Message;

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
