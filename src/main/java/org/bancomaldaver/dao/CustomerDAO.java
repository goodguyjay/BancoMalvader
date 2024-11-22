package org.bancomaldaver.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bancomaldaver.utils.DatabaseWrapper;

public final class CustomerDAO {
  public static Map<String, String> getCustomerDetailsByCpf(String cpf) throws Exception {
    final String query =
        "SELECT u.name, u.cpf, u.birth_date, u.phone, "
            + "a.zip_code, a.street, a.house_number, a.neighborhood, a.city, a.state "
            + "FROM customer c "
            + "INNER JOIN user u ON c.id_user = u.id_user "
            + "LEFT JOIN address a ON u.id_user = a.id_user "
            + "WHERE u.cpf = ?";

    Map<String, Object> rawData = DatabaseWrapper.executeQueryForSingleResult(query, cpf);

    if (rawData.isEmpty()) {
      throw new Exception("Cliente não encontrado.");
    }

    Map<String, String> formattedData = new HashMap<>();

    for (Map.Entry<String, Object> entry : rawData.entrySet()) {
      formattedData.put(
          entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "");
    }

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

  public static double getBalance(int accountId) throws Exception {
    final String query = "SELECT balance FROM account WHERE id_account = ?";

    return DatabaseWrapper.executeQueryForSingleDouble(query, accountId);
  }

  public static double getCreditLimit(int accountId) throws Exception {
    final String query = "SELECT credit_limit FROM checking_account WHERE id_account = ?";

    return DatabaseWrapper.executeQueryForSingleDouble(query, accountId);
  }

  public static boolean authenticate(int accountId, String password) throws Exception {
    final String query =
        "SELECT COUNT(*) FROM user u "
            + "INNER JOIN customer c ON u.id_user = c.id_user "
            + "INNER JOIN account a ON c.id_customer = a.id_customer "
            + "WHERE a.id_account = ? AND u.password = ?";

    var count = DatabaseWrapper.executeQueryForSingleInt(query, accountId, password);

    return count > 0;
  }

  public static void updateBalance(int accountId, double amount) throws Exception {
    final String query = "UPDATE account SET balance = balance + ? WHERE id_account = ?";

    DatabaseWrapper.executeUpdateTerribleFix(query, accountId, amount, accountId);
  }

  public static void insertTransaction(int accountId, String transactionType, double amount)
      throws Exception {
    final String query =
        "INSERT INTO transaction (transaction_type, amount, id_account) VALUES (?, ?, ?)";

    DatabaseWrapper.executeUpdateTerribleFix(query, accountId, transactionType, amount, accountId);
  }

  public static List<Map<String, String>> getTransactions(int accountId) throws Exception {
    final String query =
        "SELECT transaction_type, amount, transaction_date "
            + "FROM transaction WHERE id_account = ? ORDER BY transaction_date ASC";

    return DatabaseWrapper.executeQueryForMultipleResults(query, accountId);
  }
}
