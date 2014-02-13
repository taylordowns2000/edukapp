package uk.ac.edukapp.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.ac.edukapp.model.Widgetprofile;

public class UploadResults {
	
	private String uploadMessage;
	private Widgetprofile widgetProfile;
	private String widgetFolderPath;
	private List<String> htmlFileList;
	
	
	/**
	 * @return the uploadMessage
	 */
	public String getUploadMessage() {
		return uploadMessage;
	}
	
	
	/**
	 * @param uploadMessage the uploadMessage to set
	 */
	public void setUploadMessage(String uploadMessage) {
		this.uploadMessage = uploadMessage;
	}
	
	
	/**
	 * @return the widgetProfile
	 */
	public Widgetprofile getWidgetProfile() {
		return widgetProfile;
	}
	
	
	/**
	 * @param widgetProfile the widgetProfile to set
	 */
	public void setWidgetProfile(Widgetprofile widgetProfile) {
		this.widgetProfile = widgetProfile;
	}
	
	
	/**
	 * @return the widgetFolderPath
	 */
	public String getWidgetFolderPath() {
		return widgetFolderPath;
	}
	
	
	/**
	 * @param widgetFolderPath the widgetFolderPath to set
	 */
	public void setWidgetFolderPath(String widgetFolderPath) {
		this.widgetFolderPath = widgetFolderPath;
	}
	
	

	public void setHTMLFileList ( List<File> inList ) {
		htmlFileList = new ArrayList<String>();
		for ( File f : inList ) {
			htmlFileList.add(f.getName());
		}
	}
	
	
	public List<String> getHtmlFileList() {
		return htmlFileList;
	}
	
}
