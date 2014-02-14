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
import uk.ac.edukapp.model.Functionality;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.WidgetFunctionality;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.FunctionalityService;
import uk.ac.edukapp.service.UserAccountService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.util.Message;

@Path("functionalities")
public class Functionalities {
	private static final Log log = LogFactory.getLog(Functionalities.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Functionality> getAllFunctionalties(@Context HttpServletRequest request) {
		ServletContext ctx = request.getSession().getServletContext();
		FunctionalityService fs = new FunctionalityService(ctx);
		
		return fs.getAllFunctionalities();
	}
	
	@GET
	@Path("{level}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Functionality> getFunctionalitiesForLevel ( @Context HttpServletRequest request,
															@PathParam("level") int level ) {
		ServletContext ctx = request.getSession().getServletContext();
		FunctionalityService fs = new FunctionalityService(ctx);
		return fs.getFunctionalitiesForLevel(level);
	}
	
	
	@POST
	@Path("edit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public WidgetFunctionality addFunctionalityToWidget(@Context HttpServletRequest request,
														@FormParam("widget_id") String widget_id,
														@FormParam("functionality_id") String functionality_id,
														@FormParam("relevance") int relevance ) {
		
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String errorString = "Not logged in";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		WidgetProfileService widgetProfileService = new WidgetProfileService ( ctx );
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widget_id);
		if ( widgetProfile == null ) {
			String errorString = "Failed to find widget profile for id: "+widget_id;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString ),
												Response.Status.NOT_FOUND );
		}
		
		FunctionalityService fs = new FunctionalityService(ctx);
		Functionality functionality = fs.getFunctionalityForId(functionality_id);
		if ( functionality == null ) {
			String errorString = "Failed to find functionality for id: "+functionality_id;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString ),
					Response.Status.NOT_FOUND );
		}
		return fs.addFunctionalityToWidget(widgetProfile, functionality, relevance);
				
	}
	
	
	@DELETE
	@Path("edit/{widgetId}/{funcId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeFunctionalityFromWidgetGET(@Context HttpServletRequest request,
													@PathParam("widgetId") String widgetId,
													@PathParam("funcId") String funcId)
	{
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String errorString = "Not logged in";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString),
													Response.Status.NOT_FOUND);
		}
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		if ( wp == null ) {
			String errorString = "Failed to find widget profile for id: "+widgetId;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException (errorString ),
												Response.Status.NOT_FOUND );
		}
		FunctionalityService fs = new FunctionalityService(ctx);
		Functionality  f = fs.getFunctionalityForId(funcId);
		if ( f == null ) {
			String errorString = "Failed to find functionality for id: "+funcId;
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException ( errorString ), Response.Status.NOT_FOUND);
		}
		return fs.removeFunctionalityFromWidget(wp, f);
		
	}

}
