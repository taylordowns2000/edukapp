package uk.ac.edukapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.WidgetDescription;
import uk.ac.edukapp.model.WidgetStats;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.repository.Widget;
import uk.ac.edukapp.util.Message;

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

/**
 * Services for Widget Profiles
 * 
 * @author scott.bradley.wilson@gmail.com
 * @author kjpopat@gmail.com
 * 
 */
public class WidgetProfileService extends AbstractService {

	static Logger logger = Logger.getLogger(WidgetProfileService.class
			.getName());

	private PropertiesConfiguration properties;

	// popularity factors
	static int EmbedFactor = 1;
	static int DownloadFactor = 4;
	static int ViewFactor = 20;

	public WidgetProfileService(ServletContext ctx) {
		super(ctx);
		try {
			properties = new PropertiesConfiguration("store.properties");
			if (properties.containsKey("popularity.factor.embeds")) {
				EmbedFactor = properties.getInt("popularity.factor.embeds");
			}
			if (properties.containsKey("popularity.factor.downloads")) {
				DownloadFactor = properties
						.getInt("popularity.factor.downloads");
			}
			if (properties.containsKey("popularity.factor.views")) {
				ViewFactor = properties.getInt("popularity.factor.views");
			}
		} catch (ConfigurationException e) {
			logger.debug(e.toString());
			// leave defaults
		}
	}

