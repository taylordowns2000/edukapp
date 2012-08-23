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

package uk.ac.edukapp.model;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EntityManagerTest {
  private static final Logger logger = Logger.getLogger(EntityManagerTest.class);

  /**
   * Check that the JPA configuration is valid.
   */
  @Test
  public void testJPAConfiguration() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("edukapp");
    final EntityManager em = emf.createEntityManager();
  }

  /**
   * Try to persist an entity
   */
  @Test
  public void persist() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("edukapp");
    final EntityManager em = emf.createEntityManager();
    final SyndicatedWidgetprofile syndicatedWidgetprofile = new SyndicatedWidgetprofile("", "", "", "", "", "", "",
        new Date(), new Date());
    em.persist(syndicatedWidgetprofile);
    logger.debug("Persisted entity's id: " + syndicatedWidgetprofile.getId());
    assertTrue(syndicatedWidgetprofile.getId() > 0);
  }
}
