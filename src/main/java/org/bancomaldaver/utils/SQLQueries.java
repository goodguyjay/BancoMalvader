package org.bancomaldaver.utils;

public final class SQLQueries {

  private SQLQueries() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  // Queries de usuário
  public static final String INSERT_USER =
      "INSERT INTO user (name, cpf, birth_date, phone, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";
  public static final String CHECK_CPF_EXISTS = "SELECT COUNT(*) FROM user WHERE cpf = ?";
  public static final String SELECT_USER_ID_BY_CPF = "SELECT id_user FROM user WHERE cpf = ?";

  // Queries de funcionário
  public static final String INSERT_EMPLOYEE =
      "INSERT INTO employee (employee_code, role, id_user) VALUES (?, ?, ?)";

  // Queries de cliente
  public static final String SELECT_CUSTOMER_BY_USER_ID =
      "SELECT id_customer FROM customer WHERE id_user = ?";

  // Queries de endereço
  public static final String INSERT_ADDRESS =
      "INSERT INTO address (id_user, zip_code, street, house_number, neighborhood, city, state) VALUES (?, ?, ?, ?, ?, ?, ?)";
}
