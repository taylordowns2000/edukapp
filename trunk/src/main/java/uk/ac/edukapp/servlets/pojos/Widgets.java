package uk.ac.edukapp.servlets.pojos;


import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;


import uk.ac.edukapp.exceptions.RESTException;
import uk.ac.edukapp.model.Category;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.ExtendedWidgetProfile;
import uk.ac.edukapp.renderer.Renderer;
import uk.ac.edukapp.renderer.SearchResults;
import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.CategoryService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;
import uk.ac.edukapp.service.WookieService;
import uk.ac.edukapp.util.Message;

@Path("widgets")
public class Widgets {
	private static final Log log = LogFactory.getLog(Tags.class);
	
	@GET
	@Path("{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ExtendedWidgetProfile getWidget(@Context HttpServletRequest request,
											@PathParam("widgetId") String widgetId){
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		if ( wp == null ) {
			String errorString = "Cannot find widgetprofile id: "+widgetId;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);			
		}
		wp.getTags();
		wp.getActivities();
		wp.getDescription();
		wp.getFunctionalities();
		
		// make extended widget profile

		ExtendedWidgetProfile extendedWidgetProfile = new ExtendedWidgetProfile();
		extendedWidgetProfile.setWidgetProfile(wp);
		extendedWidgetProfile.setRenderInfo(Renderer.render(wp, true));
		extendedWidgetProfile.setRenderUrl(Renderer.render(wp, false));
		ActivityService activityService = new ActivityService(ctx);
		extendedWidgetProfile.setUploadedBy(activityService.getUploadedBy(wp));
		WidgetStatsService widgetStatsService = new WidgetStatsService(ctx);
		extendedWidgetProfile.setWidgetStats(widgetStatsService.getStats(wp));
		
		return extendedWidgetProfile;
	}
	

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
	
	@GET
	@Path("featured")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Widgetprofile> getFeaturedWidgets ( @Context HttpServletRequest request ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		List<Widgetprofile> widgetProfiles = widgetProfileService.findFeaturedWidgetProfiles();

		return widgetProfiles;
	}
	
	
	
	@GET
	@Path("categories/{categoryIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResults getWidgetsByCategoryList(@Context HttpServletRequest request,
													@PathParam("categoryIds") String categoryIds ) {
		ServletContext ctx = request.getSession().getServletContext();
		CategoryService categoryService = new CategoryService (ctx);
		List<List<Category>> categories = categoryService.getCategories(categoryIds);
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		return widgetProfileService.returnWidgetProfilesFilteredByCategories(categories);
	}
	
	
	@GET
	@Path("name/{widgetName}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean checkIfWidgetExists(@Context HttpServletRequest request, @PathParam("widgetName") String widgetName )
	{
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		int n = widgetProfileService.countProfilesByName(widgetName);
		return (n > 0);
		
	}
	
	@GET
	@Path("check/{widgetName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message checkIfUserCanEdit(@Context HttpServletRequest request, @PathParam("widgetName") String widgetName )
	{
		Message msg = new Message();
		msg.setMessage("OK");
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		Widgetprofile wp = widgetProfileService.findWidgetProfileByName(widgetName);
		if ( wp != null ) {
			Useraccount owner = wp.getOwner();
			if ( owner == null || userAccount.equals(owner) ) {
				msg.setMessage("EXISTS");
			}
			else {
				msg.setMessage("NOTALLOWED");
			}
		}
		return msg;
	}
	
	
	@POST
	@Path("categories/add/{widgetId}/{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message addCategoryToWidget ( @Context HttpServletRequest request,
											@PathParam("widgetId") String widgetId,
											@PathParam("categoryId") String categoryId )
	{
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		ServletContext ctx = request.getSession().getServletContext();
		CategoryService categoryService = new CategoryService (ctx);
		Category category = categoryService.getCategoryForId(categoryId);
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);
		
		return widgetProfileService.addCategory(widgetProfile, category);
	}
	
	
	@DELETE
	@Path("categories/remove/{widgetId}/{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeCategoryFromWidget ( @Context HttpServletRequest request,
												@PathParam("widgetId") String widgetId,
												@PathParam("categoryId") String categoryId )
	{
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		ServletContext ctx = request.getSession().getServletContext();
		CategoryService categoryService = new CategoryService (ctx);
		Category category = categoryService.getCategoryForId(categoryId);
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);
		return widgetProfileService.removeCategory(widgetProfile, category);	
	}
	
	
	
	@PUT
	@Path("featured/toggle/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message toggleFeaturedForWidget ( @Context HttpServletRequest request,
												@PathParam("widgetId") String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Message msg = wps.toggleFeatured(widgetId);
		wps.rebuildFeaturedWidgetsCache();
		return msg;
	}
	
	
	
	
	@PUT
	@Path("publish_level/{widgetId}/{level}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message setPublishLevel (@Context HttpServletRequest request,
									@PathParam("widgetId") String widgetId,
									@PathParam("level") int level)
	{
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException (Response.Status.UNAUTHORIZED);
		}
		WidgetProfileService wps = new WidgetProfileService(ctx);
		
		return wps.setPublishLevel(widgetId, level);
	}
	
	@DELETE
	@Path("delete/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message deleteWidget ( @Context HttpServletRequest request,
									@PathParam("widgetId") String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService ( ctx );
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		if ( wp == null ) {
			throw new WebApplicationException ( new RESTException ( "Widget id: "+widgetId+" Not Found"), Response.Status.NOT_FOUND);
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		String widgetURI = wp.getWidId();
		try {
			WookieService wookieService = new WookieService();
			wookieService.removeWidgetFromWookie ( widgetURI );
			String language = "en";
			SolrConnector.getInstance().delete(wp, language);
			Message msg = wps.deleteWidgetProfileWithId ( widgetId, true );
			ActivityService acs = new ActivityService(ctx);
			acs.addUserActivity(userAccount.getId(), "delete", wp.getId());
			return msg;
		}
		catch ( Exception e ) {
			throw new WebApplicationException ( e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@DELETE
	@Path("fulldelete/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message fullDeleteWidget (@Context HttpServletRequest request,
									@PathParam("widgetId") String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService ( ctx );
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		if ( wp == null ) {
			throw new WebApplicationException ( new RESTException ( "Widget id: "+widgetId+" Not Found"), Response.Status.NOT_FOUND);
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		String widgetURI = wp.getWidId();
		try {
			WookieService wookieService = new WookieService();
			wookieService.removeWidgetFromWookie ( widgetURI );
			SolrConnector.getInstance().index();
			Message msg = wps.deleteWidgetProfileWithId ( widgetId, true );
			ActivityService acs = new ActivityService(ctx);
			acs.addUserActivity(userAccount.getId(), "delete", wp.getId());
			return msg;
		}
		catch ( Exception e ) {
			throw new WebApplicationException ( e, Response.Status.INTERNAL_SERVER_ERROR);
		}
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
