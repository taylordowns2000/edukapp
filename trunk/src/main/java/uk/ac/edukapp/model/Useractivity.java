package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the useractivities database table.
 * 
 */
@Entity
@Table(name = "useractivities")
@NamedQueries({ @NamedQuery(name = "Useractivity.uploaded", query = "SELECT act "
		+ "FROM Useractivity act WHERE (act.objectId = :objectId AND act.activity='uploaded')") })
public class Useractivity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false, length = 25)
	private String activity;

	@Column(name = "object_id", nullable = false)
	private int objectId;

	@Column(name = "subject_id", nullable = false)
	private int subjectId;

	@Column(nullable = false)
	private Timestamp time;

	public Useractivity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}