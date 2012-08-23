package uk.ac.edukapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import uk.ac.edukapp.dao.SessionInfo;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.renderer.MetadataRenderer;

public class SessionServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Subject currentUser = SecurityUtils.getSubject();
		
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setAuthenticated(currentUser.isAuthenticated());
		
		Useraccount account = (Useraccount) currentUser.getPrincipal();
		if (account != null){
			sessionInfo.setUserName(account.getUsername());	
			sessionInfo.setUserId(account.getId());
		}
		
		MetadataRenderer.render(resp.getOutputStream(), sessionInfo);
	}
	

}
