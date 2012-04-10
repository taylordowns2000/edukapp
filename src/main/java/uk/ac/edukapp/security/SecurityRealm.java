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

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userrole;
import uk.ac.edukapp.service.TagService;
import uk.ac.edukapp.service.UserAccountService;

/**
 * Edukapp security realm implementation
 * 
 * @author scottw
 * 
 */
public class SecurityRealm extends AuthorizingRealm {

	private UserAccountService userAccountService;
	static Logger logger = Logger.getLogger(SecurityRealm.class.getName());

	public SecurityRealm() {
		setName("SecurityRealm"); // This name must match the name in the User
									// class's getPrincipals() method
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("MD5");
		this.setCredentialsMatcher(matcher);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Useraccount user = userAccountService.getUserAccount(token
				.getUsername());
		if (user != null) {
			ByteSource salt = ByteSource.Util.bytes(user.getSalt());
			return new SimpleAuthenticationInfo(user, user.getPassword(), salt,
					getName());
		} else {
			return null;
		}
	}

	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// Long userId = (Long)
		// principals.fromRealm(getName()).iterator().next();
		// Useraccount user = userAccountService.getUserAccount(userId);
		Object u = principals.iterator().next();
		Useraccount user = null;
		if (u instanceof Useraccount) {
			// re-retrieve the useraccount instance as the one enclosed in
			// principal
			// does not carry roles in it
			logger.info("user is a user");
			int userid = ((Useraccount) u).getId();
			user = userAccountService.getUserAccount(userid);
		} else {
			logger.info("user is not user");
		}

		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// FIXME add roles
			// for (Role role : user.getRoles()) {
			// info.addRole(role.getName());
			// info.addStringPermissions(role.getPermissions());
			// }
			List<Userrole> roles = user.getRoles();
			logger.info("id:" + user.getId());
			logger.info("uname:" + user.getUsername());
			logger.info("email:" + user.getEmail());
			if (roles == null) {
				logger.info("roles is null");
			} else if (roles.size() == 0) {
				logger.info("has no roles");
			} else {
				for (Userrole role : roles) {
					logger.info("role:" + role.getRolename());
					info.addRole(role.getRolename());
					// info.addStringPermissions(role.getPermissions());
				}
			}
			return info;
		} else {
			return null;
		}

	}

	/**
	 * @return the userAccountService
	 */
	public UserAccountService getUserAccountService() {
		return userAccountService;
	}

	/**
	 * @param userAccountService
	 *            the userAccountService to set
	 */
	public void setUserAccountService(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}
}
