package uk.ac.edukapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the openjpa_sequence_table database table.
 * 
 */
@Entity
@Table(name="openjpa_sequence_table")
public class OpenjpaSequenceTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID", unique=true, nullable=false)
	private byte id;

	@Column(name="SEQUENCE_VALUE")
	private BigInteger sequenceValue;

    public OpenjpaSequenceTable() {
    }

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public BigInteger getSequenceValue() {
		return this.sequenceValue;
	}

	public void setSequenceValue(BigInteger sequenceValue) {
		this.sequenceValue = sequenceValue;
	}

}