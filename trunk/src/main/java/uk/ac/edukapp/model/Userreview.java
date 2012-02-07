package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the userreview database table.
 * 
 */
@Entity
@Table(name="userreview")
public class Userreview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="comment_id", nullable=false)
	private int commentId;

	@Column(nullable=false)
	private byte rating;

	@Column(nullable=false)
	private Timestamp time;

	@Column(name="user_id", nullable=false)
	private int userId;

	@Column(name="widgetprofile_id", nullable=false)
	private int widgetprofileId;

    public Userreview() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCommentId() {
		return this.commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public byte getRating() {
		return this.rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getWidgetprofileId() {
		return this.widgetprofileId;
	}

	public void setWidgetprofileId(int widgetprofileId) {
		this.widgetprofileId = widgetprofileId;
	}

}