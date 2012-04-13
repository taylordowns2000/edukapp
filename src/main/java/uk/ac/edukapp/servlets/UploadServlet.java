package uk.ac.edukapp.servlets;

import uk.ac.edukapp.model.*;
import uk.ac.edukapp.renderer.WookieServerConfiguration;
import uk.ac.edukapp.repository.SolrConnector;

import java.io.File;
import java.io.IOException;

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
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
				List<FileItem> listFileItems = (List<FileItem>) servletFileUpload.parseRequest(request);
				// Iterate the multipart items list
				for (FileItem fileItemCurrent : listFileItems) {
					// If the current item is a form field, then create a string
					// part
					if (!fileItemCurrent.isFormField()) {
						// The item is a file upload, so we create a FilePart
						FilePart filePart = new FilePart(
								fileItemCurrent.getFieldName(), 
								new ByteArrayPartSource( fileItemCurrent.getName(),fileItemCurrent.get())
						);
						if (!uploadW3CWidget(filePart)){
							doForward(request, response, "/upload.jsp?error=3");							
						}
					}
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
	
	/**
	 * Upload a Widget to Wookie
	 * @param filePart
	 * @return true if the widget was successfully uploaded
	 * @throws HttpException
	 * @throws IOException
	 */
	private boolean uploadW3CWidget(FilePart filePart) throws HttpException, IOException{
		
		HttpClient client = new HttpClient();
		WookieServerConfiguration wookie = WookieServerConfiguration.getInstance();
		client.getState().setCredentials(wookie.getAuthScope(), wookie.getCredentials());
		
		PostMethod postMethod = new PostMethod(wookie.getWookieServerLocation()+"/widgets");

		Part[] parts = {filePart};
		postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));

		int status = client.executeMethod(postMethod);
		
		if (status == 200 || status == 201){
			
			//
			// update the index
			//
			SolrConnector.getInstance().index();
			
			return true;
		} 
		return false;

	}

}