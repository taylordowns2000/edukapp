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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * Login servlet using Apache Shiro
 * @author scottw
 *
 */
public class ShiroLoginServlet extends HttpServlet{

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 
		// Get auth parameters and construct credentials token
		//
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    String rememberMe = request.getParameter("rememberMe");
	    
	    UsernamePasswordToken token = new UsernamePasswordToken( username, password );
	    
	    //
	    // Get Shiro to turn on "remember me" if user selects it
	    //
	    if (rememberMe !=null && rememberMe.equalsIgnoreCase("true")){
	    	token.setRememberMe(true);
	    }
	    
	    Subject currentUser = SecurityUtils.getSubject();
	    
        String from = request.getParameter("from");
	    try {
	    	//
	    	// Try to log in with supplied credentials
	    	//
			currentUser.login(token);
	        
	        if (from == null || from.equals("/login.jsp")){
	          response.sendRedirect("/index.jsp");
	        }else {
	          response.sendRedirect(from);
	        }
		} catch (AuthenticationException e) {
			//
			// Log in attempt failed
			//
			response.sendRedirect("/login.jsp?wrong=true&from="+from);
		}
	    
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	

}
