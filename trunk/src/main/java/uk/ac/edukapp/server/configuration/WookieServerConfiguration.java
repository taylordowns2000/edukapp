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
package uk.ac.edukapp.server.configuration;

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
	private String url = "http://localhost:8080/wookie";
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
