package uk.ac.edukapp.renderer;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.server.configuration.WookieServerConfiguration;

public class WidgetRenderer {

	private static WidgetRenderer renderer = new WidgetRenderer();

	private WookieConnectorService conn;

	private WidgetRenderer() {
		try {
			conn = WookieServerConfiguration.getInstance().getWookieConnector("myshareddata");
		} catch (WookieConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static WidgetRenderer getInstance() {
		return renderer;
	}
	
	public String getDownloadUrl(String uri){
		return conn.getConnection().getURL()+"/widgets/"+uri+"?format=application/widget";
	}
	
	/**
	 * Returns html markup of widget instance as an iframe
	 * 
	 * @param the
	 *            id (typically a uri) of widget to render
	 * @return html-ready code as a string
	 */
	public String render(String uri, int width, int height, boolean wrap) {
		String html = "";
		WidgetInstance widgetInstance = null;
		try {
			setUser();
			widgetInstance = conn.getOrCreateInstance(uri);
			if (widgetInstance.getWidth() != null && widgetInstance.getWidth().trim().length() > 0) width = Integer.parseInt(widgetInstance.getWidth());
			if (widgetInstance.getHeight() != null && widgetInstance.getHeight().trim().length() > 0) height = Integer.parseInt(widgetInstance.getHeight());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (WookieConnectorException wce) {
			wce.printStackTrace();
			return null;
		}

		//
		// If we don't need a wrapped version, just return the URL
		//
		if (!wrap) return widgetInstance.getUrl();
		
		//
		// Return Iframe wrapper HTML snippet
		//
		html += "<iframe src=\"" + widgetInstance.getUrl() + "\"";
		html += " width=\"" + width + "\"";
		html += " height=\"" + height + "\"";
		html += "></iframe>";
		return html;
	}
	
	public String render(String url, boolean wrap){
		return render(url, 500, 300, wrap);		
	}
	
	/**
	 * Set the user - this will be the current logged-in user if available
	 */
	private void setUser(){
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if (userAccount != null){
			User user = new User(String.valueOf(userAccount.getId()), userAccount.getUsername());
			conn.setCurrentUser(user);
		} else {
			conn.setCurrentUser(new User("Guest","Guest"));
		}
	}

}
