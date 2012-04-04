package uk.ac.edukapp.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.exceptions.EntityNotFound;
import uk.ac.edukapp.exceptions.WidgetUpdateException;
import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.model.Comment;
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.WidgetDescription;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.repository.Widget;
import uk.ac.edukapp.util.Message;
import uk.ac.edukapp.service.WidgetService;

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
 */
public class WidgetProfileService extends AbstractService {

	static Logger logger = Logger.getLogger(WidgetProfileService.class
			.getName());

	public WidgetProfileService(ServletContext ctx) {
		super(ctx);
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

	public SearchResults searchWidgetProfilesOrderedByRelevance(String query,
			String language, int rows, int offset) {
		return searchWidgetProfiles(query, language, rows, offset);
	}

	public SearchResults searchWidgetProfilesOrderedByRating(String query,
			String language, int rows, int offset) {
		// FIXME create comparator using ratings
		return searchWidgetProfiles(query, language, rows, offset);
	}

	public List<Widgetprofile> findSimilarWidgetsProfiles(
			Widgetprofile profile, String language) {
		WidgetService widgetService = new WidgetService();
		List<Widget> widgets = widgetService.findSimilarWidgets(
				profile.getWidId(), language);
		return getWidgetProfilesForWidgets(widgets);
	}

	public SearchResults searchWidgetProfilesOrderedByPopularity(String query,
			String language, int rows, int offset) {
		// FIXME create comparator using popularity
		return searchWidgetProfiles(query, language, rows, offset);
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
			// TODO replace with a named query
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
				entityManager.persist(widgetProfile);
			}

			widgetProfiles.add(widgetProfile);
		}

		entityManager.getTransaction().commit();
		entityManager.close();

		return widgetProfiles;
	}

	// public void updateWidgetprofileDescription(String id, String newText)
	// throws PersistenceException, EntityNotFound {
	// Widgetprofile widget = findWidgetProfileById(id);
	// if (widget == null) {
	// throw new EntityNotFound();
	// }
	// WidgetDescription widget_desc = widget.getDescription();
	// if (widget_desc == null) {
	// widget_desc = new WidgetDescription();
	// widget_desc.setWid_id(Integer.parseInt(id));
	// }
	// widget_desc.setDescription(newText);
	// widget.setDescription(widget_desc);
	//
	// EntityManager entityManager = getEntityManagerFactory()
	// .createEntityManager();
	// entityManager.getTransaction().begin();
	//
	// entityManager.persist(entityManager.merge(widget));
	//
	// entityManager.getTransaction().commit();
	// }

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
		return msg;

	}

	private Message addTagIdToWidget(Widgetprofile widget, String tagid) {
		// not yet implemented
		return null;
	}

	private boolean isNumeric(String str) {
		return str.matches("-?\\d+(.\\d+)?");
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

		if (isNumeric(newTag)) {
			return addTagIdToWidget(widget, newTag);
		} else {
			return addTagToWidget(widget, newTag);
		}
	}

	public Message updateDescription(String id, String text) {

		Widgetprofile widget = null;

		// check whether id is numeric
		if (isNumeric(id)) {
			widget = findWidgetProfileById(id);
		} else {
			widget = findWidgetProfileByUri(id);
		}

		WidgetDescription widget_desc = widget.getDescription();
		if (widget_desc == null) {
			widget_desc = new WidgetDescription();
			widget_desc.setWid_id(Integer.parseInt(id));
		}
		widget_desc.setDescription(text);
		widget.setDescription(widget_desc);

		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entityManager.merge(widget));
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			Message msg = new Message();
			msg.setMessage("eroor:" + e.getMessage());
		}
		Message msg = new Message();
		msg.setMessage("OK update description done");
		msg.setId("" + widget.getId());
		return msg;
	}

	public Message addComment(String id, String text, String userid) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Useraccount user = entityManager.find(Useraccount.class, userid);
		Widgetprofile widget = entityManager.find(Widgetprofile.class, id);

		if (user == null) {
			Message msg = new Message();
			msg.setMessage("user is null");
			return msg;
		}
		if (widget == null) {
			Message msg = new Message();
			msg.setMessage("widget is null");
			return msg;
		}

		Comment c = new Comment();
		c.setCommenttext(text);
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(c);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			Message msg = new Message();
			msg.setMessage("error");
			return msg;
		}

		Userreview ur = new Userreview();
		ur.setComment(c);
		byte b = 0;
		ur.setRating(b);
		ur.setUserAccount(user);
		ur.setWidgetProfile(widget);
		java.util.Date date = new java.util.Date();
		Timestamp now = new Timestamp(date.getTime());
		ur.setTime(now);
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(ur);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			Message msg = new Message();
			msg.setMessage("error");
			return msg;
		}

		Message msg = new Message();
		msg.setMessage("OK");
		return msg;

	}
}
