package uk.ac.edukapp.renderer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

public class WookieServerConfiguration {
	
	static Logger _logger = Logger.getLogger(WookieServerConfiguration.class.getName());
	
	private String url = "http://localhost:8080/wookie";
	private String apiKey = "TEST";
	private static WookieServerConfiguration instance;
	
	private WookieServerConfiguration(){
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration("store.properties");
			if (properties.containsKey("wookie.server.location")) url = properties.getString("wookie.server.location");
			if (properties.containsKey("wookie.server.apiKey")) apiKey = properties.getString("wookie.server.apiKey");
			
		} catch (ConfigurationException e) {
			//
			// No store properties found, so default to "localhost"
			//
			_logger.warn("No store properties found; defaulting to using Wookie server at localhost with TEST api key");
		}
	}
	
	public static WookieServerConfiguration getInstance(){
		if (instance == null) instance = new WookieServerConfiguration();
		return instance;
	}

	public String getWookieServerLocation(){
		return url;
	}
	
	public WookieConnectorService getWookieConnector(String sharedDataKey) throws WookieConnectorException{
		WookieConnectorService conn = new WookieConnectorService(url, apiKey, sharedDataKey);
		return conn;
	}
}
