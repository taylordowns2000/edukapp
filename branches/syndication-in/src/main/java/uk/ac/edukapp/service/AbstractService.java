package uk.ac.edukapp.service;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

public abstract class AbstractService {

	protected EntityManagerFactory emf;
	
	public AbstractService(){
	}
	
	public AbstractService(ServletContext ctx){
		emf = (EntityManagerFactory) ctx.getAttribute("emf");
	}
	
	protected EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}
}
