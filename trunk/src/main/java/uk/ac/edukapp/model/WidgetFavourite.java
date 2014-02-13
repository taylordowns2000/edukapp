package uk.ac.edukapp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonUnwrapped;



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
 * Persistence class for WidgetFavourite joining table
 * 
 * @author kjpopat@gmail.com
 */

@Entity
@Table(name="favourite")
@NamedQueries({
	@NamedQuery(name="favourite.select", query="SELECT fav from WidgetFavourite fav WHERE fav.userAccount=:user AND fav.widgetProfile=:widgetprofile")
})
public class WidgetFavourite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7685904607433058704L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true,nullable=false)
	private int id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private Useraccount userAccount;
	
	@Column(nullable=false)
	private int relevance;
	
	@JsonUnwrapped
	@ManyToOne
	@JoinColumn(name="widgetprofile_id", nullable=false)
	private Widgetprofile widgetProfile;
	
	
	public WidgetFavourite(){}


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
	 * @return the userAccount
	 */
	public Useraccount getUserAccount() {
		return userAccount;
	}


	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(Useraccount userAccount) {
		this.userAccount = userAccount;
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
	 * @return the widgetProfile
	 */
	public Widgetprofile getWidgetProfile() {
		return widgetProfile;
	}


	/**
	 * @param widgetProfile the widgetProfile to set
	 */
	public void setWidgetProfile(Widgetprofile widgetProfile) {
		this.widgetProfile = widgetProfile;
	}

}
