package org.bancomaldaver.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.util.logging.Logger;

final class DatabaseConnection {
  private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
  private static final HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://localhost:3306/banco_malvader");
    config.setUsername("root");
    config.setPassword("masterkey");
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(30000);

    dataSource = new HikariDataSource(config);
  }

  private DatabaseConnection() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  static Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch (Exception e) {
      logger.log(java.util.logging.Level.SEVERE, e.getMessage());
      throw new RuntimeException("Erro ao conectar ao banco de dados.");
    }
  }
}
