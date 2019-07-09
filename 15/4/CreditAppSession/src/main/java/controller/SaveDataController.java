package controller;

import model.FormDataStorage;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/saveData")
@MultipartConfig
public class SaveDataController extends HttpServlet {

  private static final Logger log = Logger.getLogger(SaveDataController.class);

  @Inject
  FormDataStorage formDataStorage;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    String sessionId = req.getSession().getId();

    try {
      formDataStorage.put(sessionId, req.getReader().readLine());
    } catch (IOException e) {
      log.error(e);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    String sessionId = req.getSession().getId();

    String formData = formDataStorage.get(sessionId);
    if (formData != null) {
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      try {
        resp.getWriter().write(formData);
      } catch (IOException e) {
        log.error(e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }

    } else {
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
  }
}
