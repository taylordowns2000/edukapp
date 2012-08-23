/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package uk.ac.edukapp.shibboleth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.service.UserAccountService;

/**
 * Realm for Shibboleth authenticated users.
 * Supports only ShibbolethToken tokens.
 *
 * @author Geert van der Ploeg
 */
public class ShibbolethRealm extends AuthorizingRealm {

  public static final String REALM_NAME = "shibbolethRealm";

  private UserAccountService userAccountService;

  public ShibbolethRealm() {
    setName(REALM_NAME);
  }

  /**
   * @throws UnsupportedOperationException until it's clear we need this at all.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    throw new UnsupportedOperationException("Not yet supported. Do we need this at all?");
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    ShibbolethToken shibToken = (ShibbolethToken) token;
    Useraccount user = userAccountService.getUserAccount(shibToken.getUsername());
    if (user != null) {
      return new SimpleAuthenticationInfo(user, token.getCredentials(), REALM_NAME);
    }
    return null;
  }

  @Override
  public Class getAuthenticationTokenClass() {
    // Only support ShibbolethToken objects.
    return ShibbolethToken.class;
  }

  /*
   * This class does not enforce credentials-matching: this is left to Shibboleth.
   * @return AllowAllCredentialsMatcher
   */
  @Override
  public CredentialsMatcher getCredentialsMatcher() {
    return new AllowAllCredentialsMatcher();
  }

  public void setUserAccountService(UserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }
}
