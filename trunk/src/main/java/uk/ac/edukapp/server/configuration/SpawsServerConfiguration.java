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
import org.apache.log4j.Logger;

import uk.ac.bolton.spaws.ParadataManager;
import uk.ac.bolton.spaws.model.impl.Node;
import uk.ac.bolton.spaws.model.impl.Submitter;

public class SpawsServerConfiguration {

	static Logger _logger = Logger.getLogger(SpawsServerConfiguration.class
			.getName());
	
	private static final long ONE_HOUR = new Long(3525000);

	// Defaults
	private String nodeUrl = "http://alpha.mimas.ac.uk";
	private String username = "fred";
	private String password = "flintstone";
	private Node node;
	private boolean enabled = true;
	private long cacheDuration = ONE_HOUR;
	private int interval;

	private String submitterName = "edukapp";
	private Submitter submitter;
	
	private static SpawsServerConfiguration instance;

	private SpawsServerConfiguration() {
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration(
					"store.properties");
			if (properties.containsKey("spaws.node.location"))
				nodeUrl = properties.getString("spaws.node.location");
			if (properties.containsKey("spaws.node.username"))
				username = properties.getString("spaws.node.username");
			if (properties.containsKey("spaws.node.password"))
				password = properties.getString("spaws.node.password");
			if (properties.containsKey("spaws.submitter.name"))
				submitterName = properties.getString("spaws.submitter.name");
			if (properties.containsKey("spaws.enabled"))
				enabled = properties.getBoolean("spaws.enabled");
			if (properties.containsKey("spaws.cache"))
				cacheDuration = properties.getLong("spaws.cache");
			if (properties.containsKey("spaws.stats.interval"))
				interval = properties.getInt("spaws.stats.interval",1);
		} catch (ConfigurationException e) {
			//
			// No store properties found, so use default
			//
			_logger.warn("No SPAWS node properties found; using defaults");
		}
		try {
			node = new Node(new URL(nodeUrl), username, password);
		} catch (MalformedURLException e) {
			_logger.warn("SPAWS node URL is invalid; using defaults");
			try {
				node = new Node(new URL("http://alpha.mimas.ac.uk"), username, password);
			} catch (MalformedURLException e1) {
				// Should never happen...
			}

		}
		
		submitter = new Submitter();
		submitter.setSubmitter(submitterName);
		submitter.setSigner(submitterName);
		submitter.setSubmissionAttribution(submitterName);
	}

	public static SpawsServerConfiguration getInstance() {
		if (instance == null)
			instance = new SpawsServerConfiguration();
		return instance;
	}

	public Node getNode(){
		return node;
	}
	
	public Submitter getSubmitter(){
		return submitter;
	}
	
	public ParadataManager getParadataManager() throws Exception{
		return new ParadataManager(submitter, node);
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public long getCacheDuration(){
		return cacheDuration;
	}
	
	public int getInterval(){
		return interval;
	}
}
