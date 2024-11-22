package org.bancomaldaver.controllers;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.dao.CustomerDAO;
import org.bancomaldaver.models.Customer;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

/**
 * controlador responsável por gerenciar as operações relacionadas a usuários. permite criar novos
 * usuários com endereço, validar logins e recuperar informações de clientes.
 */
public final class UserController {

  private static final Logger logger = Logger.getLogger(UserController.class.getName());

  /**
   * cria um novo usuário com informações de endereço.
   *
   * @param customer o objeto {@code Customer} contendo os dados do cliente.
   * @return o id do usuário criado.
   * @throws IllegalArgumentException se o cpf já estiver cadastrado ou ocorrer erro na inserção.
   * @throws Exception se ocorrer um erro ao executar as queries.
   */
  public int createUserWithAddress(Customer customer) throws Exception {
    if (doesCpfExist(customer.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado.");
    }

    // insere o usuário no banco de dados
    DatabaseWrapper.executeQuery(
        SQLQueries.INSERT_USER,
        customer.getName(),
        customer.getCpf(),
        customer.getBirthDate().toString(),
        customer.getPhone(),
        customer.getPassword(),
        "CUSTOMER");

    // recupera o id do usuário inserido
    var userId =
        DatabaseWrapper.executeQueryForSingleInt(
            SQLQueries.SELECT_USER_ID_BY_CPF, customer.getCpf());

    if (userId == 0) {
      throw new IllegalArgumentException("Erro ao inserir usuário.");
    }

    logger.log(Level.INFO, "Usuário inserido com sucesso: ID " + userId);

    // insere o endereço do usuário
    var address = customer.getAddress();
    DatabaseWrapper.executeQuery(
        SQLQueries.INSERT_ADDRESS,
        userId,
        address.getZipCode(),
        address.getStreet(),
        address.getHouseNumber(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState());

    return userId;
  }

  /**
   * recupera os detalhes de um cliente com base no cpf.
   *
   * @param cpf o cpf do cliente.
   * @return um mapa contendo os detalhes do cliente.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public Map<String, String> getCustomerDetails(String cpf) throws Exception {
    return CustomerDAO.getCustomerDetailsByCpf(cpf);
  }

  /**
   * valida as credenciais de login de um cliente e retorna o id da conta associada.
   *
   * @param cpf o cpf do cliente.
   * @param password a senha do cliente.
   * @param branch a agência da conta.
   * @return o id da conta associada ao cliente, se as credenciais forem válidas.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public int validateCustomerLogin(String cpf, String password, String branch) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar tudo de novo.
    final String query =
        "SELECT a.id_account FROM user u "
            + "INNER JOIN customer c ON u.id_user = c.id_user "
            + "INNER JOIN account a ON c.id_customer = a.id_customer "
            + "WHERE u.cpf = ? AND u.password = ? AND a.branch = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, cpf, password, branch);
  }

  /**
   * verifica se um cpf já está registrado no banco de dados.
   *
   * @param cpf o cpf a ser verificado.
   * @return true se o cpf já existir, false caso contrário.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  private boolean doesCpfExist(String cpf) throws Exception {
    var count = DatabaseWrapper.executeQueryForSingleInt(SQLQueries.CHECK_CPF_EXISTS, cpf);
    return count > 0;
  }
}
