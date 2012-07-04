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
@Table(name = "userreview")
@NamedQueries({ @NamedQuery(name = "Userreview.findForWidgetProfile", query = "SELECT r FROM Userreview r WHERE r.widgetProfile = :widgetprofile") })
public class Userreview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comment_id", referencedColumnName = "id")
	private Comment comment;

	@Column(nullable = false)
	private Date time;

	@JsonIgnore
	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "widgetprofile_id", referencedColumnName = "id")
	private Widgetprofile widgetProfile;

	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Useraccount userAccount;

	public Userreview() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
	 * @param widgetProfile
	 *            the widgetProfile to set
	 */
	public void setWidgetProfile(Widgetprofile widgetProfile) {
		this.widgetProfile = widgetProfile;
	}

	/**
	 * @return the userAccount
	 */
	@JsonIgnore
	public Useraccount getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount
	 *            the userAccount to set
	 */
	public void setUserAccount(Useraccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @return the comment
	 */
	@JsonIgnore
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	/**
	 * Return the actual comment text, e.g. for serialization
	 * 
	 * @return
	 */
	public String getText() {
		return this.getComment().getCommenttext();
	}

	/**
	 * Return just the user name, e.g. for serialization
	 * 
	 * @return
	 */
	public String getUser() {
		return this.getUserAccount().getUsername();
	}
	
	/**
	 * Return profile page link URL for serialization
	 * 
	 * @return
	 */
	public String getUserProfile(){
		//
		// If this is an "external review" use the account website url
		//
		if (this.getUserAccount().getId() == 0){
			return this.getUserAccount().getAccountInfo().getWebsite();
		//
		// Otherwise return a link to the user profile page
		//
		} else {
			return "/user/"+this.getUserAccount().getId();
		}
	}

}