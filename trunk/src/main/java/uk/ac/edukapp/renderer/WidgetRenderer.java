package uk.ac.edukapp.renderer;

import java.io.IOException;
import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

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

	/**
	 * Returns html markup of widget instance as an iframe
	 * 
	 * @param the
	 *            id (typically a uri) of widget to render
	 * @return html-ready code as a string
	 */
	public String render(String uri, int width, int height) {
		String html = "";
		WidgetInstance widgetInstance = null;
		try {
			conn.setCurrentUser(new User("edukapp-front-page",
					"edukapp-front-page"));
			widgetInstance = conn.getOrCreateInstance(uri);
			if (widgetInstance.getHeight() != null && widgetInstance.getHeight().trim().length() > 0) height = Integer.parseInt(widgetInstance.getHeight());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (WookieConnectorException wce) {
			wce.printStackTrace();
			return null;
		}
		// html+="<p>"+uri+"</p>";
		html += "<iframe src=\"" + widgetInstance.getUrl() + "\"";
		html += " width=\"" + width + "\"";
		html += " height=\"" + height + "\"";
		html += "></iframe>";
		return html;
	}

	public String render(String uri) {
		return render(uri, 500, 300);
	}

}
