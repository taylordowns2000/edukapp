package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * The persistent class for the useraccount database table.
 * 
 */
@Entity
@Table(name="useraccount")
public class Useraccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=100)
	private String email;

	@Column(nullable=false, length=256)
	private String password;

	@Column(nullable=false, length=20)
	private String username;
	
	@Column(nullable=false, length=256)
  private String salt;
	
	@Column(nullable=false, length=256)
  private String token;

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

}