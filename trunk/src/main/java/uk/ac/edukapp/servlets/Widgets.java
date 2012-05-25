package uk.ac.edukapp.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import uk.ac.edukapp.exceptions.RESTException;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.service.WidgetProfileService;

@Path("widgets")
public class Widgets {

	@GET
	@Path("search/{query}/{start}/{rows}")
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResults searchWidgets (@Context HttpServletRequest request, 
									@PathParam("query") String query, 
									@PathParam("start") String start, 
									@PathParam("rows") String rows ) throws RESTException {
		
		// had to get the context from the session because this request is actually a ShiroHttpServletRequest
		ServletContext ctx = request.getSession().getServletContext();
		return this.searchWidgetsWithGivenOrdering(query, start, rows, null, ctx);
	}
	
	@GET
	@Path("search/{query}/{start}/{rows}/{orderby}")
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResults searchWidgetsOrderBy (@Context HttpServletRequest request, 
									@PathParam("query") String query, 
									@PathParam("start") String start, 
									@PathParam("rows") String rows,
									@PathParam("orderby") String orderby ) throws RESTException {
		ServletContext ctx = request.getSession().getServletContext();
		return this.searchWidgetsWithGivenOrdering(query, start, rows, orderby, ctx);
	}
	
	
	private SearchResults searchWidgetsWithGivenOrdering ( String query, 
										String start, String rows, 
										String orderbyterm, ServletContext ctx ) throws RESTException {
		int offset, result_size;
		try {
			offset = Integer.parseInt(start);
			result_size = Integer.parseInt(rows);
		} catch ( NumberFormatException e ) {
			offset = 0;
			result_size = 10;
		}
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		SearchResults searchResults = null;
		String language = "en";
		if ( orderbyterm == null ) {
			searchResults = widgetProfileService.searchWidgetProfilesOrderedByRelevance(query, language, result_size, offset);
		}
		else if ( orderbyterm.equalsIgnoreCase ( "date")) {
			searchResults = widgetProfileService.searchWidgetProfilesOrderedByDate(query, language, result_size, offset);
		}
		else if ( orderbyterm.equalsIgnoreCase("rating")) {
			searchResults = widgetProfileService.searchWidgetProfilesOrderedByRating(query, language, result_size, offset);
		}
		else if ( orderbyterm.equalsIgnoreCase("popularity")) {
			searchResults = widgetProfileService.searchWidgetProfilesOrderedByPopularity(query, language, result_size, offset);
		}
		else {
			throw new RESTException ( "Orderby term: "+orderbyterm+" not recognized");
		}
		
		if ( searchResults == null ) {
			// return an empty list
			return new SearchResults();
		}
		return searchResults;
		
	}
}
