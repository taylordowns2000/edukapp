package uk.ac.edukapp.renderer;

import java.io.IOException;

import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;

import uk.ac.edukapp.repository.SolrConnector;



public class WidgetRenderer {
  
  private static WidgetRenderer renderer = new WidgetRenderer();
  
  private WookieConnectorService conn;
  
  
  private WidgetRenderer(){
    try {
      conn = new WookieConnectorService("http://widgets.open.ac.uk:8080/wookie/", "TEST", "myshareddata");      
    }catch(WookieConnectorException wce){
      wce.printStackTrace();
    }
  }  
  
  
  public static WidgetRenderer getInstance() {
    return renderer;
  }
  
  /**
   * Returns html markup of widget instance as an iframe
   * @param the id (typically a uri) of widget to render
   * @return html-ready code as a string
   */
	public String render(String uri){
	  String html = "";
	  WidgetInstance widgetInstance = null;
	  try {
	    conn.setCurrentUser(new User("edukapp-front-page","edukapp-front-page"));
	    widgetInstance = conn.getOrCreateInstance(uri);
	  }catch(IOException ioe){
	    ioe.printStackTrace();
	    return null;
	  }catch(WookieConnectorException wce){
	    wce.printStackTrace();
	    return null;
	  }
	  html+="<p>"+uri+"</p>";
	  html+="<iframe src=\""+widgetInstance.getUrl()+"\"";
	  html+=" width=\""+"100"+"\"";
	  html+=" height=\""+"100"+"\"";
	  html+="></iframe>";
	  return html;
	}
}
