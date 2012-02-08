package uk.ac.edukapp.service;

import java.util.List;

import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.repository.Widget;


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

/**
 * Provides services for obtaining widgets and their metadata, which can then be
 * associated with relevant Widgetprofile data
 * 
 * @author scott.bradley.wilson@gmail.com
 *
 */
public class WidgetService {
	
	public List<Widget> findWidgets(String query){
		return findWidgets(query,"en",10,0);
	}
	
	public List<Widget> findWidgets(String query, String lang, int rows, int offset){
		List<Widget> widgets = SolrConnector.getInstance().query(query, lang, rows, offset);
		return widgets; 
	}

	public Widget findWidgetByUri(String uri){
		return findWidgetByUri(uri, "en");
	}
	
	public Widget findWidgetByUri(String uri, String lang){
		List<Widget> widgets = SolrConnector.getInstance().query("uri:"+uri,lang, 1,0);
		Widget widget = null;
		if (widgets.size()==1) widget = widgets.get(0);
		return widget;
	}

}
