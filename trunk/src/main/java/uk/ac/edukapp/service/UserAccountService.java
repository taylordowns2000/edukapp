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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Useraccount;

/**
 * Service for obtaining user accounts
 * @author scottw
 *
 */
public class UserAccountService extends AbstractService {
	
	public UserAccountService(ServletContext servletContext){
		super(servletContext);
	}

	public Useraccount getUserAccount(String username){
		Useraccount userAccount = null;
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
	    Query q = entityManager.createQuery("SELECT u " + "FROM Useraccount u WHERE u.username=?1");
	    q.setParameter(1, username);
	    try {
	    	userAccount = (Useraccount) q.getSingleResult();
	      } catch (javax.persistence.NoResultException e) {
	        return null;
	      }
		entityManager.close();
		return userAccount;
	}
	

	public Useraccount getUserAccount(Long userId){
		Useraccount userAccount = null;
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		userAccount = entityManager.find(Useraccount.class, userId);
		return userAccount;
	}
}
