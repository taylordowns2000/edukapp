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
