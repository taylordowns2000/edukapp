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

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for ShiroShibbolethFilter
 *
 * @author Geert van der Ploeg
 */
public class ShiroShibbolethFilterTest {

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  FilterChain chain;

  @InjectMocks
  private ShiroShibbolethFilter filter;

  @Mock
  private org.apache.shiro.mgt.SecurityManager securityManager;

  @Before
  public void before() {
    filter = new ShiroShibbolethFilter();
    MockitoAnnotations.initMocks(this);
    SecurityUtils.setSecurityManager(securityManager);
  }

  @After
  public void cleanup() {
    // Cleanup stuff from the current test.
    ThreadContext.remove();
  }

  @Test
  public void notAuthenticated() throws Exception {
    when(request.getHeader("REMOTE_USER")).thenReturn(null);

    filter.doFilter(request, response, chain);

    // User should be redirected
    verify(response).sendRedirect("/Shibboleth.sso/Login");

    // Filter chain should stop processing
    verify(chain, never()).doFilter((ServletRequest) any(), (ServletResponse) any());

    // Shiro security manager should not create a subject
    verify(securityManager, never()).createSubject((SubjectContext) any());
  }

  @Test
  public void authenticatedByShib() throws Exception {
    when(request.getHeader("REMOTE_USER")).thenReturn("testuser");

    Subject s = mock(Subject.class);

    when(securityManager.createSubject((SubjectContext) any())).thenReturn(s);

    filter.doFilter(request, response, chain);

    // Chain should continue.
    verify(chain).doFilter(request, response);

    // Security manager should have created a subject
    verify(securityManager).createSubject((SubjectContext) any());

    // Authenticated user 'subject' should have its login() get called.
    verify(s).login((AuthenticationToken) any());
  }

  @Test
  public void authenticatedByShibButNotALocalUser() throws Exception {
    when(request.getHeader("REMOTE_USER")).thenReturn("testuser");

    Subject s = mock(Subject.class);
    reset(securityManager);
    when(securityManager.createSubject((SubjectContext) any())).thenReturn(s);

    doThrow(new UnknownAccountException("User not found (not really, this is a mocked exception)"))
        .when(s).login((AuthenticationToken) any());
    filter.doFilter(request, response, chain);

    // Should respond with an Unauthorized
    verify(response).sendError(eq(401), anyString());

    // Filter chain should stop processing
    verify(chain, never()).doFilter((ServletRequest) any(), (ServletResponse) any());

  }

}
