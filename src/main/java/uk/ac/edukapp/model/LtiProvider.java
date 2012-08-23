package uk.ac.edukapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * An LTI provider
 * @author scottw
 *
 */
@Entity
@Table(name="ltiprovider")
@NamedQueries({
	@NamedQuery(name="ltiprovider.findByConsumerKey", query="select l from LtiProvider l where l.consumerKey = :key")
})
public class LtiProvider {

  public LtiProvider() {
  }

  public LtiProvider(Useraccount userAccount){
		this.id = userAccount.getId();
		this.consumerKey = "TEST";
		this.consumerSecret = "TEST";
	}
	
	@Id
	@Column(unique=true, nullable=false)
	private int id;
	
	/**
	 * The consumer key
	 */
	@Column
	private String consumerKey;
	
	/**
	 * The consumer secret
	 */
	@Column
	private String consumerSecret;


	/**
	 * @return the consumerKey
	 */
	public String getConsumerKey() {
		return consumerKey;
	}

	/**
	 * @param consumerKey the consumerKey to set
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	/**
	 * @return the consumerSecret
	 */
	public String getConsumerSecret() {
		return consumerSecret;
	}

	/**
	 * @param consumerSecret the consumerSecret to set
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
