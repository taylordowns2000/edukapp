package uk.ac.edukapp.service;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

public abstract class AbstractService {

	protected EntityManagerFactory emf;
	protected ServletContext servletContext;
	
	public AbstractService(){
	}
	
	public AbstractService(ServletContext ctx){
		emf = (EntityManagerFactory) ctx.getAttribute("emf");
		servletContext = ctx;
	}
	
	protected EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}
}
