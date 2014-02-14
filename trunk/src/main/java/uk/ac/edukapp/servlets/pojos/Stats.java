package uk.ac.edukapp.servlets.pojos;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.service.WidgetStatsService;
import uk.ac.edukapp.util.Message;


@Path("stats")
public class Stats {
	
	private static final int INC_VIEWS = 1;
	private static final  int INC_EMBEDS = 2;
	private static final  int INC_DOWNLOADS = 3;
	
	@POST
	@Path("views/{widgetid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message incrementViews ( @Context HttpServletRequest request, @PathParam("widgetid") String widgetId ) {
		return incrementAStat ( widgetId, INC_VIEWS, request);
	}
	
	
	@POST
	@Path("emdeds/{widgetid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message incrementEmbeds ( @Context HttpServletRequest request, @PathParam("widgetid") String widgetId ) {
		return incrementAStat ( widgetId, INC_EMBEDS, request);
	}
	
	
	@POST
	@Path("downloads/{widgetid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message incrementDownloads ( @Context HttpServletRequest request, @PathParam("widgetid") String widgetId ) {
		return incrementAStat ( widgetId, INC_DOWNLOADS, request);
	}
	
	
	private Message incrementAStat ( String widgetId, int theStat, HttpServletRequest request ) {
		ServletContext ctx = request.getSession().getServletContext();
		
		WidgetProfileService wps = new WidgetProfileService ( ctx );
		
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		
		WidgetStatsService wst = new WidgetStatsService ( ctx );
		
		Message m;
		
		switch (theStat ) {
		case INC_VIEWS:
			m = wst.addToViews(wp);
			break;
			
		case INC_EMBEDS:
			m = wst.addToEmbeds(wp);
			break;
			
		case INC_DOWNLOADS:
			m = wst.addToDownloads(wp);
			break;
			
		default:
			m = new Message();
			m.setMessage("Internal Error, unrecognised increment");
			break;
		}
		
		return m;
		
		
	}
		
}
