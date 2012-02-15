package uk.ac.edukapp.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

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
	
	private static ObjectMapper mapper;
	
	private static ObjectMapper getObjectMapper(){
		if (mapper == null) mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	
	public static void render(OutputStream out, ExtendedWidgetProfile extendedWidgetProfile){
		try {
			getObjectMapper().writeValue(out, extendedWidgetProfile);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void render(OutputStream out, List<Widgetprofile> widgetProfiles){
		try {
			getObjectMapper().writeValue(out, widgetProfiles);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void render(OutputStream out, Widgetprofile widgetProfile){
		try {
			getObjectMapper().writeValue(out, widgetProfile);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String render(Widgetprofile widgetProfile){
		return widgetProfile.getName()+"\n";
	}
	
	public static void renderReviews(OutputStream out, List<Userreview> userReviews){
		try {
			getObjectMapper().writeValue(out, userReviews);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
