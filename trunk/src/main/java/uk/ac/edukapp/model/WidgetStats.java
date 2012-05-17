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
package uk.ac.edukapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class WidgetStats {
	
	@JsonIgnore
	@Id
	@Column(nullable = false)
	private int wid_id;

	@Column(name = "downloads")
	private int downloads;
	
	@Column(name = "embeds")
	private int embeds;
	
	@Column(name = "views")
	private int views;
	
	private Number averageRating;
	
	private Long totalRatings;
	
	/**
	 * @return the wid_id
	 */
	public int getWid_id() {
		return wid_id;
	}
	/**
	 * @param wid_id the wid_id to set
	 */
	public void setWid_id(int wid_id) {
		this.wid_id = wid_id;
	}
	/**
	 * @return the downloads
	 */
	public int getDownloads() {
		return downloads;
	}
	/**
	 * @param downloads the downloads to set
	 */
	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}
	/**
	 * @return the embeds
	 */
	public int getEmbeds() {
		return embeds;
	}
	/**
	 * @param embeds the embeds to set
	 */
	public void setEmbeds(int embeds) {
		this.embeds = embeds;
	}
	/**
	 * @return the views
	 */
	public int getViews() {
		return views;
	}
	/**
	 * @param views the views to set
	 */
	public void setViews(int views) {
		this.views = views;
	}
	/**
	 * @return the averageRating
	 */
	public Number getAverageRating() {
		return averageRating;
	}
	/**
	 * @param averageRating the averageRating to set
	 */
	public void setAverageRating(Number averageRating) {
		this.averageRating = averageRating;
	}
	/**
	 * @return the totalRatings
	 */
	public Long getTotalRatings() {
		return totalRatings;
	}
	/**
	 * @param totalRatings the totalRatings to set
	 */
	public void setTotalRatings(Long totalRatings) {
		this.totalRatings = totalRatings;
	}
	
	
	
}
