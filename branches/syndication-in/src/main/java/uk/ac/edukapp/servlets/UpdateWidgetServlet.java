package uk.ac.edukapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateWidgetServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//
		// // getParameters
		// String id = req.getParameter("id");
		// String operation = req.getParameter("operation");
		// String newText = req.getParameter("newText");
		// String newTag = req.getParameter("newTag");
		//
		// PrintWriter out = resp.getWriter();
		//
		// if (id == null || id.trim().length() == 0) {
		// resp.sendError(400);
		// return;
		// }
		//
		// if (operation != null) {
		//
		// if (operation.equals("description")) {
		// if (newText != null && newText.trim().length() != 0) {
		// // update description
		// WidgetProfileService widgetProfileService = new WidgetProfileService(
		// req.getServletContext());
		// try {
		// widgetProfileService.updateWidgetprofileDescription(id,
		// newText);
		// } catch (PersistenceException pe) {
		// pe.printStackTrace();
		// out.print("update failed");
		// } catch (Exception e) {
		// e.printStackTrace();
		// out.print("update failed");
		// }
		// out.print("update done");
		// }
		// } else if (operation.equals("add-tag")) {
		//
		// if (newTag != null && newTag.trim().length() != 0) {
		// // update description
		// WidgetProfileService widgetProfileService = new WidgetProfileService(
		// req.getServletContext());
		// try {
		// widgetProfileService.addTag(id, newText);
		// } catch (PersistenceException pe) {
		// pe.printStackTrace();
		// out.print("update failed" + pe.getMessage());
		// } catch (Exception e) {
		// e.printStackTrace();
		// out.print("update failed" + e.getMessage());
		// }
		// out.print("update done");
		// }
		//
		// } else if (operation.equals("remove-tag")) {
		//
		// } else if (operation.equals("add-use")) {
		//
		// } else if (operation.equals("remove-tag")) {
		//
		// } else if (operation.equals("set-featured")) {
		//
		// } else if (operation.equals("unset-featured")) {
		//
		// } else if (operation.equals("unset-featured")) {
		//
		// } else if (operation.equals("add-activity")) {
		//
		// } else if (operation.equals("remove-activity")) {
		//
		// } else if (operation.equals("name")) {
		//
		// } else {
		// out.print("operation not valid");
		// }
		// }

	}
}
