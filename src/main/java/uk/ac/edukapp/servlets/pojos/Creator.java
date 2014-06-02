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
package uk.ac.edukapp.servlets.pojos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import uk.ac.edukapp.builder.WidgetBuilder;
import uk.ac.edukapp.exceptions.RESTException;
import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.UploadResults;
import uk.ac.edukapp.repository.SolrConnector;
import uk.ac.edukapp.server.configuration.WookieServerConfiguration;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.WidgetProfileService;
import uk.ac.edukapp.util.FileFolderUtils;
import uk.ac.edukapp.util.ServletUtils;

@Path("creator")
public class Creator {
	private static final Log log = LogFactory.getLog(Creator.class);
	
	
	@GET
	@Path("editable/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean canEditWidget ( @Context HttpServletRequest request,
									@PathParam("widgetId") String widgetId )
	{

		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.UNAUTHORIZED);
		}
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			log.debug("Could not file widget id: "+widgetId);
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		// if the widget has no widget owner
		Useraccount widgetOwner = widgetProfile.getOwner();
		if ( widgetOwner == null ) {
			return false;
		}
		
		// if the current user is not the owner
		if (widgetOwner.getId() != userAccount.getId()) {
			return false;
		}
		
		WidgetBuilder widgetBuilder = new WidgetBuilder();
		return widgetBuilder.checkWidgetCanBeEdited(widgetProfile);	
	}
	
	@PUT
	@Path("convert/{widgetId}/{toType}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean convertWidget (@Context HttpServletRequest request,
									@PathParam("widgetId") String widgetId,
									@PathParam("toType") String newType ) {
		
		ServletContext ctx = request.getSession().getServletContext();
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.UNAUTHORIZED);
		}
		
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
		
		Widgetprofile widgetProfile = widgetProfileService.findWidgetProfileById(widgetId);
		if ( widgetProfile == null ) {
			log.debug("Could not file widget id: "+widgetId);
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		
		return true;
	}
	
	
	
	
	
	@GET
	@Path("icons/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getWidgetIcons ( @Context HttpServletRequest request,
											@PathParam("type") String iconType ){
		ArrayList<String> icons = new ArrayList<String>();
		String pattern;
		if ( iconType.equalsIgnoreCase("flash") ) {
			pattern = "_f\\.+";
		}
		else if ( iconType.equalsIgnoreCase("web")){
			pattern = "_w\\.+";
		}
		else if ( iconType.equalsIgnoreCase("url")){
			pattern = "_u\\.+";
		}
		else if ( iconType.equalsIgnoreCase("general")){
			pattern = "widget\\.png+";
		}
		else { // assume "all"
			pattern = ".png+";
		}
		ServletContext context = request.getSession().getServletContext();
		String path = context.getRealPath("/");
		path = path + "/icons";
		File folder = new File(path);
		File[] files = FileFolderUtils.listFilesInFolderWithPattern(folder, pattern);
		
		String rootURL = ServletUtils.getServletRootURL(request);
		
		for ( int i = 0; i < files.length; i++ ) {
			File iconFile = files[i];
			String iconURL = rootURL + "/icons/" + iconFile.getName();
			icons.add(iconURL);
		}
		return icons;
	}
	
	
	
	
	@Path("widget")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Widgetprofile> uploadWidget ( @Context HttpServletRequest request,
			InMultiPart multipart ) throws IOException{
		ServletContext ctx = request.getSession().getServletContext();

		ArrayList<File> fileList = new ArrayList<File>();
		while ( multipart.hasNext()) {
			InPart part = multipart.next();
			MultivaluedMap<String, String> headers = part.getHeaders();
			String contentDisposition = headers.getFirst("Content-Disposition");
			String parameterName = getHeaderParameter ( "name", contentDisposition );
			InputStream is = part.getInputStream();
			if ( parameterName.equals("widgetFile")) {
				String fileName = getHeaderParameter ( "filename", contentDisposition);
				File f = FileFolderUtils.tempFileFromStream(fileName, is);
				fileList.add(f);
			}
			
		}
		ArrayList<Widgetprofile> widgets = new ArrayList<Widgetprofile>();
		//if ( username == null ) {
		//	String errorString = "Username not found in parameters";
		//	log.debug(errorString);
		//	throw new WebApplicationException ( new RESTException(errorString), Response.Status.NOT_FOUND);
		//}
		if ( fileList.isEmpty()) {
			String errorString = "No file found in parameters";
			log.debug(errorString);
			throw new WebApplicationException ( new RESTException(errorString), Response.Status.NOT_FOUND);
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.NOT_FOUND);
		}
		try {
			for ( File widgetFile: fileList ) {
				Widgetprofile widgetProfile = uploadWidgetToWookie(widgetFile, ctx, userAccount );
				if ( widgetProfile != null ) {
					widgets.add(widgetProfile);
					widgetFile.delete();
				}
				else {
					log.debug("Failed to upload "+widgetFile.getName()+" to wookie!");
				}
			}
		}
		catch ( Exception e ) {
			throw new WebApplicationException ( e, Response.Status.BAD_REQUEST);
		}

		return widgets;
	}
	
	@Path("gadget")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Widgetprofile uploadGadget(@Context HttpServletRequest request,
											@FormParam("uploadrul") String uploadurl,
											@FormParam("gadgetname") String gadgetName){
		ServletContext ctx = request.getSession().getServletContext();
		try {
			
			//
			// create and return the widget profile
			// TODO use the shindig metadata service
			//
			
			Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
			if ( userAccount == null ) {
				String ErrorString = "Not logged in!";
				log.debug(ErrorString);
				throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.NOT_FOUND);
			}
			
			WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);
			Widgetprofile gadget = widgetProfileService.createWidgetProfile(uploadurl, gadgetName, "", "", Widgetprofile.OPENSOCIAL_GADGET, userAccount);
			
			addUserUploadActivity(gadget.getId(), userAccount, ctx, true);
			
			SolrConnector.getInstance().index(gadget, "en");
			return gadget;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException (e, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@Path("file")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public UploadResults uploadFile ( @Context HttpServletRequest request,
			InMultiPart multipart ) throws IOException, JDOMException, RESTException {
		ServletContext ctx = request.getSession().getServletContext();
		WidgetProfileService wps = new WidgetProfileService(ctx);
		Widgetprofile wp = null;
		//ArrayList<File> fileList = new ArrayList<File>();
		File mediaFile = null;
		File siteFile = null;
		String widgetName = null;
		String widgetDescription = null;
		String widgetWidth = null;
		String widgetHeight = null;
		String icon = null;
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.UNAUTHORIZED);
		}		
		while ( multipart.hasNext()) {
			InPart part = multipart.next();
			MultivaluedMap<String, String> headers = part.getHeaders();
			String contentDisposition = headers.getFirst("Content-Disposition");
			String contentType = headers.getFirst("Content-Type");
			String parameterName = getHeaderParameter ( "name", contentDisposition );
			InputStream is = part.getInputStream();
			if ( parameterName.equals("widgetname")) {
				widgetName = IOUtils.toString(is);
				checkParam( widgetName, "title");
				if ( !this.userOwnsWidgetName(widgetName, userAccount, wps)) {
					throw new WebApplicationException ( Response.Status.CONFLICT);
				}
				//if ( titleAlreadyExists ( widgetName, new WidgetProfileService(ctx))) {
				//	throw new WebApplicationException ( Response.Status.CONFLICT);
				//}
			}
			else if ( parameterName.equals("widgetdescription")) {
				widgetDescription = IOUtils.toString(is);
			}
			else if ( parameterName.equals("widgetwidth")) {
				widgetWidth = IOUtils.toString(is);
			}
			else if ( parameterName.endsWith("widgetheight")) {
				widgetHeight = IOUtils.toString(is);
			}
			else if ( parameterName.endsWith("icon")){
				icon = IOUtils.toString(is);
			}
			else if (parameterName.equals("sitefile")) {
				String fileName = getHeaderParameter("filename", contentDisposition);
				if ( contentType == null || 
						!this.checkZipMimeType(contentType) || 
						fileName == null || 
						!fileName.endsWith(".zip")) {
					String es = "Import not supported for type: "+contentType;
					log.debug(es);
					throw new WebApplicationException ( new RESTException(es), Response.Status.UNSUPPORTED_MEDIA_TYPE);
				}
				siteFile = FileFolderUtils.tempFileFromStream(fileName, is);
			}
			else if ( parameterName.equals("mediafile")) {
				String fileName = getHeaderParameter ( "filename", contentDisposition);
				if (contentType == null || !contentType.equals("application/x-shockwave-flash") || contentType.contains("application/x-java-applet") ) {
					String es = "Import not supported for type: "+contentType;
					log.debug(es);
					throw new WebApplicationException ( new RESTException(es), Response.Status.UNSUPPORTED_MEDIA_TYPE);
				}
				mediaFile = FileFolderUtils.tempFileFromStream(fileName, is);
			}
			
		}
		
		UploadResults uploadResults = new UploadResults();

		// now we have our stuff, better do something with it.
		File widgetFile = null;
		WidgetBuilder wb = new WidgetBuilder();
		if ( mediaFile != null ) {
			widgetFile = wb.buildWidgetFromFile(mediaFile, widgetName, userAccount.getUsername(), widgetDescription, widgetWidth, widgetHeight, icon, ctx);
		}
		else if ( siteFile != null ) {
			try {
				File tempFolder = wb.createFolderFromZipFile(siteFile, widgetName);
				List<File> htmlFiles = wb.findIndexFilesInWidgetFolder(tempFolder);
				if ( htmlFiles.size() == 0 ) {
					uploadResults.setUploadMessage("NONE");
					return uploadResults;
				}
				else if ( htmlFiles.size() > 1 ) {
					uploadResults.setUploadMessage("CHOOSE");
					uploadResults.setHTMLFileList(htmlFiles);
					uploadResults.setWidgetFolderPath(tempFolder.getName());
					return uploadResults;
				}
				else {
					String fileName = htmlFiles.get(0).getName();
					widgetFile = wb.buildWidgetFromTempFolder(tempFolder.getName(), widgetName, userAccount.getUsername(), widgetDescription, widgetWidth, widgetHeight, icon, fileName, ctx);
				}
			}
			catch ( Exception e ) {
				log.error("An Exception Occured:", e);
				throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
			}
			//widgetFile = wb.buildWidgetFromZip(siteFile, widgetName, userAccount.getUsername(), widgetDescription, widgetWidth, widgetHeight, icon, chosenIndexFile, ctx);
		}
		wp = uploadWidgetToWookie(widgetFile, ctx, userAccount );
		uploadResults.setUploadMessage("OK");
		uploadResults.setWidgetProfile(wp);
		widgetFile.delete();		
		return uploadResults;
	}
	
	@POST
	@Path("build")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Widgetprofile buildWidgetWithChosenIndexFile ( @Context HttpServletRequest request,
			@FormParam("widgetname") String title,
			@FormParam("widgetdescription") String description,
			@FormParam("widgetwidth") String width,
			@FormParam("widgetheight") String height,
			@FormParam("icon") String icon,
			@FormParam("chosenfilename") String chosenIndexFile,
			@FormParam("tempfoldername") String tempFolderName) throws IOException, JDOMException, RESTException {
		ServletContext ctx = request.getSession().getServletContext();
		
		WidgetBuilder widgetBuilder = new WidgetBuilder();
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.UNAUTHORIZED);
		}		
		
		
		File widgetFile = widgetBuilder.buildWidgetFromTempFolder(tempFolderName, title, userAccount.getUsername(), description, width, height, icon, chosenIndexFile, ctx);
		Widgetprofile wp = uploadWidgetToWookie(widgetFile, ctx, userAccount );

		return wp;
	}
	
	@PUT
	@Path("embed")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Widgetprofile updateEmbedORAddressWidget (@Context HttpServletRequest request,
			@FormParam("widgetname") String title,
			@FormParam("widgetdescription") String description,
			@FormParam("embed") String embed,
			@FormParam("widgetwidth") String width,
			@FormParam("widgetheight") String height,
			@FormParam("icon") String icon,
			@FormParam("builder") String builder) throws JDOMException, IOException, RESTException
{
		ServletContext ctx = request.getSession().getServletContext();
		Widgetprofile wp = null;
		checkParam(title, "title");
		WidgetProfileService wps = new WidgetProfileService(ctx);
		if (width == null || width.equals("")) {
			width = "400";
		}
		if (height == null || height.equals("")) {
			height = "400";
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		if (userAccount == null) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException(new RESTException(ErrorString),
					Response.Status.UNAUTHORIZED);
		}
		if ( !this.userOwnsWidgetName(title, userAccount, wps)) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		File widgetFile;
		WidgetBuilder wb = new WidgetBuilder();
		if ( builder == null || builder.equals("embed") ) {
		 widgetFile = wb.buildWidgetFromEmbed(embed, title,
				userAccount.getUsername(), description, width, height, icon,
				ctx);
		}
		else {
			widgetFile = wb.buildWidgetFromUrl(embed, title, 
					userAccount.getUsername(), description, width, height, icon,
					ctx);
		}
		wp = this.uploadWidgetToWookie(widgetFile, ctx, userAccount);

		// add edit information to widget profile
		wp = wps.addEditInformation(wp, builder, embed, width, height);

		return wp;
	}
	
	/**
	 * new URL Widget
	 * @param request
	 * @param title
	 * @param description
	 * @param url
	 * @param width
	 * @param height
	 * @param icon
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws RESTException
	 */
	@POST
	@Path("webaddress")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Widgetprofile newWebAddressWidget(@Context HttpServletRequest request,
			@FormParam("widgetname") String title,
			@FormParam("widgetdescription") String description,
			@FormParam("webaddress") String webaddress,
			@FormParam("widgetwidth") String width,
			@FormParam("widgetheight") String height,
			@FormParam("icon") String icon) throws JDOMException, IOException,
			RESTException {
		ServletContext ctx = request.getSession().getServletContext();
		Widgetprofile wp = null;
		checkParam(title, "title");
		WidgetProfileService wps = new WidgetProfileService(ctx);
		if (width == null || width.equals("")) {
			width = "400";
		}
		if (height == null || height.equals("")) {
			height = "400";
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		if (userAccount == null) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException(new RESTException(ErrorString),
					Response.Status.UNAUTHORIZED);
		}
		
		// check web address
		
		if (!ServletUtils.checkURL(webaddress)) {
			throw new WebApplicationException(new RESTException("Web Address Not Valid"), Response.Status.NOT_ACCEPTABLE);
		}
		WidgetBuilder wb = new WidgetBuilder();
		File widgetFile = wb.buildWidgetFromUrl(webaddress, title,
				userAccount.getUsername(), description, width, height, icon,
				ctx);
		wp = this.uploadWidgetToWookie(widgetFile, ctx, userAccount);

		// add edit information to widget profile
		wp = wps.addEditInformation(wp, "webaddress", webaddress, width, height);

		return wp;
	}
											
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param title
	 * @param description
	 * @param embed
	 * @param width
	 * @param height
	 * @param icon
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws RESTException
	 */
	@POST
	@Path("embed")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Widgetprofile newEmbedWidget( @Context HttpServletRequest request,
											@FormParam("widgetname") String title,
											@FormParam("widgetdescription") String description,
											@FormParam("embed") String embed,
											@FormParam("widgetwidth") String width,
											@FormParam("widgetheight") String height,
											@FormParam("icon") String icon) throws JDOMException, IOException, RESTException
	{
		ServletContext ctx = request.getSession().getServletContext();
		Widgetprofile wp = null;
		checkParam( title, "title");
		WidgetProfileService wps = new WidgetProfileService(ctx);
		if ( width == null || width.equals("")) {
			width = "400";
		}
		if ( height == null || height.equals("")){
			height = "400";
		}
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject().getPrincipal();
		if ( userAccount == null ) {
			String ErrorString = "Not logged in!";
			log.debug(ErrorString);
			throw new WebApplicationException ( new RESTException(ErrorString), Response.Status.UNAUTHORIZED);
		}
		WidgetBuilder wb = new WidgetBuilder();
		File widgetFile = wb.buildWidgetFromEmbed(embed, title, userAccount.getUsername(), description, width, height, icon, ctx);
		wp = this.uploadWidgetToWookie(widgetFile, ctx, userAccount);
		
		// add edit information to widget profile
		wp = wps.addEditInformation(wp, "embed",embed, width, height);
		
		return wp;
	}
	

	
	
	
	private String getHeaderParameter (String param, String contentDisposition ) {
		String result = "";
		Pattern p = Pattern.compile("(?<="+param+"=\").*?(?=\")");
		Matcher m = p.matcher(contentDisposition);
		if ( m.find() ){
			result = m.group();
		}
		return result;
	}
	
	

	private Widgetprofile uploadWidgetToWookie(File widgetFile, ServletContext ctx, Useraccount userAccount ) throws JDOMException, IOException, RESTException{

		HttpClient client = new HttpClient();
		WookieServerConfiguration wookie = WookieServerConfiguration
				.getInstance();
			client.getState().setCredentials(wookie.getAuthScope(),
					wookie.getCredentials());

			PostMethod postMethod = new PostMethod(
					wookie.getWookieServerLocation() + "/widgets");
			FilePart filePart = new FilePart("widgetFile", widgetFile);
			Part[] parts = { filePart };
			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));
			
			int status = client.executeMethod(postMethod);

			if (status == 200 || status == 201) {
				boolean createOrUpdate = (status==201)?true:false;

				Widgetprofile widgetprofile = this
						.createWidgetProfileFromResponse(
								postMethod.getResponseBodyAsStream(), ctx, createOrUpdate, userAccount);
				SolrConnector.getInstance().index();
				addUserUploadActivity(widgetprofile.getId(), userAccount, ctx, createOrUpdate);

				return widgetprofile;
			}
			else {
				throw new RESTException ("Recived status code: "+status+" from wookie!");
			}

	}
	
	
	private Widgetprofile createWidgetProfileFromResponse(InputStream body, ServletContext ctx, boolean createProfile, Useraccount owner)
			throws JDOMException, IOException {
		//
		// parse the response and extract the widget metadata
		//
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(body);
		Namespace XML_NS = Namespace.getNamespace("",
				"http://www.w3.org/ns/widgets");
		Element rootNode = document.getRootElement();
		String uri = document.getRootElement().getAttributeValue("id");
		String name = rootNode.getChildText("name", XML_NS);
		String description = rootNode.getChildText("description", XML_NS);
		Element iconElement = rootNode.getChild("icon", XML_NS);
		String icon = "";
		if ( iconElement != null ) {
			icon = iconElement.getAttributeValue("src");
		}
		WidgetProfileService widgetProfileService = new WidgetProfileService(ctx);

		if ( createProfile ) {
			//
			// create and return the widget profile
			//
			return widgetProfileService.createWidgetProfile(uri, name, description, icon, Widgetprofile.W3C_WIDGET, owner);
		}
		else {
			return widgetProfileService.updateWidgetProfile(uri, name, description, icon);
		}
	}
	
	
	private void addUserUploadActivity(int widgetprofileId, Useraccount userAccount, ServletContext ctx, boolean created) {

		int userId = userAccount.getId();
		try {
			ActivityService activityService = new ActivityService(ctx);
			if ( created ) {
				activityService.addUserActivity(userId, "uploaded", widgetprofileId);
			}
			else {
				activityService.addUserActivity(userId, "updated", widgetprofileId);
			}
			
		} catch (Exception e) {
			log.error("adding to user activity table failed");
			e.printStackTrace();
		}
	}
	
	
	private void checkParam ( String param, String paramname ) {
		if ( param == null || param.equals("")) {
			throw new WebApplicationException ( 
					new RESTException ( "The parameter "+paramname+" should not be null"),
					Response.Status.BAD_REQUEST);
		}
	}
	
	
	private boolean userOwnsWidgetName ( String widgetName, Useraccount user, WidgetProfileService service  ) {
		Widgetprofile wp = service.findWidgetProfileByName(widgetName);
		if ( wp == null || wp.getOwner() == null || user.equals(wp.getOwner()) ) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * application/zip, 
	 * application/x-zip, 
	 * application/x-zip-compressed, 
	 * application/octet-stream, 
	 * application/x-compress, 
	 * application/x-compressed, 
	 * multipart/x-zip
	 * 
	 */
	
	private boolean checkZipMimeType ( String zipMime ) {
		if ( zipMime == null ) {
			return false;
		}
		else if ( zipMime.equals("application/zip") ||
				zipMime.equals("application/x-zip") ||
				zipMime.equals("application/x-zip-compressed") ||
				zipMime.equals("application/octet-stream") ||
				zipMime.equals("application/x-compress") ||
				zipMime.equals("application/x-compressed") ||
				zipMime.endsWith("multipart/x-zip")
				) {
			return true;
		}
		return false;
	}

}
