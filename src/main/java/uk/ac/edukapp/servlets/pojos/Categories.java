/*
 *  (c) 2013 University of Bolton
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

package uk.ac.edukapp.servlets.pojos;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.edukapp.model.Category;
import uk.ac.edukapp.service.CategoryService;
import uk.ac.edukapp.util.Message;
import uk.ac.edukapp.util.ServletUtils;

@Path("categories")
public class Categories {
	private static final Log log = LogFactory.getLog(Categories.class);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<List<Category>> getAllCategories (@Context HttpServletRequest request) {
		ServletContext ctx = request.getSession().getServletContext();
		CategoryService categoryService = new CategoryService(ctx);
		return categoryService.getAllCategories();
	}
	
	
	@GET
	@Path("ids/{idList}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<List<Category>> getCategoriesInList (@Context HttpServletRequest request,
													@PathParam ("idList") String idList ) {
		ServletContext ctx = request.getSession().getServletContext();
		CategoryService categoryService = new CategoryService(ctx);
		return categoryService.getCategories(idList);
	}
	
	@POST
	@Path("edit/{title}/{group}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message newCategory(@Context HttpServletRequest request,
								@PathParam("title") String title,
								@PathParam("group") String group ) {
		ServletContext ctx = request.getSession().getServletContext();
		
		if ( !ServletUtils.isNumeric(group)) {
			Message msg = new Message();
			msg.setMessage("Error: group must be an integer");
			return msg;
		}
		
		CategoryService categoryService = new CategoryService(ctx);
		
		return categoryService.addCategory(title, Integer.valueOf(group));
	}

}
