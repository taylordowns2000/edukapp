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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.model.LtiProvider;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.WidgetFavourite;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.servlets.pojos.Users;
import uk.ac.edukapp.util.MD5Util;
import uk.ac.edukapp.util.Message;

/**
 * Service for obtaining user accounts
 * 
 * @author scottw
 * 
 */
public class UserAccountService extends AbstractService {

	private static final Log log = LogFactory.getLog(Users.class);
	private ServletContext ctx;

	public UserAccountService(ServletContext servletContext) {
		super(servletContext);
		ctx = servletContext;
	}
	
	
	public List<Useraccount>listUsers()
	{
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query q = em.createQuery("SELECT u FROM Useraccount u");
		@SuppressWarnings("unchecked")
		List<Useraccount> results = q.getResultList();
		em.close();
		return results;
	}

	/*
	 * 		EntityManager em = getEntityManagerFactory().createEntityManager();
		// em.getTransaction().begin();
		Query q = em.createQuery("SELECT w FROM Widgetprofile w ");
		@SuppressWarnings("unchecked")
		List<Widgetprofile> results = q.getResultList();
		em.close();
		return results;
	 */
	public Useraccount getUserAccount(String username) {
		Useraccount userAccount = null;
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		Query q = entityManager.createQuery("SELECT u "
				+ "FROM Useraccount u WHERE u.username=?1");
		q.setParameter(1, username);
		try {
			userAccount = (Useraccount) q.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
		entityManager.close();
		return userAccount;
	}

	public Useraccount getUserAccount(Long userId) {
		Useraccount userAccount = null;
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		userAccount = entityManager.find(Useraccount.class, userId);
		entityManager.close();
		return userAccount;
	}

	public Useraccount getUserAccount(int userId) {
		Useraccount userAccount = null;
		EntityManager entityManager = getEntityManagerFactory()
				.createEntityManager();
		userAccount = entityManager.find(Useraccount.class, userId);
		entityManager.close();
		return userAccount;
	}
	
	
	public Useraccount registerNewUser ( String username, String email, String password, String realname ) throws Exception {
		EntityManager em = getEntityManagerFactory().createEntityManager();

		em.getTransaction().begin();

		Useraccount user = new Useraccount();
		user.setUsername(username);
		user.setEmail(email);

		UUID token = UUID.randomUUID();
		String salt = token.toString();
		String hashedPassword = MD5Util.md5Hex(salt + password);
		user.setPassword(hashedPassword);
		user.setSalt(salt);
		user.setToken("03");
		em.persist(user);
		log.info("User created with id: " + user.getId());
		Accountinfo info = new Accountinfo();
		info.setId(user.getId());
		info.setRealname(realname);
		info.setJoined(new Timestamp(new Date().getTime()));
		em.persist(info);
		//LtiProvider lti = new LtiProvider(user);
		//em.persist(lti);

		em.getTransaction().commit();
		em.close();

		ActivityService as = new ActivityService(ctx);
		as.addUserActivity(user.getId(), "joined", 0);

		return user;
	}
	
	
	public Useraccount updateUser ( int userId,
									String username,
									String email,
									String password,
									String realname ) {
		
		Useraccount user = this.getUserAccount(userId);
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		user.setEmail(email);

		UUID token = UUID.randomUUID();
		String salt = token.toString();
		String hashedPassword = MD5Util.md5Hex(salt + password);
		user.setPassword(hashedPassword);
		user.setSalt(salt);
		em.persist(user);
		
		user.setUsername(username);
		
		Accountinfo info = user.getAccountInfo();
		info.setRealname(realname);
		em.persist(user);
		em.persist(info);
		
		em.getTransaction().commit();
		em.close();
		
		return user;
	}
		
	
	public Useraccount authenticateUser ( String username, String password ) throws AuthenticationException {
	    UsernamePasswordToken token = new UsernamePasswordToken( username, password );
	    Subject currentUser = SecurityUtils.getSubject();

		currentUser.login(token);
		return getUserAccount ( username );
	}
	
	
	public WidgetFavourite addFavourite ( Useraccount user, Widgetprofile favourite, int level ) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		WidgetFavourite widgetFavourite = null;
		// check to see if it exists
		try {
			Query q = em.createNamedQuery("favourite.select");
			q.setParameter("user", user);
			q.setParameter("widgetprofile", favourite);
			widgetFavourite = (WidgetFavourite)q.getSingleResult();
			if ( widgetFavourite != null ) {
				widgetFavourite.setRelevance(level);
			}
		}
		catch ( NoResultException exp ) {
			// fine just make one
			widgetFavourite = new WidgetFavourite();
			widgetFavourite.setUserAccount(user);
			widgetFavourite.setWidgetProfile(favourite);
			widgetFavourite.setRelevance(level);
			em.persist(widgetFavourite);
		}
		em.getTransaction().commit();
		em.close();
		return widgetFavourite;
	}
	
	
	public Message removeFavourite( Useraccount user, Widgetprofile favourite ) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Query q = em.createNamedQuery("favourite.select");
		q.setParameter("user", user);
		q.setParameter("widgetprofile", favourite);
		WidgetFavourite fav = (WidgetFavourite) q.getSingleResult();
		Message msg = new Message();
		if ( fav != null ) {
			em.remove(fav);
			msg.setMessage("OK");
		}
		else {
			msg.setMessage("Favourite could not be removed");
		}
		em.getTransaction().commit();
		em.close();
		return msg;
	}
}
