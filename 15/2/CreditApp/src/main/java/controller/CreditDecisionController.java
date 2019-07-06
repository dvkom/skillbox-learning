package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.CreditRequest;
import model.DecisionEngine;
import model.SingleObjectStorage;
import org.apache.log4j.Logger;
import utils.CreditRequestValidator;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/decision")
@MultipartConfig
public class CreditDecisionController extends HttpServlet {

  private static final Logger log = Logger.getLogger(CreditRequestValidator.class);

  private static final String PATH_TO_SCHEMA = "/creditRequestSchema.json";

  @Inject
  private volatile DecisionEngine decisionEngine;

  @Inject
  private volatile SingleObjectStorage<CreditRequest> storage;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String jsonString = req.getReader().readLine();
    if (jsonString != null && CreditRequestValidator.isValid(jsonString, PATH_TO_SCHEMA)) {
      log.info("JSON is valid: " + jsonString);
      ObjectMapper objectMapper = new ObjectMapper();
      CreditRequest creditRequest = objectMapper.readValue(jsonString, CreditRequest.class);
      storage.save(creditRequest);
      log.info("Deserialized object: " + creditRequest);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    CreditRequest creditRequest = storage.get();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.valueToTree(creditRequest);

    boolean creditDecision = decisionEngine.decide(creditRequest);
    ((ObjectNode) jsonResponse).put("creditDecision", creditDecision);
    String jsonString = objectMapper.writeValueAsString(jsonResponse);
    log.info("Serialized object: " + jsonString);

    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(jsonString);
  }
}