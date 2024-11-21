package org.bancomaldaver.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DatabaseWrapper {
  private static final Logger logger = Logger.getLogger(DatabaseWrapper.class.getName());

  private DatabaseWrapper() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  public static int executeUpdate(String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      setParameters(statement, parameters);
      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warning("Nenhuma linha foi afetada pela operação.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1); // Return the first generated key
        } else {
          throw new SQLException("Nenhuma chave gerada foi retornada.");
        }
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Erro ao executar o update: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  public static List<Map<String, String>> executeQueryForMultipleResults(
      String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<Map<String, String>> results = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
          Map<String, String> row = new HashMap<>();
          for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = resultSet.getObject(i);
            row.put(columnName, value != null ? value.toString() : "");
          }
          results.add(row);
        }

        return results;
      }

    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Erro ao executar query: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  public static int executeDelete(String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      var affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warning("Nenhuma linha foi deletada.");
      }

      return affectedRows;

    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar o delete: " + e.getMessage());
      throw new RuntimeException("Operação de exclusão no banco de dados cancelada.");
    }
  }

  public static ResultSet executeQuery(String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);
      return statement.executeQuery();

    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar query: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  public static double executeQueryForSingleDouble(String query, Object... parameters)
      throws Exception {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getDouble(1); // Return the first column of the first row
        } else {
          throw new SQLException("Nenhum resultado encontrado para a consulta.");
        }
      }
    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar query: " + e.getMessage());
      throw new RuntimeException("Erro ao executar consulta no banco de dados.");
    }
  }

  public static int executeQueryForSingleInt(String query, Object... parameters) throws Exception {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1);
        }
      }
    } catch (Exception e) {
      throw new Exception("Error executing query: " + e.getMessage(), e);
    }

    return 0; // Default value if no rows found
  }

  public static String executeQueryForSingleString(String query, Object... parameters)
      throws Exception {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getString(1); // Return the first column of the first row
        }
      }
    } catch (Exception e) {
      throw new Exception("Error executing query: " + e.getMessage(), e);
    }

    return null; // Default value if no rows found
  }

  public static Map<String, Object> executeQueryForSingleResult(
      String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          Map<String, Object> result = new HashMap<>();
          ResultSetMetaData metaData = resultSet.getMetaData();
          int columnCount = metaData.getColumnCount();

          for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = resultSet.getObject(i);
            result.put(columnName, value);
          }

          return result;
        } else {
          throw new SQLException("Nenhum resultado encontrado.");
        }
      }

    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar query: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  public static void closeResources(ResultSet resultSet, PreparedStatement statement) {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
    } catch (SQLException e) {
      logger.log(java.util.logging.Level.WARNING, "Erro ao finalizar a conexão: " + e.getMessage());
    }
  }

  private static void setParameters(PreparedStatement statement, Object... parameters)
      throws SQLException {
    for (var i = 0; i < parameters.length; i++) {
      statement.setObject(i + 1, parameters[i]);
    }
  }

  private static boolean isQuerySafe(String query) {
    String[] disallowed = {
      ";",
      "'",
      "--",
      "/*",
      "*/",
      "xp_", // Esse é específico pra SQL Server, mas sei lá, deixa aí.
      "exec",
      "drop",
      "truncate",
      "alter"
    };

    for (String disallowedPattern : disallowed) {
      if (query.toLowerCase().contains(disallowedPattern)) {
        logger.warning("Pattern proibido na query: " + disallowedPattern);
        return false;
      }
    }

    return true;
  }
}
