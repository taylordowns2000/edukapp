package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the tags database table.
 * 
 */
@Entity
@Table(name="tags")
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=30)
	private String tagtext;

	//bi-directional many-to-many association to Widgetprofile
	@ManyToMany(mappedBy="tags")
	private List<Widgetprofile> widgetprofiles;

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
	
}