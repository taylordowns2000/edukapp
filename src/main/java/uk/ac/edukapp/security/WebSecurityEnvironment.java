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
package uk.ac.edukapp.security;

import javax.servlet.ServletContext;

import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.env.IniWebEnvironment;

import uk.ac.edukapp.service.UserAccountService;

/**
 * Web Security Environment for Shiro - injects UserAccountService
 * into SecurityReam on application start
 * @author scottw
 *
 */
public class WebSecurityEnvironment extends IniWebEnvironment {

	/* (non-Javadoc)
	 * @see org.apache.shiro.web.env.IniWebEnvironment#configure()
	 */
	@Override
	protected void configure() {
		super.configure();
		
		//
		// Set the user account service for the security realm
		//
		ServletContext context = getServletContext();
		UserAccountService userAccountService = new UserAccountService(context);
		for (Realm realm : ((RealmSecurityManager) getSecurityManager()).getRealms()){
			if (realm.getName().equals("SecurityRealm")){
				( (SecurityRealm)realm).setUserAccountService(userAccountService);
			}
		}
		
	}
	

}
