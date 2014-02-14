package uk.ac.edukapp.servlets.pojos;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Userreview;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.UserReviewService;
import uk.ac.edukapp.service.WidgetProfileService;

@Path("reviews")
public class Reviews {
	private static final Log log = LogFactory.getLog(Reviews.class);
	
	
	@GET
	@Path("{widgetid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Userreview>getReviews (@Context HttpServletRequest request, @PathParam("widgetid") String widgetId ) {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService widgetProfileService = new WidgetProfileService ( ctx );
		Widgetprofile wp = widgetProfileService.findWidgetProfileById(widgetId);
		if ( wp == null ) {
			log.debug("Failed to find widget profile for id: "+widgetId);
			throw new WebApplicationException ( Response.Status.NOT_FOUND);
		}
		
		UserReviewService userReviewService = new UserReviewService(ctx);
		return userReviewService.getUserReviewsForWidgetProfile(wp);
	}
	
	
	@POST
	@Path("edit/{widgetid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean postReview ( @Context HttpServletRequest request,
								@PathParam("widgetid") String widgetId,
								@FormParam("comment") String comment) {
		

		ServletContext ctx = request.getSession().getServletContext();
		//UserAccountService userAccountService = new UserAccountService(ctx);
		//Useraccount userAccount = userAccountService.getUserAccount(username);
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String errorString = "No user signed in";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		WidgetProfileService widgetProfileService = new WidgetProfileService ( ctx );
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			String errorString = "Failed to fine widget profile for id: "+widgetId;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString ),
												Response.Status.NOT_FOUND );
		}
		UserReviewService userReviewService = new UserReviewService ( ctx );
		return userReviewService.publishUserReview(comment, userAccount, widgetProfile);
	}

}
