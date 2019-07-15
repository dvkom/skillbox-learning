package utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
  private static final Logger log = Logger.getLogger(ConnectionFactory.class);
  private static final String DB_NAME = "learn";
  private static final String DB_USER = "root";
  private static final String DB_PASS = "mmm333";

  public static Connection getConnection() {
    return ConnectionHolder.instance;
  }

  private static class ConnectionHolder {
    private static volatile Connection instance;

    static {
      try {
        instance = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME +
                "?useSSL=false" + "&user=" + DB_USER + "&password=" + DB_PASS +
                "&rewriteBatchedStatements=true");
        createTable();
      } catch (SQLException e) {
        log.error(e);
      }
    }

    private static void createTable() throws SQLException {
      instance.createStatement().execute("CREATE TABLE " +
          "IF NOT EXISTS site_visit(" +
          "id INT NOT NULL AUTO_INCREMENT, " +
          "browser VARCHAR(50), " +
          "visitDate DATETIME, " +
          "PRIMARY KEY(id))");
    }
  }
}
