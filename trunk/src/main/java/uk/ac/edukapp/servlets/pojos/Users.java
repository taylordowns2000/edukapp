package uk.ac.edukapp.servlets.pojos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;


import uk.ac.edukapp.exceptions.RESTException;
import uk.ac.edukapp.model.Accountinfo;
import uk.ac.edukapp.model.LtiProvider;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.WidgetFavourite;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.UserAccountService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.util.MD5Util;
import uk.ac.edukapp.util.Message;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Path("users")
public class Users {
	
	private static final Log log = LogFactory.getLog(Users.class);
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Useraccount> listUsers(@Context HttpServletRequest request) {
		ServletContext ctx = request.getSession().getServletContext();
		UserAccountService uas = new UserAccountService (ctx);
		return uas.listUsers();
	}
	
	@GET
	@Path("withfavourites")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Useraccount> listUsersWithFavourites(@Context HttpServletRequest request ) {
		ServletContext ctx = request.getSession().getServletContext();
		UserAccountService uas = new UserAccountService (ctx );
		return uas.listUsersWithFavourites();
	}
	
	@PUT
	@Path("edit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Useraccount updateUser (@FormParam ("userId") String userId,
									@FormParam("username") String username,
									@FormParam("email") String email,
									@FormParam("password") String password,
									@FormParam("realname") String realname,
									@Context HttpServletRequest request ) {
		
		ServletContext ctx = request.getSession().getServletContext();

		
		UserAccountService uas = new UserAccountService (ctx );
		
		
		return uas.updateUser(Integer.parseInt(userId), username, email, password, realname);
	}
	

	@POST
	@Path("edit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Useraccount newUser ( @FormParam("username") String username,
			@FormParam("email") String email,
			@FormParam("password") String password,
			@FormParam("realname") String realname,
			@Context HttpServletRequest request) throws Exception {
		ServletContext ctx = request.getSession().getServletContext();
		
		// check to see if user already exists
		UserAccountService uas = new UserAccountService ( ctx );
		Useraccount olduser = uas.getUserAccount(username);
		if ( olduser != null ) {
			throw new WebApplicationException ( Response.Status.CONFLICT);
		}
		return uas.registerNewUser(username, email, password, realname);
		
/*		em.getTransaction().begin();
		
		Useraccount user = new Useraccount();
		user.setUsername(username);
		user.setEmail(email);
		
		UUID token = UUID.randomUUID();
		String salt = token.toString();
		String hashedPassword = MD5Util.md5Hex(salt + password);
		user.setPassword(hashedPassword);
		user.setSalt(salt);
		em.persist(user);
		
		try {
			log.info("User created with id: " + user.getId());
			Accountinfo info = new Accountinfo();
			info.setId(user.getId());
			info.setRealname(realname);
			info.setJoined(new Timestamp(new Date().getTime()));
			em.persist(info);
			LtiProvider lti = new LtiProvider(user);
			em.persist(lti);
			
		}
		catch ( Exception e ) {
			log.info("Exception attemting to create user info" + e.toString() );
			e.printStackTrace();
		}
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		ActivityService as = new ActivityService ( ctx );
		try {
			as.addUserActivity(user.getId(), "joined", 0);
		} catch (Exception e) {
			log.error("Adding a user activity failed" + e.toString() );
			e.printStackTrace();
		}

		return user;*/
		
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path ( "login")
	public Useraccount login ( @Context HttpServletRequest request,
			@FormParam("username") String username,
			@FormParam("password") String password ) {
		ServletContext ctx = request.getSession().getServletContext();
		UserAccountService service = new UserAccountService ( ctx );
		try {
			Useraccount account = service.authenticateUser(username, password);
			return account;
		}
		catch ( AuthenticationException e ) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("widgets")
	public List<Widgetprofile> getUsersWidgets (@Context HttpServletRequest request) {
		
		Useraccount signedInUser = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( signedInUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService(ctx);
		return wps.getWidgetsForUser(signedInUser);
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("widgets/{userid}")
	public List<Widgetprofile> getAUsersWidgets (@Context HttpServletRequest request,
											@PathParam("userid") String userId ) {
		
		Useraccount signedInUser = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( signedInUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		ServletContext ctx = request.getSession().getServletContext();
		UserAccountService uas = new UserAccountService( ctx );
		Useraccount user = uas.getUserAccount(userId);
		if ( user == null ) {
			String message = "User account: "+userId+" not found";
			log.error(message);
			throw new WebApplicationException (Response.Status.NOT_FOUND);
		}
		WidgetProfileService wps = new WidgetProfileService(ctx);
		return wps.getWidgetsForUser(user);
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("favourites/{userid}")
	public List<WidgetFavourite> getUsersFavourites( @Context HttpServletRequest request,
													@PathParam("userid") String userId ){
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount signedInUser = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( signedInUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		UserAccountService uas = new UserAccountService( ctx );
		Useraccount user = uas.getUserAccount(userId);
		if ( user == null ) {
			String message = "User account: "+userId+" not found";
			log.error(message);
			throw new WebApplicationException (Response.Status.NOT_FOUND);
		}
		List<WidgetFavourite> favourites = user.getFavourites();
		if ( favourites == null ) {
			favourites = new ArrayList<WidgetFavourite>();
		}
		return favourites;
		
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("favourites/{widgetId}/{level}")
	public WidgetFavourite addUserFavourite (@Context HttpServletRequest request,
												@PathParam("widgetId") String widgetId,
												@PathParam("level") int level)
	{
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount signedInUser = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( signedInUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		UserAccountService uas = new UserAccountService( ctx );
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		
		if ( wp == null ) {
			throw new WebApplicationException ( Response.Status.NOT_FOUND);
		}
		
		return uas.addFavourite(signedInUser, wp, level);
	}
	
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("favourites/{widgetId}")
	public Message removeUserFavourite ( @Context HttpServletRequest request,
											@PathParam("widgetId") String widgetId ) {
		
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount signedInUser = (Useraccount)SecurityUtils.getSubject().getPrincipal();
		if ( signedInUser == null ) {
			throw new WebApplicationException ( Response.Status.UNAUTHORIZED);
		}
		UserAccountService uas = new UserAccountService( ctx );
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile wp = wps.findWidgetProfileById(widgetId);
		
		if ( wp == null ) {
			throw new WebApplicationException ( Response.Status.NOT_FOUND);
		}
		
		return uas.removeFavourite(signedInUser, wp);
	}
												
	
	
}
