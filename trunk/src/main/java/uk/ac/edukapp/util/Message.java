package uk.ac.edukapp.util;

/*
 * class to model message returned when invoking REST api
 *  
 */
public class Message {
	private String message;
	private String id;

	// default constructor
	public Message() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
