package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the accountinfo database table.
 * 
 */
@Entity
@Table(name="accountinfo")
public class Accountinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	private Timestamp joined;

	private Timestamp lastseen;

	@Column(nullable=false, length=35)
	private String realname;

	@Column(length=2056)
	private String shortbio;

	@Column(length=256)
	private String website;

    public Accountinfo() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getJoined() {
		return this.joined;
	}

	public void setJoined(Timestamp joined) {
		this.joined = joined;
	}

	public Timestamp getLastseen() {
		return this.lastseen;
	}

	public void setLastseen(Timestamp lastseen) {
		this.lastseen = lastseen;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getShortbio() {
		return this.shortbio;
	}

	public void setShortbio(String shortbio) {
		this.shortbio = shortbio;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}