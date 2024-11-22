package org.bancomaldaver.dao;

import java.util.Map;
import org.bancomaldaver.models.CheckingAccount;
import org.bancomaldaver.models.SavingsAccount;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

public final class AccountDAO {

  public int createCustomer(int userId) throws Exception {
    final String query = "INSERT INTO customer (id_user) VALUES (?)";
    DatabaseWrapper.executeQuery(query, userId);

    return DatabaseWrapper.executeQueryForSingleInt(SQLQueries.SELECT_CUSTOMER_BY_USER_ID, userId);
  }

  public int createAccount(int customerId, String branch, String accountType) throws Exception {
    var accountNumber = generateAccountNumber();

    final String query =
        "INSERT INTO account (account_number, id_customer, branch, account_type, balance) "
            + "VALUES (?, ?, ?, ?, 0.0)";

    return DatabaseWrapper.executeQuery(query, accountNumber, customerId, branch, accountType);
  }

  public void createCheckingAccount(int accountId, CheckingAccount account) throws Exception {
    final String query =
        "INSERT INTO checking_account (id_account, credit_limit, due_date) VALUES (?, ?, ?)";

    DatabaseWrapper.executeQuery(query, accountId, account.getLimit(), account.getDueDate());
  }

  public void createSavingsAccount(int accountId, SavingsAccount account) throws Exception {
    final String query = "INSERT INTO savings_account (id_account, interest_rate) VALUES (?, ?)";

    DatabaseWrapper.executeQuery(query, accountId, account.getInterestRate());
  }

  public int getUserIdByCpf(String cpf) throws Exception {
    final String query = "SELECT id_user FROM user WHERE cpf = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, cpf);
  }

  public int getCustomerIdByUserId(int userId) throws Exception {
    final String query = "SELECT id_customer FROM customer WHERE id_user = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, userId);
  }

  public String getAccountNumberByCustomerId(int customerId) throws Exception {
    final String query = "SELECT account_number FROM account WHERE id_customer = ?";

    return DatabaseWrapper.executeQueryForSingleString(query, customerId);
  }

  public boolean deleteAccount(String accountNumber) throws Exception {
    final String query = "DELETE FROM account WHERE account_number = ?";

    var rowsAffected = DatabaseWrapper.executeDelete(query, accountNumber);
    return rowsAffected > 0;
  }

  public Map<String, Object> getAccountDetailsByNumber(String accountNumber) throws Exception {
    final String query =
        "SELECT a.account_type, u.name, u.cpf, a.balance, "
            + "       c.credit_limit, c.due_date, s.interest_rate "
            + "FROM account a "
            + "LEFT JOIN checking_account c ON a.id_account = c.id_account "
            + "LEFT JOIN savings_account s ON a.id_account = s.id_account "
            + "INNER JOIN customer cust ON a.id_customer = cust.id_customer "
            + "INNER JOIN user u ON cust.id_user = u.id_user "
            + "WHERE a.account_number = ?";

    Map<String, Object> accountDetails =
        DatabaseWrapper.executeQueryForSingleResult(query, accountNumber);

    if (accountDetails.isEmpty()) {
      throw new Exception("Account not found.");
    }

    return accountDetails;
  }

  private int generateAccountNumber() throws Exception {
    final String query = "SELECT MAX(account_number) FROM account";

    var maxAccountNumber = DatabaseWrapper.executeQueryForSingleInt(query);

    return (maxAccountNumber == 0) ? 10000 : maxAccountNumber + 1;
  }
}
