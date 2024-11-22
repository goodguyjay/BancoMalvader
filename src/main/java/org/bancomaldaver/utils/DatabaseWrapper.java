package org.bancomaldaver.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * classe utilitária para operações com o banco de dados. oferece métodos para executar queries e
 * updates com validações de segurança contra injeção sql.
 */
public final class DatabaseWrapper {
  private static final Logger logger = Logger.getLogger(DatabaseWrapper.class.getName());

  /**
   * construtor privado para evitar instanciação da classe.
   *
   * @throws UnsupportedOperationException sempre que for chamado.
   */
  private DatabaseWrapper() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  /**
   * executa uma query de inserção ou atualização no banco de dados e retorna a chave gerada.
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return a chave primária gerada pela operação.
   */
  public static int executeQuery(String query, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      setParameters(statement, parameters);
      var affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warning("Nenhuma linha foi afetada pela operação.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Nenhuma chave gerada foi retornada.");
        }
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Erro ao executar o update: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  /**
   * executa uma atualização no banco de dados com uma correção temporária (não há solução mais
   * permanente que uma solução temporária...).
   *
   * <p>se Deus existe, me perdoe.
   *
   * @param query query sql a ser executada.
   * @param accountId id da conta que será retornado.
   * @param parameters parâmetros para a query.
   * @return o id da conta.
   */
  public static int executeUpdateTerribleFix(String query, int accountId, Object... parameters) {
    if (!isQuerySafe(query)) {
      throw new IllegalArgumentException("Tentativa de Injeção SQL detectada.");
    }

    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);
      var affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warning("Nenhuma linha foi afetada pela operação.");
      }

      return accountId;
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Erro ao executar o update: " + e.getMessage());
      throw new RuntimeException("Operação no banco de dados cancelada.");
    }
  }

  /**
   * executa uma consulta e retorna múltiplos resultados como uma lista de maps.
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return lista de resultados onde cada mapa representa uma linha da consulta.
   */
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
        var columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
          Map<String, String> row = new HashMap<>();
          for (var i = 1; i <= columnCount; i++) {
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

  /**
   * executa uma consulta para exclusão e retorna o número de linhas afetadas.
   *
   * <p>só eu e Deus sabíamos o pq eu fiz esse método. agora é só Deus.
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return número de linhas deletadas.
   */
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

  /**
   * executa uma consulta para exclusão e retorna o número de linhas afetadas.
   *
   * <p>java foi maldito comigo nessa. overload apenas de tipo de retorno deveria ser permitido. se
   * for... bem, código legado instantaneo :)
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return número de linhas deletadas.
   */
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
          return resultSet.getDouble(1);
        } else {
          throw new SQLException("Nenhum resultado encontrado para a consulta.");
        }
      }
    } catch (SQLException e) {
      logger.log(java.util.logging.Level.SEVERE, "Erro ao executar query: " + e.getMessage());
      throw new RuntimeException("Erro ao executar consulta no banco de dados.");
    }
  }

  /**
   * executa uma consulta para exclusão e retorna o número de linhas afetadas.
   *
   * <p>mesma coisa do anterior... fazer o que né.
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return número de linhas deletadas.
   */
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
      throw new Exception("Erro ao executar query: " + e.getMessage(), e);
    }

    return 0;
  }

  /**
   * executa uma consulta para exclusão e retorna o número de linhas afetadas.
   *
   * <p>mesma coisa do anterior... mas retona uma string :)
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return número de linhas deletadas.
   */
  public static String executeQueryForSingleString(String query, Object... parameters)
      throws Exception {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      setParameters(statement, parameters);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getString(1);
        }
      }
    } catch (Exception e) {
      throw new Exception("Erro ao executar query: " + e.getMessage(), e);
    }

    return null;
  }

  /**
   * executa uma consulta para exclusão e retorna o número de linhas afetadas.
   *
   * <p>mesma coisa do anterior... mas retorna um map :)
   *
   * @param query query sql a ser executada.
   * @param parameters parâmetros para a query.
   * @return número de linhas deletadas.
   */
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
          var columnCount = metaData.getColumnCount();

          for (var i = 1; i <= columnCount; i++) {
            var columnName = metaData.getColumnLabel(i);
            var value = resultSet.getObject(i);
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

  /**
   * define os parâmetros para o prepared statement.
   *
   * @param statement o prepared statement a ser configurado.
   * @param parameters os valores dos parâmetros a serem definidos.
   * @throws SQLException se ocorrer um erro ao definir os parâmetros.
   */
  private static void setParameters(PreparedStatement statement, Object... parameters)
      throws SQLException {
    for (var i = 0; i < parameters.length; i++) {
      statement.setObject(i + 1, parameters[i]);
    }
  }

  /**
   * verifica se a query é segura contra injeção sql.
   *
   * @param query a query sql a ser analisada.
   * @return true se a query for considerada segura; false caso contrário.
   */
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
