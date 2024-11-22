package org.bancomaldaver.controllers;

import static org.bancomaldaver.dao.CustomerDAO.authenticate;

import java.util.List;
import java.util.Map;
import org.bancomaldaver.dao.CustomerDAO;
import org.bancomaldaver.utils.DatabaseWrapper;

/**
 * controlador responsável por gerenciar as operações relacionadas aos clientes. atua como
 * intermediário entre os dados (dao) e a interface de usuário.
 */
public final class CustomerController {

  /**
   * retorna o saldo de uma conta após autenticar o cliente.
   *
   * @param accountId o id da conta.
   * @param password a senha do cliente.
   * @return o saldo da conta.
   * @throws IllegalArgumentException se a senha estiver incorreta.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public double getBalance(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getBalance(accountId);
  }

  /**
   * realiza um depósito em uma conta específica.
   *
   * @param accountId o id da conta.
   * @param amount o valor do depósito (deve ser maior que zero).
   * @throws IllegalArgumentException se o valor for menor ou igual a zero.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public void deposit(int accountId, double amount) throws Exception {
    if (amount <= 0) {
      throw new IllegalArgumentException("Valor do depósito deve ser maior que zero.");
    }
    CustomerDAO.updateBalance(accountId, amount);
    CustomerDAO.insertTransaction(accountId, "DEPOSIT", amount);
  }

  /**
   * realiza um saque em uma conta após autenticar o cliente.
   *
   * @param accountId o id da conta.
   * @param amount o valor do saque (deve ser menor ou igual ao saldo disponível).
   * @param password a senha do cliente.
   * @throws IllegalArgumentException se a senha estiver incorreta ou o saldo for insuficiente.
   * @throws Exception se ocorrer um erro durante a operação.
   */
  public void withdraw(int accountId, double amount, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }

    var balance = CustomerDAO.getBalance(accountId);

    if (amount > balance) {
      throw new IllegalArgumentException("Saldo insuficiente.");
    }
    CustomerDAO.updateBalance(accountId, -amount);
    CustomerDAO.insertTransaction(accountId, "WITHDRAWAL", -amount);
  }

  /**
   * retorna o limite de crédito de uma conta corrente após autenticar o cliente.
   *
   * @param accountId o id da conta.
   * @param password a senha do cliente.
   * @return o limite de crédito da conta.
   * @throws IllegalArgumentException se a senha estiver incorreta.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public double getCreditLimit(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getCreditLimit(accountId);
  }

  /**
   * retorna o tipo da conta (corrente ou poupança) com base no id da conta.
   *
   * @param accountId o id da conta.
   * @return o tipo da conta como string.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public String getAccountType(int accountId) throws Exception {
    final String query = "SELECT account_type FROM account WHERE id_account = ?";
    return DatabaseWrapper.executeQueryForSingleString(query, accountId);
  }

  /**
   * retorna o extrato de transações de uma conta após autenticar o cliente.
   *
   * @param accountId o id da conta.
   * @param password a senha do cliente.
   * @return uma lista de mapas onde cada mapa representa uma transação.
   * @throws IllegalArgumentException se a senha estiver incorreta.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public List<Map<String, String>> getStatement(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getTransactions(accountId);
  }
}
