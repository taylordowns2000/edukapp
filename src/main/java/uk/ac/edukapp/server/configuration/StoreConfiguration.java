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
 * Configuration information for the store itself,
 * for example the server location
 * 
 * @author scottw
 *
 */
public class StoreConfiguration {
	
	static Logger _logger = Logger.getLogger(StoreConfiguration.class.getName());
	
	private static StoreConfiguration instance;
	
	private String location;
	
	private StoreConfiguration(){
		
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration("store.properties");
			
			if (properties.containsKey("store.location"))
				location = properties.getString("store.location");
		} catch (ConfigurationException e) {
			//
			// No store properties found, so use default
			//
			_logger.warn("No store properties found; using defaults");
		}
		
	}
	
	public static StoreConfiguration getInstance(){
		if (instance == null) instance = new StoreConfiguration();
		return instance;
	}
	
	public String getLocation(){
		return location;
	}

}
