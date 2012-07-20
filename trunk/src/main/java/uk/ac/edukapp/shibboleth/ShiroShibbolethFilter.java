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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet filter that uses data from Shibboleth to authenticate a user against Shiro.
 * <ul>
 *   <li>Unauthenticated requests are redirected to a configurable Shibboleth endpoint</li>
 *   <li>Authenticated requests from users that are not found in the account store are responded with a 401 status
 *   code. (they should have been provisioned up front)</li>
 *   <li>Authenticated requests that are known locally can pass through unmodified (e.g. their security context is
 *   set without user agent interaction)</li>
 * </ul>
 * @author Geert van der Ploeg
 */
public class ShiroShibbolethFilter implements Filter {

  Logger LOG = LoggerFactory.getLogger(ShiroShibbolethFilter.class);

  protected static String shibbolethTriggerUrl = "/Shibboleth.sso/Login";

  public void init(FilterConfig filterConfig) throws ServletException {
    final String configuredTriggerUrl = filterConfig.getInitParameter("shibbolethTriggerUrl");
    if (!StringUtils.isEmpty(configuredTriggerUrl)) {
      shibbolethTriggerUrl = configuredTriggerUrl;
    }
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    LOG.debug("Hitting ShiroShibbolethFilter");
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    final String remoteUser = httpRequest.getHeader("REMOTE_USER");

    if (requiresAuthentication(httpRequest)) {
      LOG.debug("Redirecting to Shibboleth trigger URL: {}", shibbolethTriggerUrl);
      httpResponse.sendRedirect(shibbolethTriggerUrl);
      return;
    } else {
      LOG.debug("Remote user: {}", remoteUser);

      final ShibbolethToken token = buildShibbolethToken(httpRequest);
      Subject currentUser = SecurityUtils.getSubject();
      try {
        currentUser.login(token);
        LOG.debug("Authorized user locally: {}", currentUser);
      } catch (AuthenticationException e) {
        LOG.info("User cannot be authenticated. Probably not provisioned yet? Will respond with 401.", e);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unknown user");
        return;
      }
    }
    chain.doFilter(request, response);
  }


  protected ShibbolethToken buildShibbolethToken(HttpServletRequest httpRequest) {
    Map<String, String> assertions = new HashMap<String, String>();
    assertions.put("mail", httpRequest.getHeader("mail"));
    assertions.put("cn", httpRequest.getHeader("cn"));
    return new ShibbolethToken(httpRequest.getHeader("REMOTE_USER"), assertions);
  }

  protected boolean requiresAuthentication(HttpServletRequest httpRequest) {
    return StringUtils.isBlank(httpRequest.getHeader("REMOTE_USER"));
  }

  public void destroy() {
  }
}
