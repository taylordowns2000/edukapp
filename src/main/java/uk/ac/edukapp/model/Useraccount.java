package uk.ac.edukapp.model;

import java.util.List;

import javax.persistence.*;

import uk.ac.edukapp.model.Userrole;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The persistent class for the useraccount database table.
 * 
 */
@Entity
@Table(name = "useraccount")
public class Useraccount {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = false, length = 256)
	private String password;

	@Column(nullable = false, length = 256)
	private String username;

	@Column(nullable = false, length = 256)
	private String salt;

	@Column(nullable = true, length = 256)
	private String token;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private Accountinfo accountInfo;

	// bi-directional many-to-many association to Userrole
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "useraccount_roles", joinColumns = { @JoinColumn(name = "user_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false) })
	private List<Userrole> roles;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="userAccount", fetch=FetchType.EAGER)
	private List<WidgetFavourite> favourites;
	
	


	@JsonIgnore
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@JsonIgnore
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Useraccount() {
	}

	@JsonIgnore
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonIgnore
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public List<Userrole> getRoles() {
		return roles;
	}

	public void setRoles(List<Userrole> roles) {
		this.roles = roles;
	}

	/**
	 * @return the accountInfo
	 */
	public Accountinfo getAccountInfo() {
		return accountInfo;
	}

	/**
	 * @param accountinfo the accountinfo to set
	 */
	public void setAccountInfo(Accountinfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public void setFavourites(List<WidgetFavourite> favourites) {
		this.favourites = favourites;
	}

	public List<WidgetFavourite> getFavourites() {
		return favourites;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Useraccount other = (Useraccount)obj;
		if ( other.getId() == this.getId() ) {
			return true;
		}
		return false;
	}
	
	
}