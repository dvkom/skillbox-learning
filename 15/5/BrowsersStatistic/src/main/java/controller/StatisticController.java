package controller;

import com.google.gson.Gson;
import model.StatisticModel;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/statistic")
@MultipartConfig
public class StatisticController extends HttpServlet {
  private static final Logger log = Logger.getLogger(StatisticController.class);

  @Inject
  private volatile StatisticModel statisticModel;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    statisticModel.addStatistic(req);
    try {
      writeStatisticToResponse(resp);
    } catch (IOException e) {
      log.error(e);
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
  }

  private void writeStatisticToResponse(HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Integer> statistic = statisticModel.getStatistic();
    Gson gson = new Gson();
    String json = gson.toJson(statistic);
    resp.getWriter().write(json);
  }
}
