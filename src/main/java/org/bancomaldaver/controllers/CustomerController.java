package org.bancomaldaver.controllers;

import static org.bancomaldaver.dao.CustomerDAO.authenticate;

import java.util.List;
import java.util.Map;
import org.bancomaldaver.dao.CustomerDAO;
import org.bancomaldaver.utils.DatabaseWrapper;

public final class CustomerController {
  public double getBalance(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getBalance(accountId);
  }

  public void deposit(int accountId, double amount) throws Exception {
    if (amount <= 0) {
      throw new IllegalArgumentException("Valor do depósito deve ser maior que zero.");
    }
    CustomerDAO.updateBalance(accountId, amount);
    CustomerDAO.insertTransaction(accountId, "DEPOSIT", amount);
  }

  public void withdraw(int accountId, double amount, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }

    double balance = CustomerDAO.getBalance(accountId);
    if (amount > balance) {
      throw new IllegalArgumentException("Saldo insuficiente.");
    }
    CustomerDAO.updateBalance(accountId, -amount);
    CustomerDAO.insertTransaction(accountId, "WITHDRAWAL", -amount);
  }

  public double getCreditLimit(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getCreditLimit(accountId);
  }

  public String getAccountType(int accountId) throws Exception {
    final String query = "SELECT account_type FROM account WHERE id_account = ?";
    return DatabaseWrapper.executeQueryForSingleString(query, accountId);
  }

  public List<Map<String, String>> getStatement(int accountId, String password) throws Exception {
    if (!authenticate(accountId, password)) {
      throw new IllegalArgumentException("Senha incorreta.");
    }
    return CustomerDAO.getTransactions(accountId);
  }
}
