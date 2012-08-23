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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import org.apache.log4j.Logger;

import uk.ac.edukapp.model.SyndicatedWidgetprofile;
import uk.ac.edukapp.model.Widgetprofile;

public class SyndicationService extends AbstractService {

  private static final Logger logger = Logger.getLogger(SyndicationService.class);

  private List<URL> feedUrls = Collections.emptyList();
  private WidgetProfileService widgetProfileService;

  public SyndicationService() {
    super();
  }

  public SyndicationService(ServletContext ctx) {
    super(ctx);
    widgetProfileService = new WidgetProfileService(ctx);
  }

  public void syncFeeds() {
    for (URL url : feedUrls) {
      List<SyndicatedWidgetprofile> profiles = getWidgetsFromFeedUri(url);
      for (SyndicatedWidgetprofile p : profiles) {
        p = saveSyndicatedWidgetprofile(p);
        // TODO: create a UI to not import all gadgets automatically but make a selection
        widgetProfileService.createWidgetProfile(p.getUri(), p.getName(), p.getDescription(),
            p.getUri(), // TODO: replace with icon uri
            Widgetprofile.OPENSOCIAL_GADGET);
      }
    }
  }

  private SyndicatedWidgetprofile saveSyndicatedWidgetprofile(SyndicatedWidgetprofile p) {
    final EntityManager em = getEntityManagerFactory().createEntityManager();
    logger.info("Will persist SyndicatedWidgetprofile: " + p);

    // TODO: merge if exists already
    em.persist(p);
    return p;
  }

  private List<SyndicatedWidgetprofile> getWidgetsFromFeedUri(URL url) {
    SyndFeedInput input = new SyndFeedInput();
    try {
      final InputStreamReader reader = new InputStreamReader(url.openStream());
      SyndFeed feed = input.build(reader);
      return buildWidgets(feed);
    } catch (IOException e) {
      logger.error("while trying to download feed at url " + url, e);
    } catch (FeedException e) {
      logger.error("while parsing feed at url " + url, e);
    }
    return null;
  }

  private List<SyndicatedWidgetprofile> buildWidgets(SyndFeed feed) {
    List<SyndicatedWidgetprofile> resultList = new ArrayList<SyndicatedWidgetprofile>();
    for (Object entryObj : feed.getEntries()) {
      SyndEntry e = (SyndEntry) entryObj;
      logger.debug("Creating syndicatedwidgetprofile from feed entry: " + e);

      final SyndicatedWidgetprofile syndicatedWidgetprofile =
          new SyndicatedWidgetprofile(
              feed.getUri(),
              e.getTitle(),
              null, // TODO: which field is copyright?
              e.getAuthor(),
              e.getUri(),
              e.getDescription().getValue(),
              e.getUri(),
              e.getUpdatedDate(),
              e.getPublishedDate());
      resultList.add(syndicatedWidgetprofile);
    }
    return resultList;
  }

  public void setFeedUrls(List<URL> feedUrls) {
    this.feedUrls = feedUrls;
  }
}
