package uk.ac.edukapp.dao;

public class SessionInfo {
	
	private boolean authenticated;
	private int userId;
	private String userName;
	
	
	/**
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}
	/**
	 * @param authenticated the authenticated to set
	 */
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	

}
