package uk.ac.edukapp.roughtests;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.persistence.*;

public class TestJPA {
  public void run() {
    
    //load persitence properties from prop file
    /*
    Properties properties = new Properties();
    InputStream propertiesStream = this.getClass().getResourceAsStream("persistence.properties");
    if (propertiesStream == null)
    {
        throw new IllegalArgumentException("Unable to load configuration: persistence.properties");
    }       
    try {
      properties.load(propertiesStream);
    } catch (IOException e) {      
      e.printStackTrace();
    }
    */
    
    //
    //EntityManagerFactory factory = Persistence.createEntityManagerFactory("edukapp",properties);
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("edukapp");
    EntityManager em = factory.createEntityManager();
    
    /*-----------*/
    em.getTransaction().begin();
    
    Person p = new Person();
    p.setName("Lucas");
    p.setSalary(100.09f);
    em.persist(p);    
    
    em.getTransaction().commit();
    /*----------*/
    
    em.close();
    factory.close();
  }
}
