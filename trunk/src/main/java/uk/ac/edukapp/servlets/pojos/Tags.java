package uk.ac.edukapp.servlets.pojos;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.TagService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.util.Message;

@Path("tags")
public class Tags {
	
	private static final Log log = LogFactory.getLog(Tags.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> getAllTags ( @Context HttpServletRequest request ) {
		ServletContext ctx = request.getSession().getServletContext();
		TagService tagService = new TagService ( ctx );
		return tagService.getAllTags();
	}
	
	
	@GET
	@Path("popular")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> getPopularTags ( @Context HttpServletRequest request ) {
		ServletContext ctx = request.getSession().getServletContext();
		TagService tagService = new TagService ( ctx );
		return tagService.getPopularTags();
	}
	
	
	@GET
	@Path("widgets/{tagid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Widgetprofile> getWidgetsForTag ( @Context HttpServletRequest request,
													@PathParam("tagid") String tagid ) {
		ServletContext ctx = request.getSession().getServletContext();
		TagService ts = new TagService(ctx);
		Tag t = ts.getTag(tagid);
		
		WidgetProfileService wps = new WidgetProfileService ( ctx );
		return wps.findWidgetProfilesForTag(t);
	}
	
	
	/**
	 * addTagToWidget - will either add a new tag to a widget if the tag text doesn't exist or will add the existing tag if it does
	 * @param request
	 * @param widgetId
	 * @param username
	 * @param tagText
	 * @return
	 * @throws Exception 
	 */
	
	@POST
	@Path ( "edit/{widgetId}" )
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Message addTagToWidget ( @Context HttpServletRequest request,
										@PathParam ("widgetId") String widgetId,
										@FormParam ( "tag") String tagText ) throws Exception {

		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService (ctx );
		Widgetprofile widgetProfile = wps.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			String errorMessage = "Unable to find widget for id: "+widgetId;
			log.debug(errorMessage);
			throw new WebApplicationException ( new RESTException(errorMessage), Response.Status.NOT_FOUND);
		}
		//UserAccountService userAccountService = new UserAccountService ( ctx );
		//Useraccount userAccount = userAccountService.getUserAccount(username);
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String errorString = "Not logged in!";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		Message msg = wps.addTag(widgetProfile, tagText);
		if ( msg.getMessage().equals("OK")) {
			ActivityService activityService = new ActivityService ( ctx );
			activityService.addUserActivity(userAccount.getId(), "add tag", widgetProfile.getId());
		}
		return msg;
		
	}
	
	// for some reason can't get jquery to send delete successfully, it fails on the OPTIONS preflight
	@DELETE
	@Path ("edit/{widgetId}/{tagId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeTagFromWidget ( @Context HttpServletRequest request,
											@PathParam("widgetId") String widgetId,
											@PathParam("tagId") String tagId ) {
		Useraccount loggedUser = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( loggedUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}		
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile widgetProfile = wps.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			String errorMessage = "Unable to find widget for id: "+widgetId;
			log.debug(errorMessage);
			throw new WebApplicationException ( new RESTException(errorMessage), Response.Status.NOT_FOUND);
		}
		TagService ts = new TagService ( ctx );
		Tag tag = ts.getTag(tagId);
		if ( tag == null ) {
			String errorMessage = "Unable to find tag for id: "+tagId;
			log.debug(errorMessage);
			throw new WebApplicationException ( new RESTException ( errorMessage ),Response.Status.NOT_FOUND);
		}
		return wps.removeTagFromWidget(widgetProfile, tag);
		
	}

}
