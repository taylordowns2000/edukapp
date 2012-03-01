/*
 *  (c) 2012 University of Bolton
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

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

import uk.ac.edukapp.model.Widgetprofile;

/**
 * Widget Renderer for LTI requests
 * 
 * Currently Wookie-only
 * @author scottw
 * 
 */
public class LtiRenderer {

	private WookieConnectorService conn;
	private static LtiRenderer renderer;

	private LtiRenderer() {
	}
	
	public WookieConnectorService getConnector(String sharedDataKey){
		try {
			conn = WookieServerConfiguration.getInstance().getWookieConnector(sharedDataKey);
		} catch (WookieConnectorException wce) {
			wce.printStackTrace();
		}
		return conn;
	}

	public static LtiRenderer getInstance() {
		if (renderer == null) renderer = new LtiRenderer();
		return renderer;
	}

	public String render(Widgetprofile widgetProfile,
			Map<String, String[]> parameters) {


		String sharedDataKey = "";	
		
		//
		// If there is a context_id, append it to the shared data key
		//
		if(parameters.containsKey("context_id")){
			sharedDataKey += ":" + parameters.get("context_id")[0]; //$NON-NLS-1$
		}
		
		//
		// If there is a resource_link_id, append it to the shared data key
		//
		if(parameters.containsKey("resource_link_id")){
			sharedDataKey += ":" + parameters.get("resource_link_id")[0]; //$NON-NLS-1$
		}

		//
		// The user (viewer) information
		//
		String userId = null;
		if(parameters.containsKey("user_id")){
			userId = parameters.get("user_id")[0]; //$NON-NLS-1$
		}
		String userName = null;
		if(parameters.containsKey("lis_person_name_full")){
			userName = parameters.get("lis_person_name_full")[0]; //$NON-NLS-1$
		}
		String userImage = null;
		if(parameters.containsKey("user_image")){
			userImage = parameters.get("user_image")[0]; //$NON-NLS-1$
		}
		//
		// Create a user
		//
		User user = new User(userId, userName, userImage);


		//
		// Get the consumer key for the request
		//
		String consumerKey = null;
		if(parameters.containsKey("user_id")){
			consumerKey = parameters.get("oauth_consumer_key")[0];
		}

		//
		// Get locale
		// FIXME use this
		//
		String[] locale = parameters.get("launch_presentation_locale");

		//
		// Get instance
		//
		WidgetInstance widgetInstance = null;
		try {
			//
			// The actual shared data key == consumerkey:contextid:resourcelinkid
			//
			this.conn = this.getConnector(consumerKey+":"+sharedDataKey);
			conn.setCurrentUser(user);
			widgetInstance = conn.getOrCreateInstance(widgetProfile.getWidId());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (WookieConnectorException wce) {
			wce.printStackTrace();
			return null;
		}

		//
		// Set some properties, e.g. participant info and roles
		//
		setParticipant(widgetInstance, user);

		//
		// Set owner property
		//
		setOwner(widgetInstance, parameters);

		//
		// Set the LTI parameters in the widget preferences as JSON
		//
		setPreferences(widgetInstance, parameters);

		//
		// Return widget instance URL
		//
		String url = widgetInstance.getUrl();

		return url;
	}

	private void setPreferences(WidgetInstance widgetInstance, Map<String, String[]> parameters) {
		for (String name:BASICLTI_PARAMETERS){
			if (parameters.containsKey(name)){
				String value = parameters.get(name)[0];
				setPreference(widgetInstance, name, value);
			}
		}
	}

	private void setOwner(WidgetInstance widgetInstance, Map<String, String[]> parameters) {
		boolean isModerator = false;
		if (parameters.get("roles") != null
				&& parameters.get("roles").length > 0) {
			for (String role : BASICLTI_ADMIN_ROLES) {
				if (StringUtils.containsIgnoreCase(parameters.get("roles")[0],
						role))
					isModerator = true;
			}
		}
		if (isModerator)
			setPreference(widgetInstance,"moderator","true");
	}

	private void setPreference(WidgetInstance widgetInstance, String name, String value) {
		try {
			conn.setPropertyForInstance(widgetInstance, "setpersonalproperty", name, value);
		} catch (WookieConnectorException e) {
			e.printStackTrace();
		}
	}

	private void setParticipant(WidgetInstance widgetInstance, User user) {
		try {
			conn.addParticipant(widgetInstance, user);
		} catch (WookieConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Detailed roles we map to "Owner" for simple authorization in existing
	 * widgets. All the original detailed roles are passed into the widget in
	 * case it can make use of them.
	 */
	private static final String[] BASICLTI_ADMIN_ROLES = { "faculty",
			"instructor", "mentor", "staff", "administrator", "sysadmin",
			"syssupport", "creator", "accountadmin" };

	/**
	 * This is the set of parameters defined in the spec We push all of these
	 * into the widget so that they are available to the widget at runtime, even
	 * though Wookie itself doesn't really use most of them.
	 */
	private static final String[] BASICLTI_PARAMETERS = { "lti_version",
			"lti_lms", "resource_link_id", "resource_link_title",
			"resource_link_description", "user_id", "user_image", "roles",
			"lis_person_name_given", "lis_person_name_family",
			"lis_person_name_full", "list_person_contact_email_primary",
			"context_id", "context_type", "context_title", "context_label",
			"launch_presentation_locale",
			"launch_presentation_document_target",
			"launch_presentation_css_url", "launch_presentation_width",
			"launch_presentation_height", "launch_presentation_return_url",
			"tool_consumer_instance_guid", "tool_consumer_instance_name",
			"tool_consumer_instance_description", "tool_consumer_instance_url",
			"tool_consumer_instance_contact_email", "lis_result_sourcedid",
			"lis_outcome_service_url", "lis_person_sourcedid",
			"lis_course_offering_sourcedid", "lis_course_section_sourcedid" };
}
