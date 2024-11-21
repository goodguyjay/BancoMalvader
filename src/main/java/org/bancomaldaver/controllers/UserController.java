package org.bancomaldaver.controllers;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.dao.CustomerDAO;
import org.bancomaldaver.models.Customer;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

public final class UserController {
  private static final Logger logger = Logger.getLogger(UserController.class.getName());

  public int createUserWithAddress(Customer customer) throws Exception {
    if (doesCpfExist(customer.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado.");
    }

    DatabaseWrapper.executeUpdate(
        SQLQueries.INSERT_USER,
        customer.getName(),
        customer.getCpf(),
        customer.getBirthDate().toString(),
        customer.getPhone(),
        customer.getPassword(),
        "CUSTOMER");

    var userId =
        DatabaseWrapper.executeQueryForSingleInt(
            SQLQueries.SELECT_USER_ID_BY_CPF, customer.getCpf());

    if (userId == 0) {
      throw new IllegalArgumentException("Erro ao inserir usuário.");
    }

    logger.log(Level.INFO, "Usuário inserido com sucesso: ID " + userId);

    var address = customer.getAddress();
    DatabaseWrapper.executeUpdate(
        SQLQueries.INSERT_ADDRESS,
        userId,
        address.getZipCode(),
        address.getStreet(),
        address.getHouseNumber(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState());

    //    DatabaseWrapper.executeUpdate(SQLQueries.INSERT_CUSTOMER, userId);

    return userId;
  }

  private boolean doesCpfExist(String cpf) throws Exception {
    var count = DatabaseWrapper.executeQueryForSingleInt(SQLQueries.CHECK_CPF_EXISTS, cpf);
    return count > 0;
  }

  public Map<String, String> getCustomerDetails(String cpf) throws Exception {
    return CustomerDAO.getCustomerDetailsByCpf(cpf);
  }

  public int validateCustomerLogin(String cpf, String password, String branch) throws Exception {
    final String query =
        "SELECT a.id_account FROM user u "
            + "INNER JOIN customer c ON u.id_user = c.id_user "
            + "INNER JOIN account a ON c.id_customer = a.id_customer "
            + "WHERE u.cpf = ? AND u.password = ? AND a.branch = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, cpf, password, branch);
  }
}
