package uk.ac.edukapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.exceptions.EntityNotFound;
import uk.ac.edukapp.exceptions.WidgetUpdateException;
import uk.ac.edukapp.model.Activity;
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.WidgetDescription;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.repository.Widget;

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

	public WidgetProfileService(ServletContext ctx) {
		super(ctx);
	}

	public List<Widgetprofile> findWidgetProfilesForTag(Tag tag) {
		return tag.getWidgetprofiles();
	}

	public List<Widgetprofile> findWidgetProfilesForActivity(Activity activity) {
		return activity.getWidgetprofiles();
	}

	@SuppressWarnings("unchecked")
	public List<Widgetprofile> findFeaturedWidgetProfiles() {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query wpQuery = entityManager
				.createNamedQuery("Widgetprofile.featured");
		List<Widgetprofile> widgetProfiles = (List<Widgetprofile>) wpQuery
				.getResultList();
		entityManager.close();
		return widgetProfiles;
	}

	public SearchResults searchWidgetProfilesOrderedByRelevance(
			String query, String language, int rows, int offset) {
		return searchWidgetProfiles(query, language, rows, offset);
	}

	public SearchResults searchWidgetProfilesOrderedByRating(
			String query, String language, int rows, int offset) {
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

	public SearchResults searchWidgetProfilesOrderedByPopularity(
			String query, String language, int rows, int offset) {
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
	public SearchResults searchWidgetProfiles(String query, String lang, int rows, int offset){
		SearchResults searchResults = SolrConnector.getInstance().query(query, lang, rows, offset);
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

	public void updateWidgetprofileDescription(String id, String newText)
			throws PersistenceException, EntityNotFound {
		Widgetprofile widget = findWidgetProfileById(id);
		if (widget == null) {
			throw new EntityNotFound();
		}
		WidgetDescription widget_desc = widget.getDescription();
		if (widget_desc == null) {
			widget_desc = new WidgetDescription();
			widget_desc.setWid_id(Integer.parseInt(id));
		}
		widget_desc.setDescription(newText);
		widget.setDescription(widget_desc);

		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(entityManager.merge(widget));

		entityManager.getTransaction().commit();
	}

	public void addTag(String id, String newTag) throws PersistenceException,
			EntityNotFound, WidgetUpdateException {
		Widgetprofile widget = findWidgetProfileById(id);
		if (widget == null) {
			throw new EntityNotFound();
		}
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
			throw new WidgetUpdateException("widget already has this tag");
		} else {
			widget_tags.add(tag);
		}

		entityManager.persist(entityManager.merge(widget));
		// entityManager.merge(widget);

		entityManager.getTransaction().commit();
	}
}
