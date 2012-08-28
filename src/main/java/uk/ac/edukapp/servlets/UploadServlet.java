package uk.ac.edukapp.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import uk.ac.edukapp.model.Useraccount;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.server.configuration.WookieServerConfiguration;
import uk.ac.edukapp.service.ActivityService;
import uk.ac.edukapp.service.WidgetProfileService;

/**
 * Servlet implementation class RegisterServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Log log;
	private static final File FILE_UPLOAD_TEMP_DIRECTORY = new File(
			System.getProperty("java.io.tmpdir"));
	private int intMaxFileUploadSize = 25 * 1024 * 1024;// 25 mb is more than
														// enough

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
		log = LogFactory.getLog(UploadServlet.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (ServletFileUpload.isMultipartContent(request)) {// file attached
															// =>widget invoke
															// wookie/widgets
															// rest service

			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			// Set factory constraints
			diskFileItemFactory.setSizeThreshold(intMaxFileUploadSize);
			diskFileItemFactory.setRepository(FILE_UPLOAD_TEMP_DIRECTORY);
			// Create a new file upload handler
			ServletFileUpload servletFileUpload = new ServletFileUpload(
					diskFileItemFactory);
			try {
				// Get the multipart items as a list
				@SuppressWarnings("unchecked")
				List<FileItem> listFileItems = (List<FileItem>) servletFileUpload
						.parseRequest(request);
				// Iterate the multipart items list
				for (FileItem fileItemCurrent : listFileItems) {
					if (!fileItemCurrent.isFormField()) {
						// The item is a file upload, so we create a FilePart
						FilePart filePart = new FilePart(
								fileItemCurrent.getFieldName(),
								new ByteArrayPartSource(
										fileItemCurrent.getName(),
										fileItemCurrent.get()));
						int widget_id = uploadW3CWidget(filePart);

						if (widget_id == -1) {
							doForward(request, response, "/upload.jsp?error=3");
						} else {
							//
							// log the user upload to UserActivity table
							//
							addUserUploadActivity(widget_id);
							doForward(request, response, "/widget/" + widget_id);
						}
					}
				}

			} catch (FileUploadException fue) {
				fue.printStackTrace();
				doForward(request, response, "/upload.jsp?error=6");
			} catch (Exception e) {
				e.printStackTrace();
				doForward(request, response, "/upload.jsp?error=9");
			}

		} else {

			// get parameters
			String uploadurl = request.getParameter("uploadurl");
			String gadgetName = request.getParameter("gadget-name");
			
			try {
				
				//
				// create and return the widget profile
				// TODO use the shindig metadata service
				//
				WidgetProfileService widgetProfileService = new WidgetProfileService(
						getServletContext());
				Widgetprofile gadget = widgetProfileService.createWidgetProfile(uploadurl, gadgetName, "", "", Widgetprofile.OPENSOCIAL_GADGET);
				
				//
				// log the user upload to UserActivity table
				//
				addUserUploadActivity(gadget.getId());
				doForward(request, response, "/widget.jsp?id=" + gadget.getId());

			} catch (Exception e) {
				e.printStackTrace();
				doForward(request, response, "/upload.jsp?error=1");
			}
		}

	}

	private void doForward(HttpServletRequest request,
			HttpServletResponse response, String jsp) throws ServletException,
			IOException {
		/*
		 * if we redirect using the dispatcher then the url in address bar does
		 * not update
		 */
		// RequestDispatcher dispatcher = getServletContext()
		// .getRequestDispatcher(jsp);
		// dispatcher.forward(request, response);
		response.sendRedirect(jsp);
	}

	/**
	 * Upload a Widget to Wookie and return the id of the edukapp widgetprofile
	 * 
	 * @param filePart
	 * @return the id of the uploaded widgetprofile
	 * @throws Exception
	 */
	private int uploadW3CWidget(FilePart filePart) throws Exception {

		HttpClient client = new HttpClient();
		WookieServerConfiguration wookie = WookieServerConfiguration
				.getInstance();
		client.getState().setCredentials(wookie.getAuthScope(),
				wookie.getCredentials());

		PostMethod postMethod = new PostMethod(wookie.getWookieServerLocation()
				+ "/widgets");

		Part[] parts = { filePart };
		postMethod.setRequestEntity(new MultipartRequestEntity(parts,
				postMethod.getParams()));

		int status = client.executeMethod(postMethod);

		if (status == 200 || status == 201) {

			//
			// create a profile from the returned metadata
			//
			Widgetprofile widgetprofile = this
					.createWidgetProfileFromResponse(postMethod
							.getResponseBodyAsStream());
			//
			// Return the id
			//
			return widgetprofile.getId();
		}
		throw new Exception("Upload failed");
	}

	/**
	 * Create a widget profile object from the metadata in the Wookie POST
	 * response
	 * 
	 * @param body
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	private Widgetprofile createWidgetProfileFromResponse(InputStream body)
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
		String icon = rootNode.getChild("icon", XML_NS).getAttributeValue("src");
		//
		// create and return the widget profile
		//
		WidgetProfileService widgetProfileService = new WidgetProfileService(
				getServletContext());
		return widgetProfileService.createWidgetProfile(uri, name, description, icon, Widgetprofile.W3C_WIDGET);
	}

	private void addUserUploadActivity(int widgetprofileId) {
		// get user id from SecurityRealm
		Useraccount userAccount = (Useraccount) SecurityUtils.getSubject()
				.getPrincipal();
		log.info("about to insert to user activity, username="
				+ userAccount.getUsername());
		int userId = userAccount.getId();
		try {
			ActivityService avtivityServie = new ActivityService(
					getServletContext());
			avtivityServie.addUserActivity(userId, "uploaded", widgetprofileId);
		} catch (Exception e) {
			log.error("adding to user activity table failed");
			e.printStackTrace();
		}
	}
}