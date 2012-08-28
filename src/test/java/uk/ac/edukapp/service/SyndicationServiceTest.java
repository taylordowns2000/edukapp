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

package uk.ac.edukapp.service;

import java.net.URL;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import uk.ac.edukapp.model.SyndicatedWidgetprofile;
import uk.ac.edukapp.model.Widgetprofile;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyndicationServiceTest {

  private ServletContext servletContext = mock(ServletContext.class);

  private SyndicationService syndicationService;

  private EntityManagerFactory emf = mock(EntityManagerFactory.class);

  private EntityManager entityManager = mock(EntityManager.class);

  private Query query = mock(Query.class);
  @Before
  public void before() {
    when(servletContext.getAttribute("emf")).thenReturn(emf);
    when(emf.createEntityManager()).thenReturn(entityManager);
    EntityTransaction transaction = mock(EntityTransaction.class);
    when(entityManager.getTransaction()).thenReturn(transaction);
    syndicationService = new SyndicationService(servletContext);
  }

  @Test
  public void test() {
    when(entityManager.createNamedQuery(any(String.class))).thenReturn(query);
    when(query.getSingleResult()).thenReturn(new Widgetprofile());
    final URL localUrl = getClass().getClassLoader().getResource("localAtomFeed.xml");
    syndicationService.setFeedUrls(Arrays.asList(localUrl));
    syndicationService.syncFeeds();
    verify(entityManager, atLeast(1)).persist(any(SyndicatedWidgetprofile.class)); // at least the SyndicatedWidgetprofile
        // But the WidgetprofileService calls persist() multiple times so no exact count can be given here.
  }
}
