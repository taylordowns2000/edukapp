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

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class SyndicationSchedulerTest {

  @Mock
  private ServletContext servletContext;

  @Mock
  private SyndicationService syndicationService;

  @InjectMocks
  private SyndicationScheduler scheduler;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
//    when(servletContext.getInitParameter(ConnectionListener.SYNDICATION_URLS_PARAM)).thenReturn("http://example.com");
    // explicit call to setter, because constructor of scheduler creates its own.
    scheduler.setSyndicationService(syndicationService);
  }

  @Test
  public void test() throws InterruptedException {
    // 100 ms between calls
    scheduler.setPeriod(100);
    scheduler.setDelay(0L);
    // Start the scheduler
    scheduler.startPeriodicSyndication();
    // Wait for 550 ms, more than enough for 5 delays of 100 ms
    // Expect 6 calls: 1 at the very beginning, and then 5 consecutive (with 100 ms in between)
    verify(syndicationService, timeout(550).times(6)).syncFeeds();
  }
}
