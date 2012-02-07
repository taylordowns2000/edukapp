package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the types database table.
 * 
 */
@Entity
@Table(name="types")
public class Type implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=64)
	private String typetext;

    public Type() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypetext() {
		return this.typetext;
	}

	public void setTypetext(String typetext) {
		this.typetext = typetext;
	}

}