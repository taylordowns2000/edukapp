
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
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Tag;

public class TagService extends AbstractService{
	
	public TagService(ServletContext servletContext) {
		super(servletContext);
	}

	/**
	 * Get the most popular tags
	 * @return a List of tags
	 */
	public List<Tag> getPopularTags(){
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		TypedQuery<Tag> query = entityManager.createNamedQuery("Tag.popular", Tag.class);
		List<Tag> tags = query.setMaxResults(10).getResultList();
		return tags;
	}
}
