package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.CreditDecision;
import entity.CreditRequest;
import entity.CreditRequestView;
import model.DecisionEngine;
import model.CreditDecisionStorage;
import model.FormDataStorage;
import org.apache.log4j.Logger;
import utils.CreditRequestMapper;
import utils.CreditRequestValidator;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/decision")
@MultipartConfig
public class CreditDecisionController extends HttpServlet {

  private static final Logger log = Logger.getLogger(CreditRequestValidator.class);

  private static final String PATH_TO_SCHEMA = "/creditRequestSchema.json";

  private static final String GET_DATA = "getData";

  private static final String GET_DECISION = "getDecision";

  private static final String USER_ID = "userID";

  private static final int COOKIE_MAX_AGE = 30 * 30 * 24 * 30;

  @Inject
  private volatile DecisionEngine decisionEngine;

  @Inject
  private volatile CreditDecisionStorage decisionStorage;

  @Inject
  private volatile FormDataStorage formDataStorage;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    String userId = getUserIdFromCookie(req);
    if (userId == null) {
      userId = createUniqueUserId();
      createUserIdCookie(resp, userId);
    }

    try {
      String jsonRequest = req.getReader().readLine();
      String jsonFormData = req.getReader().readLine();

      if (jsonFormData != null) {
        log.info("JSON form data: " + jsonFormData);
        formDataStorage.put(userId, jsonFormData);
      }

      if (jsonRequest != null && CreditRequestValidator.isValid(jsonRequest, PATH_TO_SCHEMA)) {
        log.info("JSON is valid: " + jsonRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        CreditRequestView creditRequestView =
            objectMapper.readValue(jsonRequest, CreditRequestView.class);
        log.debug("Deserialized object: " + creditRequestView);
        CreditRequest creditRequest = CreditRequestMapper.INSTANCE.getRequest(creditRequestView);

        decisionStorage.put(userId,
            new CreditDecision(creditRequest, decisionEngine.decide(creditRequest)));

        log.debug("After mapping object: " + creditRequest);
      }

    } catch (IOException e) {
      log.error(e);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    String userId = getUserIdFromCookie(req);

    if (userId != null) {
      String action = req.getParameter("action");
      if (action.equals(GET_DATA)) {
        String formData = formDataStorage.get(userId);
        log.info("Form data received: " + formData);
        writeFormDataToResponse(formData, resp);
      } else if (action.equals(GET_DECISION)) {
        CreditDecision creditDecision = decisionStorage.get(userId);
        writeDecisionToResponse(creditDecision, resp);
      }
    }
  }

  private void writeFormDataToResponse(String formData, HttpServletResponse resp) {

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

  private void writeDecisionToResponse(CreditDecision creditDecision, HttpServletResponse resp) {

    if (creditDecision != null) {
      ObjectMapper objectMapper = new ObjectMapper();
      CreditRequestView creditRequestView =
          CreditRequestMapper.INSTANCE.getView(creditDecision.getCreditRequest());
      JsonNode jsonResponse = objectMapper.valueToTree(creditRequestView);

      ((ObjectNode) jsonResponse).put("creditDecision", creditDecision.isApproved());

      try {
        String jsonString = objectMapper.writeValueAsString(jsonResponse);
        log.info("Serialized object: " + jsonString);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonString);

      } catch (IOException e) {
        log.error(e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    } else {
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