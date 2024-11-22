package org.bancomaldaver.dao;

import java.util.Map;
import org.bancomaldaver.models.CheckingAccount;
import org.bancomaldaver.models.SavingsAccount;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

/**
 * classe de acesso a dados (dao) para operações relacionadas a contas bancárias. esta classe
 * gerencia a criação, recuperação e exclusão de contas e clientes associados.
 */
public final class AccountDAO {

  /**
   * cria um novo cliente associado ao id do usuário.
   *
   * @param userId o id do usuário.
   * @return o id do cliente criado.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public int createCustomer(int userId) throws Exception {
    final String query = "INSERT INTO customer (id_user) VALUES (?)";
    DatabaseWrapper.executeQuery(query, userId);

    return DatabaseWrapper.executeQueryForSingleInt(SQLQueries.SELECT_CUSTOMER_BY_USER_ID, userId);
  }

  /**
   * cria uma nova conta bancária para um cliente.
   *
   * @param customerId o id do cliente.
   * @param branch a agência associada à conta.
   * @param accountType o tipo da conta (ex: "CHECKING", "SAVINGS").
   * @return o id da conta criada.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public int createAccount(int customerId, String branch, String accountType) throws Exception {
    var accountNumber = generateAccountNumber();

    final String query =
        "INSERT INTO account (account_number, id_customer, branch, account_type, balance) "
            + "VALUES (?, ?, ?, ?, 0.0)";

    return DatabaseWrapper.executeQuery(query, accountNumber, customerId, branch, accountType);
  }

  /**
   * cria uma conta corrente associada a uma conta existente.
   *
   * @param accountId o id da conta existente.
   * @param account a conta corrente com limite de crédito e data de vencimento.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public void createCheckingAccount(int accountId, CheckingAccount account) throws Exception {
    final String query =
        "INSERT INTO checking_account (id_account, credit_limit, due_date) VALUES (?, ?, ?)";

    DatabaseWrapper.executeQuery(query, accountId, account.getLimit(), account.getDueDate());
  }

  /**
   * cria uma conta poupança associada a uma conta existente.
   *
   * @param accountId o id da conta existente.
   * @param account a conta poupança com taxa de rendimento.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public void createSavingsAccount(int accountId, SavingsAccount account) throws Exception {
    final String query = "INSERT INTO savings_account (id_account, interest_rate) VALUES (?, ?)";

    DatabaseWrapper.executeQuery(query, accountId, account.getInterestRate());
  }

  /**
   * busca o id do usuário associado ao cpf fornecido.
   *
   * @param cpf o cpf do usuário.
   * @return o id do usuário.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public int getUserIdByCpf(String cpf) throws Exception {
    final String query = "SELECT id_user FROM user WHERE cpf = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, cpf);
  }

  /**
   * busca o id do cliente associado ao id do usuário fornecido.
   *
   * @param userId o id do usuário.
   * @return o id do cliente.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public int getCustomerIdByUserId(int userId) throws Exception {
    final String query = "SELECT id_customer FROM customer WHERE id_user = ?";

    return DatabaseWrapper.executeQueryForSingleInt(query, userId);
  }

  /**
   * busca o número da conta associado ao id do cliente fornecido.
   *
   * @param customerId o id do cliente.
   * @return o número da conta.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public String getAccountNumberByCustomerId(int customerId) throws Exception {
    final String query = "SELECT account_number FROM account WHERE id_customer = ?";

    return DatabaseWrapper.executeQueryForSingleString(query, customerId);
  }

  /**
   * exclui uma conta com base no número da conta fornecido.
   *
   * @param accountNumber o número da conta a ser excluída.
   * @return true se a conta foi excluída com sucesso, false caso contrário.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public boolean deleteAccount(String accountNumber) throws Exception {
    final String query = "DELETE FROM account WHERE account_number = ?";

    var rowsAffected = DatabaseWrapper.executeDelete(query, accountNumber);
    return rowsAffected > 0;
  }

  /**
   * busca os detalhes de uma conta com base no número da conta fornecido.
   *
   * @param accountNumber o número da conta.
   * @return um mapa contendo os detalhes da conta, incluindo saldo e informações específicas de
   *     poupança ou corrente.
   * @throws Exception se a conta não for encontrada.
   */
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

  /**
   * gera um novo número de conta único com base no maior número de conta já registrado.
   *
   * @return o número de conta gerado.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  private int generateAccountNumber() throws Exception {
    final String query = "SELECT MAX(account_number) FROM account";

    var maxAccountNumber = DatabaseWrapper.executeQueryForSingleInt(query);

    return (maxAccountNumber == 0) ? 10000 : maxAccountNumber + 1;
  }
}
