package model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface StatisticModel {
  void addStatistic(HttpServletRequest req);
  Map<String, Integer> getStatistic();

}
