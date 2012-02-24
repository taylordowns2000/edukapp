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
package uk.ac.edukapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.edukapp.model.Tag;
import uk.ac.edukapp.model.Widgetprofile;
import uk.ac.edukapp.renderer.MetadataRenderer;
import uk.ac.edukapp.service.WidgetProfileService;

/**
 * Tags api endpoint
 * 
 * @author anastluc
 * 
 */
public class TagServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String tagid = req.getParameter("id");
    String operation = req.getParameter("operation");

    if (tagid == null && tagid.trim().length() == 0) {
      resp.getWriter().append("id is empty").close();
      return;
    }
    if (operation == null && operation.trim().length() == 0) {
      resp.getWriter().append("operation is empty").close();
      return;
    }

    EntityManagerFactory factory = (EntityManagerFactory) getServletContext()
        .getAttribute("emf");
    EntityManager em = factory.createEntityManager();
    WidgetProfileService widgetProfileService = new WidgetProfileService(
        getServletContext());

    Tag tag = null;
    tag = em.find(Tag.class, Integer.parseInt(tagid));

    if (tag != null) {
      OutputStream out = resp.getOutputStream();
      if (operation.equals("getName")) {
        MetadataRenderer.render(out, tag);
      } else if (operation.equals("getWidgets")) {
        List<Widgetprofile> widgetsTaggedWith = widgetProfileService
            .findWidgetProfilesForTag(tag);
        MetadataRenderer.render(out, widgetsTaggedWith);
      }
      out.flush();
      out.close();
    }

  }

}
