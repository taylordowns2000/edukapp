package uk.ac.edukapp.renderer;

import uk.ac.edukapp.model.Widgetprofile;

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

/**
 * Renderer for metadata returned by API calls
 * @author scott.bradley.wilson@gmail.com
 */
public class MetadataRenderer {

	public static String render(Widgetprofile widgetProfile){
		return widgetProfile.getName()+"\n";
	}
}
