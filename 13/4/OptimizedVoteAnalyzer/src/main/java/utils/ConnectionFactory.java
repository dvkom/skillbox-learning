package utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
  private static final Logger log = Logger.getLogger(ConnectionFactory.class);
  private static Connection connection;
  private static String dbName = "learn";
  private static String dbUser = "root";
  private static String dbPass = "mmm333";

  public static Connection getConnection() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName +
            "?useSSL=false" + "&user=" + dbUser + "&password=" + dbPass +
            "&rewriteBatchedStatements=true");
//        createTable();
        truncateTable();
      } catch (SQLException e) {
        log.error(e);
      }
    }
    return connection;
  }

  private static void truncateTable() throws SQLException {
    connection.createStatement().execute("TRUNCATE TABLE voter_count");
  }

  private static void createTable() throws SQLException {
    connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
    connection.createStatement().execute("CREATE TABLE voter_count(" +
        "id INT NOT NULL AUTO_INCREMENT, " +
        "name TINYTEXT NOT NULL, " +
        "birthDate DATE NOT NULL, " +
        "PRIMARY KEY(id))");
  }
}
