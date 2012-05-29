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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Configuration for Solr server
 * @author scottw
 *
 */
public class SolrServerConfiguration {
	
	private Logger logger = Logger.getLogger(SolrServerConfiguration.class);
	
	private static SolrServerConfiguration instance;
	
	private String serverLocation = "http://localhost:8080/solr";
	
	private SolrServerConfiguration(){
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration(
					"store.properties");
			if (properties.containsKey("solr.server.location"))
				serverLocation = properties.getString("solr.server.location");
		} catch (ConfigurationException e) {
			logger.error("Error loading Solr server configuration", e);
		}
	}
	
	public static SolrServerConfiguration getInstance(){
		if (instance == null) instance = new SolrServerConfiguration();
		return instance;
	}
	
	public String getServerLocation(){
		return serverLocation;
	}
	
	/**
	 * Return the location of the solr server core for the specified locale
	 * @param locale
	 * @return a String for the location URL for the localized solr core
	 */
	public String getServerLocation(String locale){
		if (!serverLocation.endsWith("/")){
			return serverLocation + "/" + locale;
		} else {
			return serverLocation + locale;
		}
	}

}
