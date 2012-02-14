package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * The persistent class for the userreview database table.
 * 
 */
@Entity
@Table(name="userreview")
@NamedQueries({
    @NamedQuery(name="Userreview.findForWidgetProfile", query="SELECT r FROM Userreview r WHERE r.widgetProfile = :widgetprofile")
}) 
public class Userreview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false)
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="comment_id", referencedColumnName="id")
	private Comment comment;
	
	@Column(nullable=false)
	private byte rating;

	@Column(nullable=false)
	private Date time;

	@JsonIgnore
	@Column(nullable=false)
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widgetprofile_id", referencedColumnName="id")
	private Widgetprofile widgetProfile;
	
	@Column(nullable=false)
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="id")
	private Useraccount userAccount;

    public Userreview() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getRating() {
		return this.rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the widgetProfile
	 */
	public Widgetprofile getWidgetProfile() {
		return widgetProfile;
	}

	/**
	 * @param widgetProfile the widgetProfile to set
	 */
	public void setWidgetProfile(Widgetprofile widgetProfile) {
		this.widgetProfile = widgetProfile;
	}

	/**
	 * @return the userAccount
	 */
	public Useraccount getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(Useraccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @return the comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

}