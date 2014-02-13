package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonUnwrapped;

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
 * Persistence class for Functionality Table
 * 
 * @author kjpopat@gmail.com
 */


@Entity
@Table(name="widget_functionality")
@NamedQueries({
	@NamedQuery(name="widget_functionality.select", query="SELECT wf from WidgetFunctionality wf WHERE wf.widgetProfile=:wid AND wf.functionality=:fid")
})
public class WidgetFunctionality implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true,nullable=false)
	private int id;
	

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="widgetprofile_id", nullable=false)
	private Widgetprofile widgetProfile;
	
	@Column(nullable=false)
	private int relevance;
	
	@JsonUnwrapped
	@ManyToOne
	@JoinColumn(name="functionality_id", nullable=false)
	Functionality functionality;
	
	
	public WidgetFunctionality() {}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the widgetprofile_id
	 */
	public Widgetprofile getWidgetProfile() {
		return widgetProfile;
	}


	/**
	 * @param widgetprofile_id the widgetprofile_id to set
	 */
	public void setWidgetProfile(Widgetprofile inprofile) {
		this.widgetProfile = inprofile;
	}


	/**
	 * @return the relevance
	 */
	public int getRelevance() {
		return relevance;
	}


	/**
	 * @param relevance the relevance to set
	 */
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}


	/**
	 * @return the functionality
	 */
	public Functionality getFunctionality() {
		return functionality;
	}


	/**
	 * @param functionality the functionality to set
	 */
	public void setFunctionality(Functionality functionality) {
		this.functionality = functionality;
	}
	
	
	
}
