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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The persistent class for the tags database table.
 * 
 */
@Entity
@Table(name = "tags")
@NamedQueries({
	@NamedQuery(name = "Tag.popular", query = "SELECT t, SIZE(t.widgetprofiles) as freq FROM Tag t ORDER BY freq DESC"),
	@NamedQuery(name = "Tag.findByName", query = "SELECT t FROM Tag t WHERE t.tagtext = :tagname"),
	@NamedQuery(name = "Tag.range", query="SELECT t FROM Tag t LIMIT :offset, :limit")
})
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 30)
	private String tagtext;


	// bi-directional many-to-many association to Widgetprofile
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "widgetprofiles_tags", 
			joinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "widgetprofile_id", referencedColumnName = "id", nullable = false) })
	private List<Widgetprofile> widgetprofiles;

	
	@ManyToOne
	@JoinColumn(name="owner", nullable=false)
	private Useraccount owner;
	
	
	public Tag() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTagtext() {
		return this.tagtext;
	}

	public void setTagtext(String tagtext) {
		this.tagtext = tagtext;
	}

	@JsonIgnore
	public List<Widgetprofile> getWidgetprofiles() {
		return this.widgetprofiles;
	}

	public void setWidgetprofiles(List<Widgetprofile> widgetprofiles) {
		this.widgetprofiles = widgetprofiles;
	}

	public void setOwner(Useraccount owner) {
		this.owner = owner;
	}

	public Useraccount getOwner() {
		return owner;
	}
/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((tagtext == null) ? 0 : tagtext.hashCode());
		result = prime * result
				+ ((widgetprofiles == null) ? 0 : widgetprofiles.hashCode());
		return result;
	}
*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		if (tagtext == null) {
			if (other.tagtext != null)
				return false;
		} else if (!tagtext.equals(other.tagtext))
			return false;
		//if (widgetprofiles == null) {
		//	if (other.widgetprofiles != null)
		//		return false;
		//} else if (!widgetprofiles.equals(other.widgetprofiles))
		//	return false;
		return true;
	}

}