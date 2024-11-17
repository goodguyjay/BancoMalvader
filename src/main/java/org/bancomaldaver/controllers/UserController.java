package org.bancomaldaver.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.models.Address;
import org.bancomaldaver.models.CheckingAccount;
import org.bancomaldaver.models.Customer;
import org.bancomaldaver.models.SavingsAccount;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

public final class UserController {
  private static final Logger logger = Logger.getLogger(UserController.class.getName());

  public void createSavingsAccount(Customer customer, SavingsAccount account) throws Exception {
    validateSavingsAccount(customer, account);

    var userId = insertUser(customer);

    insertAddress(userId, customer.getAddress());

    var customerId = insertCustomer(userId);

    insertSavingsAccount(customerId, account);
  }

  public void createCheckingAccount(Customer customer, CheckingAccount account) throws Exception {
    validateCheckingAccount(customer, account);

    var userId = insertUser(customer);

    insertAddress(userId, customer.getAddress());

    var customerId = insertCustomer(userId);

    insertCheckingAccount(customerId, account);
  }

  public boolean validateCustomerLogin(String cpf, String password, String branch)
      throws Exception {
    final String query =
        "SELECT COUNT(*) FROM user u "
            + "INNER JOIN account a ON u.id_user = a.id_customer "
            + "WHERE u.cpf = ? AND u.password = ? AND a.branch = ?";
    int count = DatabaseWrapper.executeQueryForSingleInt(query, cpf, password, branch);

    return count > 0;
  }

  private void validateSavingsAccount(Customer customer, SavingsAccount account) {
    if (customer.getName() == null || customer.getCpf() == null || customer.getPassword() == null) {
      throw new IllegalArgumentException("As informações do cliente estão incompletas.");
    }

    Address address = customer.getAddress();
    if (address == null || address.getZipCode() == null || address.getCity() == null) {
      throw new IllegalArgumentException("As informações do endereço estão incompletas.");
    }

    if (account.getBranch() == null || account.getInterestRate() <= 0) {
      throw new IllegalArgumentException("As informações da conta poupança estão incompletas.");
    }
  }

  private void validateCheckingAccount(Customer customer, CheckingAccount account) {
    if (customer.getName() == null || customer.getCpf() == null || customer.getPassword() == null) {
      throw new IllegalArgumentException("As informações do cliente estão incompletas.");
    }

    Address address = customer.getAddress();
    if (address == null || address.getZipCode() == null || address.getCity() == null) {
      throw new IllegalArgumentException("As informações do endereço estão incompletas.");
    }

    if (account.getBranch() == null || account.getLimit() <= 0 || account.getDueDate() == null) {
      throw new IllegalArgumentException("As informações da conta corrente estão incompletas.");
    }
  }

  private int insertUser(Customer customer) throws Exception {
    var userId =
        DatabaseWrapper.executeUpdate(
            SQLQueries.INSERT_USER,
            customer.getName(),
            customer.getCpf(),
            customer.getBirthDate().toString(),
            customer.getPhone(),
            customer.getPassword(),
            "CUSTOMER");

    if (userId == 0) {
      throw new IllegalArgumentException("Erro ao inserir usuário.");
    }

    logger.log(Level.INFO, "Usuário inserido com sucesso.");
    return userId;
  }

  private void insertAddress(int userId, Address address) throws Exception {
    DatabaseWrapper.executeUpdate(
        SQLQueries.INSERT_ADDRESS,
        userId,
        address.getZipCode(),
        address.getStreet(),
        address.getHouseNumber(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState());
  }

  private int insertCustomer(int userId) throws Exception {
    return DatabaseWrapper.executeUpdate(SQLQueries.INSERT_CUSTOMER, userId);
  }

  private void insertSavingsAccount(int customerId, SavingsAccount account) throws Exception {
    var accountNumber = generateAccountNumber();

    var accountId =
        DatabaseWrapper.executeUpdate(
            SQLQueries.INSERT_ACCOUNT,
            customerId,
            account.getBranch(),
            "SAVINGS",
            0.0,
            accountNumber // Pass the account number
            );

    DatabaseWrapper.executeUpdate(
        SQLQueries.INSERT_SAVINGS_ACCOUNT, accountId, account.getInterestRate());
  }

  private void insertCheckingAccount(int customerId, CheckingAccount account) throws Exception {
    var accountNumber = generateAccountNumber();

    var accountId =
        DatabaseWrapper.executeUpdate(
            SQLQueries.INSERT_ACCOUNT,
            customerId,
            account.getBranch(),
            "CHECKING",
            0.0,
            accountNumber);

    DatabaseWrapper.executeUpdate(
        SQLQueries.INSERT_CHECKING_ACCOUNT,
        accountId,
        account.getLimit(),
        account.getDueDate().toString());
  }

  private int generateAccountNumber() throws Exception {
    final String query = "SELECT MAX(account_number) FROM account";

    var maxAccountNumber = DatabaseWrapper.executeQueryForSingleInt(query);

    return (maxAccountNumber == 0) ? 10000 : maxAccountNumber + 1;
  }
}
