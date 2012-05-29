package uk.ac.edukapp.renderer;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

public class WookieServerConfiguration {

	static Logger _logger = Logger.getLogger(WookieServerConfiguration.class
			.getName());

	// Defaults
	private String url = "http://widgets.open.ac.uk:8080/wookie";
	private String apiKey = "TEST";
	private String username = "java";
	private String password = "java";

	private static WookieServerConfiguration instance;

	private WookieServerConfiguration() {
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration(
					"store.properties");
			if (properties.containsKey("wookie.server.location"))
				url = properties.getString("wookie.server.location");
			if (properties.containsKey("wookie.server.apiKey"))
				apiKey = properties.getString("wookie.server.apiKey");
			if (properties.containsKey("wookie.server.username"))
				username = properties.getString("wookie.server.username");
			if (properties.containsKey("wookie.server.password"))
				password = properties.getString("wookie.server.password");
		} catch (ConfigurationException e) {
			//
			// No store properties found, so default to "localhost"
			//
			_logger.warn("No store properties found; defaulting to using Wookie server at localhost with TEST api key");
		}
	}

	public static WookieServerConfiguration getInstance() {
		if (instance == null)
			instance = new WookieServerConfiguration();
		return instance;
	}

	public String getWookieServerLocation() {
		return url;
	}

	/**
	 * Get an auth scope for Wookie
	 * 
	 * @return the AuthScope for Wookie
	 * @throws MalformedURLException
	 */
	public AuthScope getAuthScope() throws MalformedURLException {
		URL url = new URL(getWookieServerLocation());
		int port = url.getPort();
		String host = url.getHost();
		AuthScope authScope = new AuthScope(host, port, AuthScope.ANY_REALM);
		return authScope;
	}

	/**
	 * Get a set of credentials to use with Wookie
	 * 
	 * @return a Credentials object
	 */
	public Credentials getCredentials() {
		return new UsernamePasswordCredentials(username, password);
	}

	public WookieConnectorService getWookieConnector(String sharedDataKey)
			throws WookieConnectorException {
		WookieConnectorService conn = new WookieConnectorService(url, apiKey,
				sharedDataKey);
		return conn;
	}
}
