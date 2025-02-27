package Base;

import java.sql.*;

public class DbMTEST {

  private static final String DB_URL = "jdbc:sqlserver://103.231.41.61;databaseName=XPRESSO_MTEST";
  private static final String USER = "sa";
  private static final String PASS = "UAT_R0INET_SQL";
  private Connection connection;

  public DbMTEST() {
    try {
      connection = DriverManager.getConnection(DB_URL, USER, PASS);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public ResultSet executeQuery(String query) {
    ResultSet resultSet = null;
    try {
      Statement stmt = connection.createStatement();
      resultSet = stmt.executeQuery(query);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
    return resultSet;
  }

  // Inside your dbMTEST class:
  public int executeUpdate(String query) {

    try (Statement stmt = connection.createStatement()) {
      return stmt.executeUpdate(query);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return 0;
    }
    /*int rowsAffected = 0;
    try {
      Statement stmt = connection.createStatement();
      rowsAffected = stmt.executeUpdate(query); // executeUpdate for non-SELECT queries
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return rowsAffected; // returns the number of rows affected by the query*/
  }

  /*public int executeUpdate(String query, Object... params) {
    int result = 0;
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        pstmt.setObject(i + 1, params[i]);
      }
      result = pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }*/

  // Method to execute SELECT queries with PreparedStatement
  public ResultSet executePreparedQuery(String query, String parameter) {

    try {
      PreparedStatement stmt = connection.prepareStatement(query);
      stmt.setString(1, parameter);
      return stmt.executeQuery();  // Caller must close ResultSet & PreparedStatement
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
   /* ResultSet resultSet = null;
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, parameter);  // Set the parameter (e.g., usercode)

      // Execute the query
      resultSet = stmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;*/
  }

  /*public ResultSet executePreparedQuery(String query, Object... params) {
    ResultSet resultSet = null;
    try {
      PreparedStatement pstmt = connection.prepareStatement(query);
      for (int i = 0; i < params.length; i++) {
        pstmt.setObject(i + 1, params[i]);
      }
      resultSet = pstmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }*/
// Execute an UPDATE, INSERT, DELETE query with PreparedStatement
  public int executeUpdate(String query, Object... params) {
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        pstmt.setObject(i + 1, params[i]);  // Set parameters dynamically
      }
      return pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  // Close connection when done
  public void closeConnection() {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
