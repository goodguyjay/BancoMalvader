package org.bancomaldaver.utils;

/**
 * classe utilitária que contém as consultas sql usadas no sistema. esta classe não pode ser
 * instanciada.
 */
public final class SQLQueries {

  /**
   * construtor privado para evitar a instanciação da classe.
   *
   * @throws UnsupportedOperationException sempre que for chamado.
   */
  private SQLQueries() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  // queries relacionadas a usuário

  /**
   * consulta sql para inserir um novo usuário na tabela 'user'. os valores são definidos como
   * parâmetros (name, cpf, birth_date, phone, password, user_type).
   */
  public static final String INSERT_USER =
      "INSERT INTO user (name, cpf, birth_date, phone, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";

  /**
   * consulta sql para verificar se um cpf já existe na tabela 'user'. retorna a contagem de
   * registros encontrados.
   */
  public static final String CHECK_CPF_EXISTS = "SELECT COUNT(*) FROM user WHERE cpf = ?";

  /** consulta sql para buscar o id de um usuário com base no cpf na tabela 'user'. */
  public static final String SELECT_USER_ID_BY_CPF = "SELECT id_user FROM user WHERE cpf = ?";

  // queries relacionadas a funcionário

  /**
   * consulta sql para inserir um novo funcionário na tabela 'employee'. os valores são definidos
   * como parâmetros (employee_code, role, id_user).
   */
  public static final String INSERT_EMPLOYEE =
      "INSERT INTO employee (employee_code, role, id_user) VALUES (?, ?, ?)";

  // queries relacionadas a cliente

  /** consulta sql para buscar o id do cliente com base no id do usuário na tabela 'customer'. */
  public static final String SELECT_CUSTOMER_BY_USER_ID =
      "SELECT id_customer FROM customer WHERE id_user = ?";

  // queries relacionadas a endereço

  /**
   * consulta sql para inserir um novo endereço na tabela 'address'. os valores são definidos como
   * parâmetros (id_user, zip_code, street, house_number, neighborhood, city, state).
   */
  public static final String INSERT_ADDRESS =
      "INSERT INTO address (id_user, zip_code, street, house_number, neighborhood, city, state) VALUES (?, ?, ?, ?, ?, ?, ?)";
}
