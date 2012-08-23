package uk.ac.edukapp.renderer;

import uk.ac.edukapp.server.configuration.StoreConfiguration;

public class GadgetRenderer {

	private static GadgetRenderer renderer = new GadgetRenderer();

	private GadgetRenderer() {
	}

	public static GadgetRenderer getInstance() {
		return renderer;
	}

	public String render(String uri, int width, int height) {

		String html = "";
		
		html += "<iframe src=\""+StoreConfiguration.getInstance().getShindigLocation()+"/gadgets/ifr?url="
				+ uri + "\"";
		html += " width=\"" + "500" + "\"";
		html += " height=\"" + "300" + "\"";
		html += "></iframe>";
		return html;
	}

	public String render(String uri) {
		return render(uri, 500, 300);
	}
}
