package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the test database table.
 * 
 */
@Entity
@Table(name="test")
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=22)
	private String name;

	@Column(nullable=false)
	private float salary;

    public Test() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getSalary() {
		return this.salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

}