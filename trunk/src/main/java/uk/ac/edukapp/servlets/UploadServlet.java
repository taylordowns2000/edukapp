package uk.ac.edukapp.servlets;

import uk.ac.edukapp.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

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
				List<FileItem> listFileItems = (List<FileItem>) servletFileUpload
						.parseRequest(request);
				// Create a list to hold all of the parts
				List<org.apache.commons.httpclient.methods.multipart.Part> listParts = new ArrayList<org.apache.commons.httpclient.methods.multipart.Part>();
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
						"http://widgets.open.ac.uk:8080/wookie/widgets");
				MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(
						listParts
								.toArray(new org.apache.commons.httpclient.methods.multipart.Part[] {}),
						postMethod.getParams());

				postMethod.setRequestEntity(multipartRequestEntity);

				HttpClient client = new HttpClient();

				// add the basic http authentication credentials
				Credentials defaultcreds = new UsernamePasswordCredentials(
						"java", "java");
				client.getState().setCredentials(
						new AuthScope("widgets.open.ac.uk", 8080,
								AuthScope.ANY_REALM), defaultcreds);

				int status = client.executeMethod(postMethod);

				System.out.println("returns status:" + status);
				log.info("post execution return status:" + status);

				byte[] responseBody = postMethod.getResponseBody();
				System.out.println("RESPONSE follows");
				System.out.println(new String(responseBody));
				log.info("RESPONSE follows");
				log.info(new String(responseBody));

				InputStream istream = postMethod.getResponseBodyAsStream();

				// if (status == HttpStatus.SC_OK) {
				// // HTTP 200 succesful upload
				// // widget updated
				//
				// } else if (status == HttpStatus.SC_CREATED) {
				// // HTTP 201 succesful upload
				// // - new widget created
				if (status == HttpStatus.SC_OK
						|| status == HttpStatus.SC_CREATED) {

					SAXBuilder builder = new SAXBuilder();
					Document document = (Document) builder.build(istream);
					Element rootNode = document.getRootElement();
					Namespace XML_NS = Namespace.getNamespace("",
							"http://www.w3.org/ns/widgets");

					String widget_id = rootNode.getAttributeValue("id");
					String version = rootNode.getAttributeValue("version");
					String recomended_height = rootNode
							.getAttributeValue("height");
					String recomended_width = rootNode
							.getAttributeValue("width");
					String name = rootNode.getChildText("name", XML_NS);
					String icon = rootNode.getChildText("icon", XML_NS);
					String description = rootNode.getChildText("description",
							XML_NS);
					String author = rootNode.getChildText("author", XML_NS);

					System.out.println("\n\nname:" + name + "\n\n");
					log.info("\n\nname:" + name + "\n\n");

					EntityManagerFactory factory = Persistence
							.createEntityManagerFactory("edukapp");
					EntityManager em = factory.createEntityManager();

					/*-----------*/
					Widgetprofile widgetprofile = null;
					try {
						em.getTransaction().begin();
						widgetprofile = new Widgetprofile();
						widgetprofile.setName(name);
						byte zero = 0;
						widgetprofile.setW3cOrOs(zero);
						widgetprofile.setWidId(widget_id);
						em.persist(widgetprofile);

						WidgetDescription wd = new WidgetDescription();
						wd.setDescription(description);
						wd.setWid_id(widgetprofile.getId());
						em.persist(wd);

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
						doForward(request, response, "/upload.jsp?error=2");
					}

				} else {
					// any other HTTP status than 200/201
					doForward(request, response, "/upload.jsp?error=3");
				}

			} catch (HttpException hte) {
				log.error("Fatal protocol violation: " + hte.getMessage());
				hte.printStackTrace();
				doForward(request, response, "/upload.jsp?error=4");
			} catch (IOException ioe) {
				log.error("Fatal transport error: " + ioe.getMessage());
				ioe.printStackTrace();
				doForward(request, response, "/upload.jsp?error=5");
			} catch (FileUploadException fue) {
				fue.printStackTrace();
				doForward(request, response, "/upload.jsp?error=6");
			} catch (JDOMException jdomex) {
				jdomex.printStackTrace();
				doForward(request, response, "/upload.jsp?error=7");
			} catch (Exception e) {
				e.printStackTrace();
				doForward(request, response, "/upload.jsp?error=9");
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
		/*
		 * if we redirect using the dispatcher then the url in address bar does
		 * not update
		 */
		// RequestDispatcher dispatcher = getServletContext()
		// .getRequestDispatcher(jsp);
		// dispatcher.forward(request, response);
		response.sendRedirect(jsp);
	}

}