	public List<Widgetprofile> getAllWidgets() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		// em.getTransaction().begin();
		Query q = em.createQuery("SELECT w FROM Widgetprofile w ");
		List<Widgetprofile> results = q.getResultList();
		return results;
	}

	/**
	 * Create a widget profile and save it
	 * 
	 * @param uri
	 * @param name
	 * @param description
	 * @return the widget profile
	 */
	public Widgetprofile createWidgetProfile(String uri, String name,
			String description, String icon, String type) {

		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Widgetprofile widgetprofile = new Widgetprofile();
		widgetprofile.setName(name);
		widgetprofile.setCreated(new Date());
		widgetprofile.setUpdated(new Date());
		widgetprofile.setIcon(icon);
		byte zero = 0;
		byte one = 1;
		if (type.equals(Widgetprofile.W3C_WIDGET)){
			widgetprofile.setW3cOrOs(zero);
		} else {
			widgetprofile.setW3cOrOs(one);			
		}
		widgetprofile.setWidId(uri);
		em.persist(widgetprofile);

		WidgetDescription wd = new WidgetDescription();
		wd.setDescription(description);
		wd.setWid_id(widgetprofile.getId());
		em.persist(wd);
		
		//
		// Create the widget stats
		//
		WidgetStats widgetStats = new WidgetStats();
		widgetStats.setWid_id(widgetprofile.getId());
		widgetStats.setDownloads(0);
		widgetStats.setEmbeds(0);
		widgetStats.setViews(0);
		em.persist(widgetStats);
		
		em.getTransaction().commit();
		em.close();

    //
    // Update the index
    //
    SolrConnector.getInstance().index(widgetprofile, "en");

    return widgetprofile;

	}

	public Widgetprofile updateWidgetProfile(String uri, String name,
			String description) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Query wpQuery = em.createNamedQuery("Widgetprofile.findByUri");
		wpQuery.setParameter("uri", uri);
		Widgetprofile widgetprofile = (Widgetprofile) wpQuery.getSingleResult();
		if (widgetprofile != null) {
			widgetprofile.setName(name);
			widgetprofile.setUpdated(new Date());
			WidgetDescription desc = widgetprofile.getDescription();
			if (desc == null) {
				desc = new WidgetDescription();
				desc.setWid_id(widgetprofile.getId());
				widgetprofile.setDescription(desc);
			}
			desc.setDescription(description);
			em.persist(em.merge(widgetprofile));
		}
		em.getTransaction().commit();
		return widgetprofile;
	}

	public List<Widgetprofile> findWidgetProfilesForTag(Tag tag) {
		return tag.getWidgetprofiles();
	}

	public List<Widgetprofile> findWidgetProfilesForActivity(Activity activity) {
		return activity.getWidgetprofiles();
	}

	/**
	 * Get the list of featured widgets
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Widgetprofile> findFeaturedWidgetProfiles() {

		List<Widgetprofile> widgetProfiles;

		//
		// Obtain cached version where available
		//
		widgetProfiles = (List<Widgetprofile>) Cache.getInstance().get(
				"FeaturedWidgets");

		if (widgetProfiles == null) {
			EntityManager entityManager = getEntityManagerFactory()
					.createEntityManager();
			Query wpQuery = entityManager
					.createNamedQuery("Widgetprofile.featured");
			widgetProfiles = (List<Widgetprofile>) wpQuery.getResultList();
			entityManager.close();
			Cache.getInstance().put("FeaturedWidgets", widgetProfiles, 3200);
			logger.debug("Loaded featured widgets from JPA");
		} else {
			logger.debug("Loaded featured widgets from cache");
		}

		return widgetProfiles;
	}

	// comparator for rating
	static final Comparator<Widgetprofile> RATING_ORDER = new Comparator<Widgetprofile>() {
		public int compare(Widgetprofile wp1, Widgetprofile wp2) {
			int wp1rating = wp1.getWidgetStats().getAverageRating().intValue();
			int wp2rating = wp2.getWidgetStats().getAverageRating().intValue();
			if (wp1rating == wp2rating) {
				return 0;
			}
			return (wp1rating < wp2rating) ? -1 : 1;
		}
	};

	// comparator for date
	static final Comparator<Widgetprofile> DATE_ORDER = new Comparator<Widgetprofile>() {
		public int compare(Widgetprofile wp1, Widgetprofile wp2) {
			return wp2.getUpdated().compareTo(wp1.getUpdated());
		}
	};

	// comparator for popularity
	// number of times downloaded + number of times embedded

	static final Comparator<Widgetprofile> POPULARITY_ORDER = new Comparator<Widgetprofile>() {
		public int compare(Widgetprofile wp1, Widgetprofile wp2) {
			WidgetStats ws1 = wp1.getWidgetStats();
			WidgetStats ws2 = wp2.getWidgetStats();
			float wp1f = factorValue(ws1.getEmbeds(), EmbedFactor)
					+ factorValue(ws1.getDownloads(), DownloadFactor)
					+ factorValue(ws1.getViews(), ViewFactor);
			float wp2f = factorValue(ws2.getEmbeds(), EmbedFactor)
					+ factorValue(ws2.getDownloads(), DownloadFactor)
					+ factorValue(ws2.getViews(), ViewFactor);
			if (wp1f == wp2f) {
				return 0;
			}
			return (wp1f < wp2f) ? -1 : 1;
		}
	};

	private static float factorValue(int value, int factor) {
		if (value == 0) { // catch for zero divide
			return 0;
		} else {
			return ((float) value) / ((float) factor);
		}
	}

	public SearchResults searchWidgetProfilesOrderedByRating(String query,
			String language, int rows, int offset) {
		return searchWidgetProfilesOrderedBy(query, language, rows, offset,
				RATING_ORDER);
	}

	public SearchResults searchWidgetProfilesOrderedByDate(String query,
			String language, int rows, int offset) {
		return searchWidgetProfilesOrderedBy(query, language, rows, offset,
				DATE_ORDER);
	}

	public SearchResults searchWidgetProfilesOrderedByPopularity(String query,
			String language, int rows, int offset) {
		return searchWidgetProfilesOrderedBy(query, language, rows, offset,
				POPULARITY_ORDER);
	}

	public SearchResults searchWidgetProfilesOrderedByRelevance(String query,
			String language, int rows, int offset) {
		return searchWidgetProfiles(query, language, rows, offset);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SearchResults searchWidgetProfilesOrderedBy(String query,
			String language, int rows, int offset, Comparator comparator) {
		SearchResults results = searchWidgetProfiles(query, language, rows,
				offset);
		Collections.sort(results.getWidgets(), comparator);
		return results;
	}

	@SuppressWarnings("unchecked")
	public SearchResults returnWidgetProfilesOrderedByDate(int rows, int offset) {
		List<Widgetprofile> widgetprofiles;
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query wpQuery = entityManager.createNamedQuery("Widgetprofile.updated");
		wpQuery.setFirstResult(offset);
		wpQuery.setMaxResults(rows);
		widgetprofiles = (List<Widgetprofile>) wpQuery.getResultList();
		entityManager.close();
		SearchResults sr = new SearchResults();
		sr.setWidgets(widgetprofiles);
		return sr;
	}

	public List<Widgetprofile> findSimilarWidgetsProfiles(
			Widgetprofile profile, String language) {
		WidgetService widgetService = new WidgetService();
		List<Widget> widgets = widgetService.findSimilarWidgets(
				profile.getWidId(), language);
		return getWidgetProfilesForWidgets(widgets);
	}

	public Widgetprofile findWidgetProfileByUri(String uri) {
		try {
			EntityManager entityManager = getEntityManagerFactory()
					.createEntityManager();
			Query wpQuery = entityManager
					.createNamedQuery("Widgetprofile.findByUri");
			wpQuery.setParameter("uri", uri);
			Widgetprofile widgetProfile = (Widgetprofile) wpQuery
					.getSingleResult();
			//
			// Ensure dependent objects are available when detached
			//
			widgetProfile.getTags();
			widgetProfile.getActivities();
			widgetProfile.getDescription();
			entityManager.close();
			return widgetProfile;
		} catch (NoResultException e) {
			return null;
		}

	}

	public Widgetprofile findWidgetProfileById(String id) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Widgetprofile widgetProfile = entityManager.find(Widgetprofile.class,
				id);

		if (widgetProfile == null)
			return null;

		//
		// Ensure dependent objects are available when detached
		//
		widgetProfile.getTags();
		widgetProfile.getActivities();
		widgetProfile.getDescription();
		entityManager.close();
		return widgetProfile;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchResults searchWidgetProfiles(String query, String lang,
			int rows, int offset) {
		SearchResults searchResults = SolrConnector.getInstance().query(query,
				lang, rows, offset);
		List widgets = searchResults.getWidgets();
		searchResults.setWidgets(getWidgetProfilesForWidgets(widgets));
		return searchResults;
	}

	private List<Widgetprofile> getWidgetProfilesForWidgets(List<Widget> widgets) {
		List<Widgetprofile> widgetProfiles = new ArrayList<Widgetprofile>();

		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		entityManager.getTransaction().begin();

		//
		// For each Widget returned from the search index, correlate with
		// an Edukapp WidgetProfile, if one exists. Otherwise, construct a
		// new profile for it and persist it.
		//
		for (Widget widget : widgets) {
			Widgetprofile widgetProfile = null;

			//
			// Find matching profile
			//
			try {
				Query wpQuery = entityManager
						.createNamedQuery("Widgetprofile.findByUri");
				wpQuery.setParameter("uri", widget.getUri());
				widgetProfile = (Widgetprofile) wpQuery.getSingleResult();
				//
				// Ensure dependent objects are available when detached
				//
				widgetProfile.getTags();
				widgetProfile.getActivities();

			} catch (NoResultException e) {

				//
				// Create a new profile
				//
				widgetProfile = new Widgetprofile();
				widgetProfile.setName(widget.getTitle());
				widgetProfile.setWidId(widget.getUri());
				widgetProfile.setIcon(widget.getIcon());
				widgetProfile.setCreated(new Date());
				widgetProfile.setUpdated(new Date());
				widgetProfile.setAuthor(widget.getAuthor());
				widgetProfile.setLicense(widget.getLicense());
				entityManager.persist(widgetProfile);

				//
				// Create the widget description
				//
				WidgetDescription widgetDescription = new WidgetDescription();
				widgetDescription.setWid_id(widgetProfile.getId());
				widgetDescription.setDescription(widget.getDescription());
				widgetProfile.setDescription(widgetDescription);

				//
				// Create the widget stats
				//
				WidgetStats widgetStats = new WidgetStats();
				widgetStats.setWid_id(widgetProfile.getId());
				widgetStats.setDownloads(0);
				widgetStats.setEmbeds(0);
				widgetStats.setViews(0);
				entityManager.persist(widgetStats);

			}

			widgetProfiles.add(widgetProfile);
		}

		entityManager.getTransaction().commit();
		entityManager.close();

		return widgetProfiles;
	}

	@SuppressWarnings("unchecked")
	private Message addTagToWidget(Widgetprofile widget, String newTag) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();

		entityManager.getTransaction().begin();

		Query q = entityManager
				.createQuery("SELECT t FROM Tag t WHERE t.tagtext=?1");
		q.setParameter(1, newTag);

		List<Tag> tags = (List<Tag>) q.getResultList();
		Tag tag = null;
		if (tags != null && tags.size() != 0) {
			tag = (Tag) tags.get(0);
		}

		if (tag == null) {
			tag = new Tag();
			tag.setTagtext(newTag);
			entityManager.persist(tag);
		}

		List<Tag> widget_tags = (List<Tag>) widget.getTags();

		if (widget_tags == null) {
			widget_tags = new ArrayList<Tag>();
		}

		// widget_tags contains fails - even after implementing equals() and
		// hash_code() in Tag, WidgetProfile
		// so use this primitive way
		boolean contains = false;
		for (Tag t : widget_tags) {
			if (t.getTagtext().equals(tag.getTagtext())
					&& t.getId() == tag.getId()) {
				contains = true;
			}
		}

		// if (widget_tags.contains(tag)) {
		if (contains) {
			Message msg = new Message();
			msg.setMessage("Widget:" + widget.getId() + " already has tag:"
					+ tag.getTagtext());
			return msg;
		} else {
			widget_tags.add(tag);
		}

		entityManager.persist(entityManager.merge(widget));
		// entityManager.merge(widget);

		entityManager.getTransaction().commit();
		Message msg = new Message();
		msg.setMessage("OK");
		msg.setId("" + tag.getId());
		return msg;

	}

	private Message addTagIdToWidget(Widgetprofile widget, String tagid) {
		// not yet implemented
		return null;
	}

	private boolean isNumeric(String str) {
		return str.matches("-?\\d+(.\\d+)?");
	}

	public Message addTag(Widgetprofile widgetProfile, String newTag) {
		if (isNumeric(newTag)) {
			return addTagIdToWidget(widgetProfile, newTag);
		} else {
			return addTagToWidget(widgetProfile, newTag);
		}
	}

	public Message addTag(String id, String newTag) {
		Widgetprofile widget = null;

		// check whether id is numeric
		if (isNumeric(id)) {
			widget = findWidgetProfileById(id);
		} else {
			widget = findWidgetProfileByUri(id);
		}

		if (widget == null) {
			Message msg = new Message();
			msg.setMessage("no widget found with id:" + id);
			return msg;
		}

		return addTag(widget, newTag);
	}

	public Message updateDescription(String id, String text) {

		Widgetprofile widget = null;

		// check whether id is numeric
		if (isNumeric(id)) {
			widget = findWidgetProfileById(id);
		} else {
			widget = findWidgetProfileByUri(id);
		}

		try {
			updateDescription(widget, text);
		} catch (Exception e) {
			Message msg = new Message();
			msg.setMessage("error:" + e.getMessage());
		}
		Message msg = new Message();
		msg.setMessage("OK update description done");
		msg.setId("" + widget.getId());
		return msg;
	}

	/**
	 * Update the description associated with a widget profile
	 * 
	 * @param widgetProfile
	 * @param text
	 */
	private void updateDescription(Widgetprofile widgetProfile, String text) {
		WidgetDescription widget_desc = widgetProfile.getDescription();
		if (widget_desc == null) {
			widget_desc = new WidgetDescription();
			widget_desc.setWid_id(widgetProfile.getId());
		}
		widget_desc.setDescription(text);
		widgetProfile.setDescription(widget_desc);

		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(entityManager.merge(widgetProfile));
		entityManager.getTransaction().commit();
	}

	public boolean addActivity(Widgetprofile widgetProfile, String body) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();

		entityManager.getTransaction().begin();

		// check if activity exists
		int activity_id = Integer.parseInt(body);
		Activity activity = entityManager.find(Activity.class, activity_id);

		if (activity == null) {
			logger.error("error - activity with id:" + activity_id
					+ " does not exist");
			return false;
		}

		List<Activity> widget_activities = widgetProfile.getActivities();

		boolean contains = false;
		for (Activity a : widget_activities) {
			if (a.getActivitytext().equals(activity.getActivitytext())
					&& (a.getId() == activity.getId())) {
				contains = true;
			}
		}

		if (contains) {
			logger.warn("Widget:" + widgetProfile.getId()
					+ " already has activity:" + activity.getActivitytext());
			return false;
		} else {
			widget_activities.add(activity);
		}

		entityManager.persist(entityManager.merge(widgetProfile));
		entityManager.getTransaction().commit();
		return true;
	}

}
