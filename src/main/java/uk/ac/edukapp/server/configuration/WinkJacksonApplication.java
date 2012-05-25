package uk.ac.edukapp.server.configuration;

import java.util.HashSet;
import java.util.Set;

//Jackson imports
import org.apache.wink.providers.jackson.WinkJacksonJaxbJsonProvider;

import javax.ws.rs.core.Application;

public class WinkJacksonApplication extends Application {

	
	
	
	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> services = new HashSet<Class<?>>();
		services.add(uk.ac.edukapp.servlets.Widgets.class);
		return services;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getSingletons()
	 */
	@Override
	public Set<Object> getSingletons() {
		WinkJacksonJaxbJsonProvider p = new WinkJacksonJaxbJsonProvider();

		Set<Object> s = new HashSet<Object>();
		s.add(p);
		return s;
	}
	
	
}
