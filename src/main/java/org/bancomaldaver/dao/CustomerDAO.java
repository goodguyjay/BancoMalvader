package org.bancomaldaver.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bancomaldaver.utils.DatabaseWrapper;

/**
 * classe de acesso a dados (dao) para operações relacionadas a clientes. fornece métodos para
 * gerenciar informações de clientes, contas bancárias e transações.
 */
public final class CustomerDAO {

  /**
   * recupera os detalhes de um cliente com base no cpf fornecido. os dados incluem informações
   * pessoais e o endereço, caso esteja cadastrado.
   *
   * @param cpf o cpf do cliente.
   * @return um mapa contendo os detalhes do cliente, incluindo um endereço formatado.
   * @throws Exception se o cliente não for encontrado.
   */
  public static Map<String, String> getCustomerDetailsByCpf(String cpf) throws Exception {
    final String query =
        "SELECT u.name, u.cpf, u.birth_date, u.phone, "
            + "a.zip_code, a.street, a.house_number, a.neighborhood, a.city, a.state "
            + "FROM customer c "
            + "INNER JOIN user u ON c.id_user = u.id_user "
            + "LEFT JOIN address a ON u.id_user = a.id_user "
            + "WHERE u.cpf = ?";

    // executa a query e obtém os resultados como um mapa de objetos
    Map<String, Object> rawData = DatabaseWrapper.executeQueryForSingleResult(query, cpf);

    if (rawData.isEmpty()) {
      throw new Exception("Cliente não encontrado.");
    }

    // converte o mapa de objetos em um mapa de strings
    Map<String, String> formattedData = new HashMap<>();

    for (Map.Entry<String, Object> entry : rawData.entrySet()) {
      formattedData.put(
          entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "");
    }

    // formata o endereço em uma única string
    var address =
        String.format(
            "%s, %s, %s, %s, %s, %s",
            rawData.get("street"),
            rawData.get("house_number"),
            rawData.get("neighborhood"),
            rawData.get("city"),
            rawData.get("state"),
            rawData.get("zip_code"));

    formattedData.put("address", address);
    return formattedData;
  }

  /**
   * recupera o saldo de uma conta bancária com base no id da conta.
   *
   * @param accountId o id da conta.
   * @return o saldo da conta.
   * @throws Exception se ocorrer um erro na consulta.
   */
  public static double getBalance(int accountId) throws Exception {
    final String query = "SELECT balance FROM account WHERE id_account = ?";

    return DatabaseWrapper.executeQueryForSingleDouble(query, accountId);
  }

  /**
   * recupera o limite de crédito de uma conta corrente com base no id da conta.
   *
   * @param accountId o id da conta.
   * @return o limite de crédito da conta.
   * @throws Exception se ocorrer um erro na consulta.
   */
  public static double getCreditLimit(int accountId) throws Exception {
    final String query = "SELECT credit_limit FROM checking_account WHERE id_account = ?";

    return DatabaseWrapper.executeQueryForSingleDouble(query, accountId);
  }

  /**
   * autentica um cliente com base no id da conta e na senha fornecida.
   *
   * @param accountId o id da conta.
   * @param password a senha do cliente.
   * @return true se a autenticação for bem-sucedida, false caso contrário.
   * @throws Exception se ocorrer um erro na consulta.
   */
  public static boolean authenticate(int accountId, String password) throws Exception {
    final String query =
        "SELECT COUNT(*) FROM user u "
            + "INNER JOIN customer c ON u.id_user = c.id_user "
            + "INNER JOIN account a ON c.id_customer = a.id_customer "
            + "WHERE a.id_account = ? AND u.password = ?";

    var count = DatabaseWrapper.executeQueryForSingleInt(query, accountId, password);

    return count > 0;
  }

  /**
   * atualiza o saldo de uma conta bancária, adicionando o valor fornecido.
   *
   * @param accountId o id da conta.
   * @param amount o valor a ser adicionado ao saldo (pode ser negativo para débitos).
   * @throws Exception se ocorrer um erro na atualização.
   */
  public static void updateBalance(int accountId, double amount) throws Exception {
    final String query = "UPDATE account SET balance = balance + ? WHERE id_account = ?";

    DatabaseWrapper.executeUpdateTerribleFix(query, accountId, amount, accountId);
  }

  /**
   * insere uma transação financeira para a conta especificada.
   *
   * @param accountId o id da conta associada à transação.
   * @param transactionType o tipo de transação (ex: "DEPOSITO", "SAQUE").
   * @param amount o valor da transação.
   * @throws Exception se ocorrer um erro na inserção.
   */
  public static void insertTransaction(int accountId, String transactionType, double amount)
      throws Exception {
    final String query =
        "INSERT INTO transaction (transaction_type, amount, id_account) VALUES (?, ?, ?)";

    DatabaseWrapper.executeUpdateTerribleFix(query, accountId, transactionType, amount, accountId);
  }

  /**
   * recupera todas as transações financeiras de uma conta específica.
   *
   * @param accountId o id da conta.
   * @return uma lista de mapas onde cada mapa representa uma transação.
   * @throws Exception se ocorrer um erro na consulta.
   */
  public static List<Map<String, String>> getTransactions(int accountId) throws Exception {
    final String query =
        "SELECT transaction_type, amount, transaction_date "
            + "FROM transaction WHERE id_account = ? ORDER BY transaction_date ASC";

    return DatabaseWrapper.executeQueryForMultipleResults(query, accountId);
  }
}
