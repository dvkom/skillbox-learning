package dao;

import org.apache.log4j.Logger;
import utils.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoterDao {
  private static final Logger log = Logger.getLogger(VoterDao.class);
  private PreparedStatement statement;
  private int counter = 1;

  public VoterDao() {
    try {
      ConnectionFactory.getConnection().setAutoCommit(false);
      String sql = "INSERT INTO voter_count(name, birthDate) VALUES(?, ?)";
      statement = ConnectionFactory.getConnection().prepareStatement(sql);
    } catch (SQLException e) {
      log.error(e);
    }
  }

  public void countVoter(String name, String birthDay) throws SQLException {
    birthDay = birthDay.replace('.', '-');
    statement.setString(1, name);
    statement.setString(2, birthDay);
    statement.addBatch();
    counter++;
    if (counter % 40_000 == 0) {
      flushCounter();
    }
  }

  public void flushCounter() throws SQLException {
    statement.executeBatch();
    ConnectionFactory.getConnection().commit();
  }

  public void printVoterCounts() throws SQLException {
    String sql = "SELECT name, birthDate, COUNT(*) FROM voter_count " +
        "GROUP BY name, birthDate HAVING COUNT(*) > 1";
    ResultSet rs = ConnectionFactory.getConnection().createStatement().executeQuery(sql);
    while (rs.next()) {
      System.out.println("\t" + rs.getString("name") + " (" +
          rs.getString("birthDate") + ") - " + rs.getInt("COUNT(*)"));
    }
  }
}
