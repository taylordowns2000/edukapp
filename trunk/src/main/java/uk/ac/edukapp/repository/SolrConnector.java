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
import org.apache.solr.common.params.MoreLikeThisParams;

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
	 * Constructor.
	 */
	private SolrConnector() {
	}

	/**
	 * Execute query on Solr server. Always returns a result set list, even
	 * if the query fails.
	 * @param term
	 * @return list of matching widgets
	 */
	public List<Widget> query(String term, String lang, int rows, int offset){
		try {
			SolrServer server = getLocalizedSolrServer(lang);
			SolrQuery query = new SolrQuery();
			query.setRows(rows);
			query.setStart(offset);
			query.setQuery(term);
			QueryResponse rsp = server.query(query);
			List<Widget> widgets = rsp.getBeans(Widget.class);
			return widgets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Widget>();   	
	}
	
	/**
	 * Execute a MoreLikeThis query on the Solr server
	 * @param term the match term, typically a uri: match
	 * @param lang the language core to search
	 * @return more widgets like the one identified by term
	 */
	public List<Widget> moreLikeThis(String term, String lang){
		try {
			SolrServer server = getLocalizedSolrServer(lang);
			SolrQuery query = new SolrQuery();
			
			query.setQueryType("/"+MoreLikeThisParams.MLT);
			query.set(MoreLikeThisParams.MATCH_INCLUDE, false);
			query.set(MoreLikeThisParams.MIN_DOC_FREQ, 1);
			query.set(MoreLikeThisParams.MIN_TERM_FREQ, 1);
			query.set(MoreLikeThisParams.SIMILARITY_FIELDS, "title,description");
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
		return new CommonsHttpSolrServer( "http://localhost:8080/solr/"+lang);    	
	}

}

