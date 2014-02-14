package uk.ac.edukapp.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

import uk.ac.edukapp.server.configuration.WookieServerConfiguration;

public class WookieService {

	public int uploadWidgetToWookie(File widgetFile) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		WookieServerConfiguration wookie = WookieServerConfiguration
			.getInstance();
		client.getState().setCredentials(wookie.getAuthScope(),
				wookie.getCredentials());

		PostMethod postMethod = new PostMethod(
				wookie.getWookieServerLocation() + "/widgets");
		FilePart filePart = new FilePart("widgetFile", widgetFile);
		Part[] parts = { filePart };
		postMethod.setRequestEntity(new MultipartRequestEntity(parts,
				postMethod.getParams()));

		int status = client.executeMethod(postMethod);

		return status;
}


	public void removeWidgetFromWookie(String widgetId) throws WookieConnectorException, ConfigurationException, IOException {
		PropertiesConfiguration properties = new PropertiesConfiguration("store.properties");
		String sharedDataKey = "storeTestData";
		String adminUsername = "java";
		String adminPassword = "java";
		
		if ( properties.containsKey("wookie.server.sharedDataKey")) {
			sharedDataKey = properties.getString("wookie.server.sharedDataKey");
		}
		if ( properties.containsKey("wookie.server.username")) {
			adminUsername = properties.getString("wookie.server.username");
		}
		if ( properties.containsKey("wookie.server.password")) {
			adminPassword = properties.getString("wookie.server.password");
		}
		WookieConnectorService connector = WookieServerConfiguration.getInstance().getWookieConnector(sharedDataKey);
		connector.deleteWidget(widgetId, adminUsername, adminPassword);
	}

}
