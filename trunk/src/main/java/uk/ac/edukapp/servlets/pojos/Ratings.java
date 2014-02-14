package uk.ac.edukapp.servlets.pojos;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

import uk.ac.edukapp.cache.Cache;
import uk.ac.edukapp.exceptions.RESTException;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userrating;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.UserAccountService;
import uk.ac.edukapp.service.UserRateService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;
import uk.ac.edukapp.util.Message;

@Path("ratings")
public class Ratings {
	private static final Log log = LogFactory.getLog(Ratings.class);
	
	
	@GET
	@Path ("average/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Number getAverageRatingForWidget ( @Context HttpServletRequest request,
												@PathParam("widgetId") String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile widgetProfile = wps.findWidgetProfileById(widgetId);
		UserRateService userRateService = new UserRateService ( ctx );
		return userRateService.getAverageRating(widgetProfile);
	}
	
	

	@GET
	@Path ("{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Userrating getUserRatingForWidget ( @Context HttpServletRequest request,
												@PathParam ( "widgetId" ) String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService( ctx );
		Widgetprofile widgetProfile = wps.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			String errorMessage = "Unable to find widget for id: "+widgetId;
			log.debug(errorMessage);
			throw new WebApplicationException ( new RESTException(errorMessage), Response.Status.NOT_FOUND);
		}
		Useraccount userAccount = (Useraccount)SecurityUtils.getSubject().getPrincipal();;
		if ( userAccount == null ) {
			String errorString = "User not logged in!";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		try {
			UserRateService userRateService = new UserRateService ( ctx );
			return userRateService.getUserRatingForWidget(widgetProfile, userAccount);
		}
		catch ( NoResultException e ) {
			Userrating ur = new Userrating();
			ur.setRating((byte) 0);
			ur.setUserAccount(userAccount);
			ur.setWidgetProfile(widgetProfile);
			return ur;
			
		}
	}
	
	
	@POST
	@Path("edit/{widgetId}/{rating}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setUserRatingForWidget ( @Context HttpServletRequest request,
											@PathParam ( "widgetId" ) String widgetId,
											@PathParam ( "rating" ) String rating ) {
		//Useraccount loggedUser = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		//if ( loggedUser == null || !loggedUser.getUsername().equals(username) ) {
		//	throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		//}		
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService( ctx );
		Widgetprofile widgetProfile = wps.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			String errorMessage = "Unable to find widget for id: "+widgetId;
			log.debug(errorMessage);
			throw new WebApplicationException ( new RESTException(errorMessage), Response.Status.NOT_FOUND);
		}
		Useraccount userAccount = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String errorString = "Not logged in";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		UserRateService userRateService = new UserRateService ( ctx );
		Message msg = userRateService.publishUserRate(rating, userAccount, widgetProfile);
		
		Cache.getInstance().remove("widgetStats:"+widgetProfile.getId());
		WidgetStatsService widgetStatsService = new WidgetStatsService(ctx);

		// this updates the ratings part of the stats for the widget
		// don't really like magic functions but there you go
		widgetStatsService.updateWidgetRatings(widgetProfile);

		
		if ( msg.getMessage().equals("OK")) {
			return true;
		}
		else {
			log.debug(msg.getMessage());
			throw new WebApplicationException ( new RESTException ( msg.getMessage()), Response.Status.NOT_MODIFIED );
		}
	}
}
