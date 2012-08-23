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
package uk.ac.edukapp.renderer;

import java.util.List;

/**
 * Holds a set of search results
 * @author scottw
 *
 */
public class SearchResults {
	
	private List widgets;
	private long number_of_results;
	/**
	 * @return the widgets
	 */
	public List getWidgets() {
		return widgets;
	}
	/**
	 * @param widgets the widgets to set
	 */
	public void setWidgets(List widgets) {
		this.widgets = widgets;
	}
	/**
	 * @return the number_of_results
	 */
	public long getNumber_of_results() {
		return number_of_results;
	}
	/**
	 * @param number_of_results the number_of_results to set
	 */
	public void setNumber_of_results(long number_of_results) {
		this.number_of_results = number_of_results;
	}
	
}
