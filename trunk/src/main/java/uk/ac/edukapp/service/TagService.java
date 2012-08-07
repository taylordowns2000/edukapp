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

package uk.ac.edukapp.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.model.Tag;

/**
 * Tag service
 * 
 * @author scottw
 * 
 */
public class TagService extends AbstractService {

	static Logger logger = Logger.getLogger(TagService.class.getName());

	public TagService(ServletContext servletContext) {
		super(servletContext);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tag> getAllTags(int offset, int limit) {
		EntityManager entityManager = getEntityManagerFactory()
		.createEntityManager();
		Query query = entityManager.createQuery("SELECT t FROM Tag t");
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	/**
	 * Get the most popular tags
	 * 
	 * @return a List of tags
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> getPopularTags() {

		List<Tag> tags;

		//
		// By preference we load this from the cache rather
		// than from JPA
		//
		tags = (List<Tag>) Cache.getInstance().get("PopularTags");

		if (tags == null) {
			EntityManager entityManager = getEntityManagerFactory()
					.createEntityManager();
			TypedQuery<Tag> query = entityManager.createNamedQuery(
					"Tag.popular", Tag.class);
			tags = query.setMaxResults(10).getResultList();
			entityManager.close();

			//
			// Add to cache with a max lifetime of one hour
			//
			Cache.getInstance().put("PopularTags", tags, 3200);
			logger.debug("Loaded popular tags from JPA");
		} else {
			logger.debug("loaded popular tags from cache");
		}
		return tags;
	}

	/**
	 * Get a tag
	 * 
	 * @param tagid
	 * @return
	 */
	public Tag getTag(String tagid) {
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Tag tag = null;

		try {

			//
			// See if the id is an integer tag id
			//
			int id = Integer.parseInt(tagid);
			tag = entityManager.find(Tag.class, id);
		} catch (NumberFormatException e) {

			//
			// maybe the id is a tag name instead?
			//
			TypedQuery<Tag> query = entityManager.createNamedQuery(
					"Tag.findByName", Tag.class);
			query.setParameter("tagname", tagid);
			tag = query.getSingleResult();
		}

		//
		// Fetch widgetProfiles so these are available
		// in the detached entity
		//
		if (tag != null)
			tag.getWidgetprofiles();

		entityManager.close();
		return tag;
	}

	/**
	 * Create a new tag
	 * @param text the tag label
	 * @return true if a new tag was created; false if one already exists with the same label
	 * @throws Exception if there was a problem creating the tag
	 */
	public boolean insertTag(String text) throws Exception {
		try {
			EntityManager entityManager = getEntityManagerFactory()
			.createEntityManager();

			//
			// check if the tag text already exists. If it does,
			// return false.
			//
			// (any better way to do this?)
			//
			
			TypedQuery<Tag> query = entityManager.createNamedQuery(
					"Tag.findByName", Tag.class);
			query.setParameter("tagname", text);
			List<Tag> result = query.getResultList();// getSingleResult();
			if (result.size() > 0) {
				return false;
			}

			//
			// execute the insert
			//
			Tag tag = new Tag();
			tag.setTagtext(text);
			entityManager.getTransaction().begin();
			entityManager.persist(tag);
			entityManager.getTransaction().commit();
			entityManager.close();
			return true;
			
		} catch (Exception e) {
			logger.error("problem creating tag:"+text, e);
			throw e;
		}
	}
}
