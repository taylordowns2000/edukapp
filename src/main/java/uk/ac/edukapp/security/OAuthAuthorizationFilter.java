/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package uk.ac.edukapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;

import org.apache.log4j.Logger;

import uk.ac.edukapp.model.LtiProvider;
import uk.ac.edukapp.service.LtiProviderService;

/**
 * OAuth Authorization Filter for API Requests
 * 
 * <p>
 * Two authorization models are supported:
 * </p>
 * 
 * <ul>
 * <li>Plain requests
 * <li>Signed requests
 * </ul>
 * 
 * <p>
 * Plain requests use an API key to access the API. The authorization method is
 * to check the API key supplied is valid.
 * </p>
 * 
 * <p>
 * Signed requests use an API secret to sign the request message. The
 * authorization method is to check the message signature validates by
 * correlating the request with the secret for the API key using the oAuth
 * 1.0 signature method.
 * </p>
 */
public class OAuthAuthorizationFilter implements Filter {

  static Logger _logger = Logger.getLogger(OAuthAuthorizationFilter.class.getName());

  private boolean useSignedMessaging = false;
  private OAuthValidator oAuthValidator;
  private ServletContext servletContext;

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    useSignedMessaging = Boolean.parseBoolean(filterConfig.getInitParameter("signed"));
    oAuthValidator = new SimpleOAuthValidator();
    servletContext = filterConfig.getServletContext();

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    //
    // Check for exceptions
    //
    if (isException((HttpServletRequest) request)) {
      chain.doFilter(request, response);
      return;
    }

    //
    // Choose the authorization method required
    //
    boolean isAuthorized = false;

    if (useSignedMessaging) {
      try {
        isAuthorized = this.isAuthorizedUsingSignedMessaging((HttpServletRequest) request);
      } catch (Exception e) {
        _logger.error(e);
      }
    } else {
      isAuthorized = this.isAuthorizedUsingPlainMessaging((HttpServletRequest) request);
    }
    
    //
    // return 403 if not authorised, otherwise continue
    //
    if (!isAuthorized) {
      ((HttpServletResponse) response)
          .sendError(HttpServletResponse.SC_FORBIDDEN);
    } else {
      chain.doFilter(request, response);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  /**
   * Returns true if the request is an exception - i.e. an open request type
   * against one of the mapped servlets
   * 
   * @param request
   * @return true if the request should be allowed as an exception, false if it
   *         should be processed normally
   */
  private boolean isException(HttpServletRequest request) {
    if (request.getServletPath().equalsIgnoreCase("flatpack")
        && request.getMethod().equals("GET"))
      return true;
    return false;
  }

  /**
   * Checks if the given request is accompanied by a valid API key
   * 
   * @param request
   * @return true if valid, otherwise false
   */
  private boolean isAuthorizedUsingPlainMessaging(HttpServletRequest request) {
    
    //
    // Verify the API key exists
    //
    String key = request.getParameter("api_key");
   
    //
    // No key
    //
    if (key == null || key.trim().equals("")) {
      _logger.info("No API key supplied");
      return false;
    }
    
    //
    // Look up the key
    //
    LtiProvider ltiProvider = getLtiProvider(key);
    
    if (ltiProvider == null){
      return false;
    } else {
    	return true;
    }

  }

  /**
   * Authorize request using message oAuth signing algorithm, also know  as
   * "two-legged oAuth"
   * @param request the request to be authorized
   * @return true if the message is authorized.
   * @throws Exception if attempts to authorize the message raise an exception, such as an invalid signature
   */
  private boolean isAuthorizedUsingSignedMessaging(HttpServletRequest request){
    try {
      
      //
      // Get oAuth wrapper for request
      //
      OAuthMessage message = getMessage(request, null);
      
      //
      // Obtain the API key
      //
      String consumerKey = message.getConsumerKey();
      
      //
      // If the API Key isn't registered, we can stop right
      // here
      //
      LtiProvider ltiProvider = getLtiProvider(consumerKey);
      if (ltiProvider == null){
        return false;
      }
      
      //
      // Obtain the secret associated with the key
      //
      String apiSecret = ltiProvider.getConsumerSecret();
      
      //
      // Create "blank" token providers. This is because we aren't using
      // oAuth to manage tokens, but just using the signature method
      //
      OAuthServiceProvider serviceProvider = new OAuthServiceProvider(null,
          null, null);
      OAuthConsumer consumer = new OAuthConsumer(null, consumerKey,
          apiSecret, serviceProvider);
      OAuthAccessor accessor = new OAuthAccessor(consumer);
      accessor.tokenSecret = "";

      //
      // Try to validate the message
      //
      oAuthValidator.validateMessage(message, accessor);
      
    } catch (Exception e) {
      //
      // Log the exception; typically this will be an invalid oAuth signature
      //
      _logger.error(e);
      return false;
    }

    //
    // No exceptions thrown, so message was valid
    //
    return true;
  }
  
  private LtiProvider getLtiProvider(String consumerKey){
      LtiProviderService ltiProviderService = new LtiProviderService(servletContext);
      LtiProvider ltiProvider = ltiProviderService.getLtiProviderForConsumerKey(consumerKey);
      
      if (ltiProvider == null){
        _logger.info("Invalid consumer key supplied:"+consumerKey);
        return null;
      }
      
      return ltiProvider;
  }

  /**
   * Wrap message in an OAuthMessage wrapper
   * @param request
   * @param URL
   * @return
   */
  public OAuthMessage getMessage(HttpServletRequest request, String URL) {
    if (URL == null) {
      URL = request.getRequestURL().toString();
    }
    int q = URL.indexOf('?');
    if (q >= 0) {
      URL = URL.substring(0, q);
    }
    return new HttpRequestMessage(request, URL);
  }

}
