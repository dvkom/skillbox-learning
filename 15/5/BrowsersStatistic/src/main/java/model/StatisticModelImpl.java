package model;

import eu.bitwalker.useragentutils.UserAgent;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ManagedBean
public class StatisticModelImpl implements StatisticModel {

  @Inject
  private volatile StatisticDao statisticDao;

  @Override
  public void addStatistic(HttpServletRequest req) {
    UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
    String browserInfo = userAgent.getBrowser().getName() +
        " " + userAgent.getBrowserVersion().toString();

    statisticDao.add(browserInfo);
  }

  @Override
  public Map<String, Integer> getStatistic() {
    return statisticDao.getAll();
  }
}
