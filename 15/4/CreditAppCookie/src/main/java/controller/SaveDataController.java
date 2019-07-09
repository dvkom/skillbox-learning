package controller;

import model.FormDataStorage;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/saveData")
@MultipartConfig
public class SaveDataController extends HttpServlet {
  private static final String USER_ID = "userID";
  private static final int COOKIE_MAX_AGE = 30 * 30 * 24 * 30;


  private static final Logger log = Logger.getLogger(SaveDataController.class);

  @Inject
  FormDataStorage formDataStorage;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    String userId = getUserIdFromCookie(req);
    if (userId == null) {
      userId = createUniqueUserId();
      createUserIdCookie(resp, userId);
    }

    try {
      formDataStorage.put(userId, req.getReader().readLine());
    } catch (IOException e) {
      log.error(e);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    String userId = getUserIdFromCookie(req);
    if (userId != null) {

      String formData = formDataStorage.get(userId);
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

    } else {
      createUserIdCookie(resp, createUniqueUserId());
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
  }

  private void createUserIdCookie(HttpServletResponse resp, String userId) {
    Cookie cookie = new Cookie(USER_ID, userId);
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setPath("/");
    resp.addCookie(cookie);
  }

  private String createUniqueUserId() {
    return UUID.randomUUID().toString();
  }

  private String getUserIdFromCookie(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    String userId = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(USER_ID)) {
          userId = cookie.getValue();
          break;
        }
      }
    }

    return userId;
  }
}
