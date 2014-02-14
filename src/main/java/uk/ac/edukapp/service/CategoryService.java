package uk.ac.edukapp.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import uk.ac.edukapp.model.Category;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.util.Message;

public class CategoryService extends AbstractService {
	static Logger logger = Logger.getLogger(AbstractService.class);
	
	public CategoryService ( ServletContext ctx ) {
		super(ctx);
	}
	
	/**
	 * 
	 * @return a list of category list with all the categories on the server.
	 */
	public List<List<Category>> getAllCategories()
	{
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query q = em.createQuery("SELECT c FROM Category c");
		@SuppressWarnings("unchecked")
		List<Category> categories = q.getResultList();
		q = em.createQuery("SELECT MAX(c.grouping) FROM Category c");
		Number n = (Number)q.getSingleResult();
		em.close();
		return this.buildListByGroup(categories, n.intValue());
	}
	
	
	/**
	 * 
	 * @param inCommaList: a string of comma separated ids of the categories to return
	 * @return a list of category lists
	 */
	public List<List<Category>> getCategories(String inCommaList) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query q = em.createQuery("SELECT c FROM Category c WHERE c.id IN (:idList)");
		List<String> idStrings = Arrays.asList(inCommaList.split(","));
		ArrayList<Number> idList = new ArrayList<Number>();
		for ( String idString: idStrings ) {
			idList.add(Integer.parseInt(idString));
		}
		q.setParameter("idList", idList);
		@SuppressWarnings("unchecked")
		List<Category> categories = q.getResultList();
		q = em.createQuery("SELECT MAX(c.grouping) FROM Category c");
		Number n = (Number)q.getSingleResult();
		em.close();
		return this.buildListByGroup(categories, n.intValue());
	}
	
	
	
	public List<Category> getCategoriesForGroup(String group) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query q = em.createQuery("SELECT c FROM Category c WHERE c.grouping = :categoryGroup");
		q.setParameter("categoryGroup", group);
		@SuppressWarnings("unchecked")
		List<Category> categories = q.getResultList();
		em.close();
		return categories;
	}
	
	
	public Category getCategoryForId ( String id ) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Category category = em.find(Category.class, id);
		em.close();
		return category;
	}
	
	
	/**
	 * addCategory
	 * @param title: String
	 * @param group: integer
	 * @return 
	 */
	public Message addCategory ( String title, int group ) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Message msg = new Message();
		msg.setMessage("OK");
		
		TypedQuery<Category> query = em.createNamedQuery("Category.findByTitle", Category.class);
		query.setParameter("title", title);
		List<Category> result = query.getResultList();
		if (result.size() > 0 ) {
			msg.setMessage("Error: category title already taken");
			em.close();
			return msg;
		}
		em.getTransaction().begin();
		
		Category c = new Category();
		c.setTitle(title);
		c.setGrouping(group);
		em.persist(c);
		em.getTransaction().commit();
		em.close();
		return msg;
	}
	
	
	private List<List<Category>> buildListByGroup ( List<Category> categories, int numGroups ) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		ArrayList<List<Category>> groupList = new ArrayList<List<Category>>();
		for ( int i = 0; i < numGroups; i++ ) {
			groupList.add(new ArrayList<Category>());
		}
		for ( Category c: categories ) {
			ArrayList<Category> group = (ArrayList<Category>) groupList.get(c.getGrouping()-1);
			group.add(c);
			// get for when category is disconnected
			Collection<Widgetprofile> wps = c.getWidgetprofiles();
			logger.debug("Widget profiles: "+wps);
		}
		em.getTransaction().commit();
		em.close();
		return groupList;
	}
	
}
