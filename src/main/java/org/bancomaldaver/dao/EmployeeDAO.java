package org.bancomaldaver.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bancomaldaver.utils.DatabaseWrapper;

public final class EmployeeDAO {

  public static Map<String, String> getEmployeeDetailsByCode(String employeeCode) throws Exception {
    final String query =
        "SELECT e.employee_code, e.role, u.name, u.cpf, u.birth_date, u.phone, "
            + "a.zip_code, a.street, a.house_number, a.neighborhood, a.city, a.state "
            + "FROM employee e "
            + "INNER JOIN user u ON e.id_user = u.id_user "
            + "LEFT JOIN address a ON u.id_user = a.id_user "
            + "WHERE e.employee_code = ?";

    Map<String, Object> rawData = DatabaseWrapper.executeQueryForSingleResult(query, employeeCode);

    if (rawData.isEmpty()) {
      throw new Exception("Funcionário não encontrado.");
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

  public static List<Map<String, String>> getFinancialTransactions() throws Exception {
    final String query =
        "SELECT t.id_transaction, a.account_number, a.account_type, t.transaction_type, "
            + "       t.amount, t.transaction_date, u.name, u.cpf "
            + "FROM transaction t "
            + "INNER JOIN account a ON t.id_account = a.id_account "
            + "INNER JOIN customer c ON a.id_customer = c.id_customer "
            + "INNER JOIN user u ON c.id_user = u.id_user "
            + "ORDER BY t.transaction_date ASC";

    return DatabaseWrapper.executeQueryForMultipleResults(query);
  }
}
