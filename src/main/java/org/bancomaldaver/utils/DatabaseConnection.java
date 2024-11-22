package org.bancomaldaver.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.util.logging.Logger;

/**
 * classe utilitária para gerenciar conexões com o banco de dados. utiliza o HikariCP, uma
 * biblioteca para gerenciamento de conexões (pooling). esta classe não pode ser instanciada.
 *
 * <p>jay, esse tipo de coisa é de pooling é necessária para a nossa aplicação? não.
 * overengineering.
 */
final class DatabaseConnection {

  private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

  /**
   * fonte de conexões configurada usando o HikariCP. todas as conexões são gerenciadas
   * automaticamente pelo pool.
   */
  private static final HikariDataSource dataSource;

  // bloco estático para inicializar as configurações da biblioteca
  static {
    var config = new HikariConfig();

    // URL de conexão com o banco de dados
    config.setJdbcUrl("jdbc:mysql://localhost:3306/banco_malvader");

    // credenciais de acesso ao banco
    config.setUsername("root");
    config.setPassword("masterkey");

    // configurações do pool de conexões
    config.setMaximumPoolSize(10); // número máximo de conexões no pool
    config.setMinimumIdle(2); // número mínimo de conexões ociosas no pool
    config.setIdleTimeout(
        30000); // tempo máximo que uma conexão ociosa pode permanecer aberta (30s)
    config.setMaxLifetime(1800000); // tempo máximo de vida de uma conexão (30min)
    config.setConnectionTimeout(30000); // tempo máximo para tentar estabelecer uma conexão (30s)

    // inicializa o pool de conexões.
    dataSource = new HikariDataSource(config);
  }

  /**
   * construtor privado para evitar a instanciação da classe.
   *
   * @throws UnsupportedOperationException sempre que for chamado.
   */
  private DatabaseConnection() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  /**
   * fornece uma conexão com o banco de dados a partir do pool.
   *
   * @return uma instância de {@code Connection}.
   * @throws RuntimeException se ocorrer um erro ao obter a conexão.
   */
  static Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch (Exception e) {
      logger.log(java.util.logging.Level.SEVERE, e.getMessage());
      throw new RuntimeException("Erro ao conectar ao banco de dados.");
    }
  }
}
