package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The persistent class for the widgetprofiles database table.
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Widgetprofile.findByUri", query = "SELECT w FROM Widgetprofile w WHERE w.widId = :uri"),
		@NamedQuery(name = "Widgetprofile.featured", query = "SELECT w FROM Widgetprofile w WHERE w.featured = 1") })
@Table(name = "widgetprofiles")
public class Widgetprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 100)
	private String name;

	@JsonIgnore
	@Column(name = "w3c_or_os", nullable = false)
	private byte w3cOrOs;

	@Column(name = "featured", nullable = true)
	private byte featured;

	@JsonProperty(value = "uri")
	@Column(name = "wid_id", nullable = false, length = 150)
	private String widId;

	// bi-directional many-to-many association to Tag
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "widgetprofiles_tags", joinColumns = { @JoinColumn(name = "widgetprofile_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable = false) })
	private List<Tag> tags;

	// bi-directional many-to-many association to Activity
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "widgetactivities", joinColumns = { @JoinColumn(name = "widgetprofile_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "activity_id", nullable = false) })
	private List<Activity> activities;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "wid_id")
	WidgetDescription description;

	public Widgetprofile() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getW3cOrOs() {
		return this.w3cOrOs;
	}

	public void setW3cOrOs(byte w3cOrOs) {
		this.w3cOrOs = w3cOrOs;
	}

	public String getWidId() {
		return this.widId;
	}

	public void setWidId(String widId) {
		this.widId = widId;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public byte getFeatured() {
		return featured;
	}

	public void setFeatured(byte featured) {
		this.featured = featured;
	}

	public WidgetDescription getDescription() {
		return description;
	}

	public void setDescription(WidgetDescription description) {
		this.description = description;
	}

	public String getType() {
		if (this.getW3cOrOs() == 0) {
			return "W3C Widget";
		} else {
			return "OpenSocial Gadget";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + featured;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + w3cOrOs;
		result = prime * result + ((widId == null) ? 0 : widId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Widgetprofile other = (Widgetprofile) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (featured != other.featured)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (w3cOrOs != other.w3cOrOs)
			return false;
		if (widId == null) {
			if (other.widId != null)
				return false;
		} else if (!widId.equals(other.widId))
			return false;
		return true;
	}

}