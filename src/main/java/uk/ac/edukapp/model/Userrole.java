package uk.ac.edukapp.model;

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
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The persistent class for the security roles
 * 
 */
@Entity
@Table(name = "roles")
public class Userrole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 30)
	private String rolename;

	// bi-directional many-to-many association to Widgetprofile
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "useraccount_roles", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) })
	private List<Useraccount> users;

	public Userrole() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@JsonIgnore
	public List<Useraccount> getUsers() {
		return users;
	}

	public void setUsers(List<Useraccount> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((rolename == null) ? 0 : rolename.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Userrole other = (Userrole) obj;
		if (id != other.id)
			return false;
		if (rolename == null) {
			if (other.rolename != null)
				return false;
		} else if (!rolename.equals(other.rolename))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}