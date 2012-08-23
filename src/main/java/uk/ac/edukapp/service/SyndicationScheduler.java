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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Service for periodic synchronisation with remote widget publishers
 */
public class SyndicationScheduler {

  private static final Logger logger = Logger.getLogger(SyndicationScheduler.class);

  private static final long period = 1000*60; // every minute

  private ServletContext servletContext;
  private Timer timer;
  private SyndicationService syndicationService;

  public SyndicationScheduler(ServletContext servletContext) {
    this.servletContext = servletContext;
    syndicationService = new SyndicationService(servletContext);
    timer = new Timer();

    final String syndicationUrlsConfig = servletContext.getInitParameter("syndicationUrls");
    if (StringUtils.isNotBlank(syndicationUrlsConfig)) {
      initWithConfig(syndicationUrlsConfig);
    }
  }

  private void initWithConfig(String syndicationUrlsConfig) {
    final String[] syndicationUrls = syndicationUrlsConfig.split(",");

    List<URL> urls = new ArrayList<URL>();
    for (String urlAsString : syndicationUrls) {
      try {
        URL u = new URL(urlAsString);
        urls.add(u);
      } catch (MalformedURLException e) {
        logger.error("While initializing syndicationServer with feed url '" + urlAsString + "'", e);
      }
    }
    syndicationService.setFeedUrls(urls);
  }

  public void startPeriodicSyndication() {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        syndicationService.syncFeeds();
      }
    }, 0L, period);
    logger.info("Started periodic syndication task. In-between delay: " + period);
  }


  public void stopSchedule() {
    timer.cancel();
    logger.info("Stopped periodic syndication task.");
  }

}
