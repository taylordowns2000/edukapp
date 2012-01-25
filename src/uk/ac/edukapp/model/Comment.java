package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the comments database table.
 * 
 */
@Entity
@Table(name="comments")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=1024)
	private String commenttext;

    public Comment() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommenttext() {
		return this.commenttext;
	}

	public void setCommenttext(String commenttext) {
		this.commenttext = commenttext;
	}

}