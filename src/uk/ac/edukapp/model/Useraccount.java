package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;


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

    public Useraccount() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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