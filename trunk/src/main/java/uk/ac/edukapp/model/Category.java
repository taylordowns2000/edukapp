package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;


/*
 *  (c) 2013 University of Bolton
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
 * Persistence class for category Table
 * 
 * @author kjpopat@gmail.com
 */

@Entity
@Table(name="category")
@NamedQueries({
	@NamedQuery(name = "Category.findByTitle", query = "SELECT c FROM Category c WHERE c.title = :title")
})
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1053309694706900434L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true,nullable=false)
	private int id;
	
	@Column(nullable=false, length=255)
	private String title;
	
	
	@Column(nullable=true)
	private int grouping;
	
	
	@JsonIgnore
	@ManyToMany(mappedBy="categories", fetch = FetchType.EAGER)
	private List<Widgetprofile> widgetprofiles;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Category other = (Category)obj;
		
		if ( other.getId() == this.getId() ) {
			return true;
		}
		return false;
	}

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public void setGrouping(int grouping) {
		this.grouping = grouping;
	}

	public int getGrouping() {
		return grouping;
	}

	public void setWidgetprofiles(List<Widgetprofile> widgets) {
		this.widgetprofiles = widgets;
	}

	public List<Widgetprofile> getWidgetprofiles() {
		return widgetprofiles;
	}
	
	
	public void addWidgetprofile(Widgetprofile widgetprofile ) {
		if ( !this.getWidgetprofiles().contains(widgetprofile)) {
			this.getWidgetprofiles().add(widgetprofile);
		}
		if ( !widgetprofile.getCategories().contains(this)) {
			widgetprofile.getCategories().add(this);
		}
	}



}
