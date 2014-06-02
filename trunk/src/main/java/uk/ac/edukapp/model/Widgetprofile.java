package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonUnwrapped;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import uk.ac.edukapp.renderer.CustomJsonDateSerializer;
import uk.ac.edukapp.renderer.Renderer;


/**
 * The persistent class for the widgetprofiles database table.
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Widgetprofile.findByUri", query = "SELECT w FROM Widgetprofile w WHERE w.widId = :uri"),
		@NamedQuery(name = "Widgetprofile.featured", query = "SELECT w FROM Widgetprofile w WHERE w.featured = 1"),
		@NamedQuery(name = "Widgetprofile.created", query = "SELECT w FROM Widgetprofile w ORDERBY w.created DESC"),
		@NamedQuery(name = "Widgetprofile.ownedBy", query = "SELECT w FROM Widgetprofile w WHERE w.owner = :user")})
		
@Table(name = "widgetprofiles")
public class Widgetprofile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String W3C_WIDGET = "w3c";
	public static final String OPENSOCIAL_GADGET = "opensocial";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private String icon;
	
	@Column(nullable = false)
	private int publish_level;
	
	@Column(nullable = false)
	private byte deleted;
	
	@Column(nullable = false)
	private String meta_data;

	@JsonIgnore
	@Column(name = "w3c_or_os", nullable = false)
	private byte w3cOrOs;

	@Column(name = "featured", nullable = true)
	private byte featured;

	@JsonProperty(value = "uri")
	@Column(name = "wid_id", nullable = false, length = 150)
	private String widId;

	@JsonSerialize(using=CustomJsonDateSerializer.class)
	@Column(nullable = false)
	private Date created;

	@JsonSerialize(using=CustomJsonDateSerializer.class)
	@Column(nullable = false)
	private Date updated;
	
	@Column(nullable = true)
	private String license;
	
	@Column(nullable = true)
	private String author;
	
	
	@Column(nullable = true)
	private String builder;

	// bi-directional many-to-many association to Tag
	@ManyToMany()
	@JoinTable(name = "widgetprofiles_tags", 
			joinColumns = { @JoinColumn(name = "widgetprofile_id", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable = false) })
	private List<Tag> tags;

	// bi-directional many-to-many association to Activity
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "widgetactivities", 
			joinColumns = { @JoinColumn(name = "widgetprofile_id", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "activity_id", nullable = false) })
	private List<Activity> activities;
	

	@JsonUnwrapped
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "wid_id")
	WidgetDescription description;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "wid_id")
	WidgetStats widgetStats;
	
	
	@OneToMany(mappedBy="widgetProfile")
	private List<WidgetFunctionality> functionalities;
	
	@ManyToOne
	@JoinColumn(name="owner", nullable=false)
	private Useraccount owner;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="widgetprofiles_category",
			joinColumns = { @JoinColumn(name="widgetprofiles_id", referencedColumnName="id")},
			inverseJoinColumns = { @JoinColumn(name="category_id", referencedColumnName="id") })
	private List<Category> categories;
	

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

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
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

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
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

	public WidgetStats getWidgetStats() {
		return widgetStats;
	}

	public void setWidgetStats(WidgetStats widgetStats) {
		this.widgetStats = widgetStats;
	}

	public String getType() {
		if (this.getW3cOrOs() == 0) {
			return "W3C Widget";
		} else {
			return "OpenSocial Gadget";
		}
	}
	
	   public String getDownloadUrl() {
	       return Renderer.getDownloadUrl(this);
	    }

	/**
	 * @return the widgetFunctionalities
	 */
	public List<WidgetFunctionality> getFunctionalities() {
		return functionalities;
	}

	/**
	 * @param widgetFunctionalities the widgetFunctionalities to set
	 */
	public void setFunctionalities(
			List<WidgetFunctionality> widgetFunctionalities) {
		this.functionalities = widgetFunctionalities;
	}

	/**
	 * @return the owner
	 */
	public Useraccount getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Useraccount owner) {
		this.owner = owner;
	}

	/**
	 * @return the publish_level
	 */
	public int getPublish_level() {
		return publish_level;
	}

	/**
	 * @param publish_level the publish_level to set
	 */
	public void setPublish_level(int publish_level) {
		this.publish_level = publish_level;
	}

	/**
	 * @return the deleted
	 */
	public byte getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(byte deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the meta_data
	 */
	public String getMeta_data() {
		return meta_data;
	}

	/**
	 * @param meta_data the meta_data to set
	 */
	public void setMeta_data(String meta_data) {
		this.meta_data = meta_data;
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

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	

	/**
	 * @return the builder
	 */
	public String getBuilder() {
		return builder;
	}

	/**
	 * @param builder the builder to set
	 */
	public void setBuilder(String builder) {
		this.builder = builder;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getCategories() {
		return categories;
	}
	
	
	public void addCategory( Category category ) {
		if ( !this.getCategories().contains(category)) {
			this.getCategories().add(category);
		}
		if (!category.getWidgetprofiles().contains(this)) {
			category.getWidgetprofiles().add(this);
		}
	}
	
	
	public void removeCategory (Category category ) {
		if ( this.getCategories().contains(category)) {
			this.getCategories().remove(category);
		}
		if ( category.getWidgetprofiles().contains(this)) {
			category.getWidgetprofiles().remove(this);
		}
	}




}