package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.CreditDecision;
import entity.CreditRequest;
import entity.CreditRequestView;
import model.DecisionEngine;
import model.CreditDecisionStorage;
import org.apache.log4j.Logger;
import utils.CreditRequestMapper;
import utils.CreditRequestValidator;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/decision")
@MultipartConfig
public class CreditDecisionController extends HttpServlet {

  private static final Logger log = Logger.getLogger(CreditRequestValidator.class);

  private static final String PATH_TO_SCHEMA = "/creditRequestSchema.json";

  @Inject
  private volatile DecisionEngine decisionEngine;

  @Inject
  private volatile CreditDecisionStorage decisionStorage;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    String sessionId = req.getSession().getId();

    try {
      String jsonRequest = req.getReader().readLine();

      if (jsonRequest != null && CreditRequestValidator.isValid(jsonRequest, PATH_TO_SCHEMA)) {
        log.info("JSON is valid: " + jsonRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        CreditRequestView creditRequestView =
            objectMapper.readValue(jsonRequest, CreditRequestView.class);
        log.debug("Deserialized object: " + creditRequestView);
        CreditRequest creditRequest = CreditRequestMapper.INSTANCE.getRequest(creditRequestView);

        decisionStorage.put(sessionId,
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

    HttpSession session = req.getSession();

    if (session != null) {
      CreditDecision creditDecision = decisionStorage.get(session.getId());
      writeDecisionToResponse(creditDecision, resp);
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
}