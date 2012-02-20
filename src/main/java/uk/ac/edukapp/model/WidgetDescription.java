package uk.ac.edukapp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the widget description database table. currently
 * only holds a text - kind of overkill have an extra persistent class just for
 * this but maybe add more fields in the future
 * 
 */
@Entity
@Table(name = "widget_descriptions")
public class WidgetDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	private int wid_id;

	@Column(nullable = false)
	private String description;

	public WidgetDescription() {

	}

	public int getWid_id() {
		return wid_id;
	}

	public void setWid_id(int wid_id) {
		this.wid_id = wid_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + wid_id;
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
		WidgetDescription other = (WidgetDescription) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (wid_id != other.wid_id)
			return false;
		return true;
	}

}