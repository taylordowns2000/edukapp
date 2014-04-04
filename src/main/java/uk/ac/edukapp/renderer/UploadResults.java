/*
 *  (c) 2014 University of Bolton
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
