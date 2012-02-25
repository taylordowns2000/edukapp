package uk.ac.edukapp.service;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Accountinfo;

public class AccountInfoService extends AbstractService {
	
	public AccountInfoService(ServletContext servletContext){
		super(servletContext);
	}
	
	public Accountinfo getAccountInfo(String id){
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		Accountinfo accountInfo = entityManager.find(Accountinfo.class, id);
		entityManager.close();
		return accountInfo;
	}

}
