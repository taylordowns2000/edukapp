package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.AccountInfoService;

public class AccountInfoServlet  extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = request.getParameter("id");
		if (id == null|| id.trim().length() == 0){
			//
			// Use current principal if logged in
			//
			
		}
		
		AccountInfoService accountInfoService = new AccountInfoService(getServletContext());
		Accountinfo accountInfo = accountInfoService.getAccountInfo(id);
		if (accountInfo == null){
			response.sendError(404);
			return;
		}
		
		OutputStream out = response.getOutputStream();
		MetadataRenderer.render(out, accountInfo);
		out.flush();
		out.close();
	}
	
	

}
