package org.bancomaldaver.utils;

import java.sql.*;
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

      var affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Falha ao inserir registro.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Falha ao obter ID gerado.");
        }
      }

    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar o update: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
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

  public static int executeQueryForSingleInt(String query, Object... parameters) throws Exception {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1); // Return the first column of the first row
        }
      }
    } catch (Exception e) {
      throw new Exception("Error executing query: " + e.getMessage(), e);
    }

    return 0; // Default value if no rows found
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
      "delete",
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
