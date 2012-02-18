package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.service.WidgetProfileService;

/**
 * Similar API endpoint
 * 
 * TODO replace with Wink/JAX-RS
 * 
 * @author scott.bradley.wilson@gmail.com
 * 
 */
public class UpdateWidgetServlet extends HttpServlet {

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

		// getParameters
		String id = req.getParameter("id");
		String operation = req.getParameter("operation");
		String newText = req.getParameter("newText");

		PrintWriter out = resp.getWriter();

		if (id == null || id.trim().length() == 0) {
			resp.sendError(400);
			return;
		}

		if (operation != null) {

			if (operation.equals("description")) {
				if (newText != null && newText.trim().length() != 0) {
					// update description
					WidgetProfileService widgetProfileService = new WidgetProfileService(
							req.getServletContext());
					try {
						widgetProfileService.updateWidgetprofileDescription(id,
								newText);
					} catch (PersistenceException pe) {
						pe.printStackTrace();
						out.print("update failed");
					} catch (Exception e) {
						e.printStackTrace();
						out.print("update failed");
					}
					out.print("update done");
				}
			} else if (operation.equals("add-tag")) {

			} else if (operation.equals("remove-tag")) {

			} else if (operation.equals("add-use")) {

			} else if (operation.equals("remove-tag")) {

			} else if (operation.equals("set-featured")) {

			} else if (operation.equals("unset-featured")) {

			} else if (operation.equals("unset-featured")) {

			} else if (operation.equals("add-activity")) {

			} else if (operation.equals("remove-activity")) {

			} else if (operation.equals("name")) {

			} else {
				out.print("operation not valid");
			}
		}

	}
}
