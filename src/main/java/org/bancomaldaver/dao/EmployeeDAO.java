package org.bancomaldaver.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bancomaldaver.utils.DatabaseWrapper;

/**
 * classe de acesso a dados (dao) para operações relacionadas a funcionários. esta classe fornece
 * métodos para recuperar detalhes de funcionários e transações financeiras.
 */
public final class EmployeeDAO {

  /**
   * recupera os detalhes de um funcionário com base no código de funcionário fornecido. os dados
   * incluem informações pessoais, cargo e endereço (se disponível).
   *
   * @param employeeCode o código do funcionário.
   * @return um mapa contendo os detalhes do funcionário, incluindo um endereço formatado.
   * @throws Exception se o funcionário não for encontrado.
   */
  public static Map<String, String> getEmployeeDetailsByCode(String employeeCode) throws Exception {
    final String query =
        "SELECT e.employee_code, e.role, u.name, u.cpf, u.birth_date, u.phone, "
            + "a.zip_code, a.street, a.house_number, a.neighborhood, a.city, a.state "
            + "FROM employee e "
            + "INNER JOIN user u ON e.id_user = u.id_user "
            + "LEFT JOIN address a ON u.id_user = a.id_user "
            + "WHERE e.employee_code = ?";

    // executa a query e obtém os resultados como um mapa de objetos
    Map<String, Object> rawData = DatabaseWrapper.executeQueryForSingleResult(query, employeeCode);

    if (rawData.isEmpty()) {
      throw new Exception("Funcionário não encontrado.");
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
   * recupera todas as transações financeiras registradas no sistema. as transações incluem
   * informações sobre a conta associada, tipo de transação e detalhes do cliente.
   *
   * @return uma lista de mapas onde cada mapa representa uma transação financeira.
   * @throws Exception se ocorrer um erro ao executar a consulta.
   */
  public static List<Map<String, String>> getFinancialTransactions() throws Exception {
    final String query =
        "SELECT t.id_transaction, a.account_number, a.account_type, t.transaction_type, "
            + "       t.amount, t.transaction_date, u.name, u.cpf "
            + "FROM transaction t "
            + "INNER JOIN account a ON t.id_account = a.id_account "
            + "INNER JOIN customer c ON a.id_customer = c.id_customer "
            + "INNER JOIN user u ON c.id_user = u.id_user "
            + "ORDER BY t.transaction_date ASC";

    // executa a query e retorna múltiplos resultados como uma lista de mapas
    return DatabaseWrapper.executeQueryForMultipleResults(query);
  }
}
