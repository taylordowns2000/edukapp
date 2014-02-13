package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

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
@NamedQueries({
	@NamedQuery(name="Functionality.findByLevel", query = "SELECT f FROM Functionality f where f.level = :level")
})
@Table(name="functionality")
public class Functionality implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true,nullable=false)
	private int id;
	
	@Column(nullable=false, length=255)
	private String name;
	
	
	@Column(nullable=false)
	private String uri;
	
	
	@Column(nullable=false)
	private int level;
	
	@JsonIgnore
	@OneToMany ( cascade=CascadeType.ALL, mappedBy="functionality")
	private List<WidgetFunctionality>  widgetFunctionalities;
	
	
	public Functionality(){}

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
	 * @return the functionality_name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param functionality_name the functionality_name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the functionality_description
	 */
	public String getURI() {
		return uri;
	}


	/**
	 * @param functionality_description the functionality_description to set
	 */
	public void setURI(String URI) {
		this.uri = URI;
	}
	
	

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the widgetFunctionalities
	 */
	public List<WidgetFunctionality> getWidgetFunctionalities() {
		return widgetFunctionalities;
	}

	/**
	 * @param widgetFunctionalities the widgetFunctionalities to set
	 */
	public void setWidgetFunctionalities(
			List<WidgetFunctionality> widgetFunctionalities) {
		this.widgetFunctionalities = widgetFunctionalities;
	}
	
}
