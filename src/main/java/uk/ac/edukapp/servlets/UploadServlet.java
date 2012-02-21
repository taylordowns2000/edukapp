package uk.ac.edukapp.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.repository.Widget;

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

		log.info("upload servlet");

		if (ServletFileUpload.isMultipartContent(request)) {// file attached
															// =>widget invoke
															// wookie/widgets
															// rest service

			log.info("request having a file attached - post it to /wookie/widgets");

			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			// Set factory constraints
			diskFileItemFactory.setSizeThreshold(intMaxFileUploadSize);
			diskFileItemFactory.setRepository(FILE_UPLOAD_TEMP_DIRECTORY);
			// Create a new file upload handler
			ServletFileUpload servletFileUpload = new ServletFileUpload(
					diskFileItemFactory);
			try {
				// Get the multipart items as a list
				List<FileItem> listFileItems = (List<FileItem>) servletFileUpload
						.parseRequest(request);
				// Create a list to hold all of the parts
				List<Part> listParts = new ArrayList<Part>();
				// Iterate the multipart items list
				for (FileItem fileItemCurrent : listFileItems) {
					// If the current item is a form field, then create a string
					// part
					if (fileItemCurrent.isFormField()) {
						StringPart stringPart = new StringPart(
								fileItemCurrent.getFieldName(), // The field
																// name
								fileItemCurrent.getString() // The field value
						);
						log.info("request string part:"
								+ fileItemCurrent.getFieldName() + ":"
								+ fileItemCurrent.getString());
						// Add the part to the list
						listParts.add(stringPart);
					} else {
						// The item is a file upload, so we create a FilePart
						FilePart filePart = new FilePart(
								fileItemCurrent.getFieldName(), // The field
																// name
								new ByteArrayPartSource(
										fileItemCurrent.getName(), // The
																	// uploaded
																	// file name
										fileItemCurrent.get() // The uploaded
																// file contents
								));
						// Add the part to the list
						listParts.add(filePart);
					}
				}
				PostMethod postMethod = new PostMethod(
						"http://localhost:8080/wookie/widgets");
				MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(
						listParts.toArray(new Part[] {}),
						postMethod.getParams());

				postMethod.setRequestEntity(multipartRequestEntity);

				HttpClient client = new HttpClient();

				// add the basic http authentication credentials
				Credentials defaultcreds = new UsernamePasswordCredentials(
						"java", "java");
				client.getState().setCredentials(
						new AuthScope("localhost", 8080, AuthScope.ANY_REALM),
						defaultcreds);

				int status = client.executeMethod(postMethod);

				log.info("post execution return status:" + status);

				if (status = HttpStatus.SC_OK) {// if succesful upload
					WookieConnectorService conn = null;
					try {
						conn = new WookieConnectorService(
								"http://localhost:8080/wookie/", "TEST",
								"myshareddata");
					} catch (WookieConnectorException wce) {
						wce.printStackTrace();
					}

					// get the latest entry - stupid way i know but i am in a
					// hurry
					HashMap<String, Widget> availableWookieWidgets = conn
							.getAvailableWidgets();
					Iterator it = availableWookieWidgets.keySet().iterator();
					org.apache.wookie.connector.Widget widget = null;
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						widget = (org.apache.wookie.connector.Widget) pairs
								.getValue();
					}

					EntityManagerFactory factory = Persistence
							.createEntityManagerFactory("edukapp");
					EntityManager em = factory.createEntityManager();

					/*-----------*/
					Widgetprofile widgetprofile = null;
					try {
						em.getTransaction().begin();

						widgetprofile = new Widgetprofile();
						widgetprofile.setName(widget.getTitle());
						byte zero = 0;
						widgetprofile.setW3cOrOs(zero);
						widgetprofile.setWidId(widget.getIdentifier());
						em.persist(widgetprofile);

						log.info("Widget created with id:"
								+ widgetprofile.getId());

					} catch (Exception e) {
						e.printStackTrace();
						// em.getTransaction().rollback();
						doForward(request, response, "/upload.jsp?error=1");
					}
					/*----------*/

					em.getTransaction().commit();
					em.close();
					factory.close();

					if (widgetprofile != null) {
						doForward(request, response, "/widget.jsp?id="
								+ widgetprofile.getId());
					} else {
						doForward(request, response, "/upload.jsp?error=1");
					}

				}

			} catch (FileUploadException fue) {
				fue.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			// get parameters
			String uploadurl = null;
			String gadgetName = null;

			uploadurl = request.getParameter("uploadurl");
			gadgetName = request.getParameter("gadget-name");

			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("edukapp");
			EntityManager em = factory.createEntityManager();

			/*-----------*/
			Widgetprofile gadget = null;
			try {
				em.getTransaction().begin();

				gadget = new Widgetprofile();
				gadget.setName(gadgetName);
				byte one = 1;
				gadget.setW3cOrOs(one);
				gadget.setWidId(uploadurl);
				em.persist(gadget);

				log.info("Gadget created with id:" + gadget.getId());

			} catch (Exception e) {
				e.printStackTrace();
				// em.getTransaction().rollback();
				doForward(request, response, "/upload.jsp?error=1");
			}
			/*----------*/

			em.getTransaction().commit();
			em.close();
			factory.close();

			if (gadget != null) {
				doForward(request, response, "/widget.jsp?id=" + gadget.getId());
			} else {
				doForward(request, response, "/upload.jsp?error=1");
			}
		}

	}

	private void doForward(HttpServletRequest request,
			HttpServletResponse response, String jsp) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}

}