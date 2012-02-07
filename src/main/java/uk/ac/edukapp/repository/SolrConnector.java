package uk.ac.edukapp.repository;

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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * Store implementation for the Widget Discovery Service
 * 
 * Uses Solr index over http as backend.
 *
 */
public class SolrConnector {

	private static SolrConnector store = new SolrConnector();

	/**
	 * Provides instance of this class (singleton)
	 * 
	 * @return instance of this class
	 */
	public static SolrConnector getInstance() {
		return store;
	}

	/**
	 * Constructor. Bookmarks are initialized with some data.
	 */
	private SolrConnector() {
	}

	/**
	 * Search widgets
	 * @param q
	 * @return list of matching widgets
	 */
	public List<Widget> search(String q, String lang) {
		List<Widget> widgets = query(q,lang);
		return widgets;
	}

	/**
	 * List all widgets
	 * @return list of all widgets
	 */
	public List<Widget> getWidgets(String lang) {
		List<Widget> widgets = query("*:*",lang);
		return widgets;
	}

	/**
	 * Gives widget of requested widget Id (NOT URI).
	 * 
	 * @param key requested widget key (widget Id).
	 * @return requested widget, or <tt>null</tt> if widget with such key
	 *         does not exist.
	 */
	public Widget getWidget(String key, String lang) {
		List<Widget> widgets = query("id:"+key,lang);
		if (widgets.size()==1) return widgets.get(0);
		return null;
	}

	/**
	 * Execute query on Solr server. Always returns a result set list, even
	 * if the query fails.
	 * @param term
	 * @return list of matching widgets
	 */
	private List<Widget> query(String term, String lang){
		try {
			SolrServer server = getLocalizedSolrServer(lang);
			SolrQuery query = new SolrQuery();
			query.setQuery(term);
			QueryResponse rsp = server.query(query);
			List<Widget> widgets = rsp.getBeans(Widget.class);
			return widgets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Widget>();   	
	}

	private SolrServer getLocalizedSolrServer(String lang) throws MalformedURLException{
		if (lang==null || lang.trim().equals("")) lang="en";
		return new CommonsHttpSolrServer( "http://localhost:8983/solr/"+lang);    	
	}

}

