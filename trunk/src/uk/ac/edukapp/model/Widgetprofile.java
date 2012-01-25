package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the widgetprofiles database table.
 * 
 */
@Entity
@Table(name="widgetprofiles")
public class Widgetprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=100)
	private String name;

	@Column(name="w3c_or_os", nullable=false)
	private byte w3cOrOs;

	@Column(name="wid_id", nullable=false, length=150)
	private String widId;

	//bi-directional many-to-many association to Tag
    @ManyToMany
	@JoinTable(
		name="widgetprofiles_tags"
		, joinColumns={
			@JoinColumn(name="widgetprofile_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="tag_id", nullable=false)
			}
		)
	private List<Tag> tags;

	//bi-directional many-to-many association to Activity
    @ManyToMany
	@JoinTable(
		name="widgetactivities"
		, joinColumns={
			@JoinColumn(name="widgetprofile_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="activity_id", nullable=false)
			}
		)
	private List<Activity> activities;

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
	
}