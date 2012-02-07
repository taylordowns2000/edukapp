package uk.ac.edukapp.renderer;

import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;



public class WidgetRenderer {
	public static String render(String url){
		
		try {
			WookieConnectorService conn = new WookieConnectorService("http://widgets.open.ac.uk:8080/wookie/", "TEST", "myshareddata");
		} catch (WookieConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}
}
