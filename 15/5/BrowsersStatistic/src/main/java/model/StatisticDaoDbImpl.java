package model;

import org.apache.log4j.Logger;
import utils.ConnectionFactory;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Default;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@Default
public class StatisticDaoDbImpl implements StatisticDao {
  private static final Logger log = Logger.getLogger(StatisticDaoDbImpl.class);

  @Override
  public void add(String browserInfo) {
    String sql = "INSERT INTO site_visit(browser, visitDate) " +
        "VALUES('" + browserInfo + "', NOW())";
    try {
      ConnectionFactory.getConnection().createStatement().execute(sql);
    } catch (SQLException e) {
      log.error(e);
    }
  }

  @Override
  public Map<String, Integer> getAll() {
    String sql = "SELECT browser, COUNT(*) FROM site_visit " +
        "GROUP BY browser HAVING COUNT(*) > 1";
    Map<String, Integer> statistic = new HashMap<>();
    try {
      ResultSet rs = ConnectionFactory.getConnection().createStatement().executeQuery(sql);
      while (rs.next()) {
        String browser = rs.getString("browser");
        int count = rs.getInt("COUNT(*)");
        statistic.put(browser, count);
      }
    } catch (SQLException e) {
      log.error(e);
    }

    return statistic;
  }
}
