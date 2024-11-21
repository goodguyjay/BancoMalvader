package org.bancomaldaver.controllers;

import java.util.Map;
import org.bancomaldaver.dao.AccountDAO;
import org.bancomaldaver.models.AccountClosureData;
import org.bancomaldaver.models.CheckingAccount;
import org.bancomaldaver.models.SavingsAccount;

public final class AccountController {
  private final AccountDAO accountDAO;

  public AccountController() {
    this.accountDAO = new AccountDAO();
  }

  public AccountClosureData getAccountClosureData(String cpf) throws Exception {
    int userId = accountDAO.getUserIdByCpf(cpf);
    int customerId = accountDAO.getCustomerIdByUserId(userId);
    var accountNumber = accountDAO.getAccountNumberByCustomerId(customerId);

    return new AccountClosureData(cpf, userId, customerId, accountNumber);
  }

  public void createCheckingAccount(int userId, CheckingAccount account) throws Exception {
    var customerId = accountDAO.createCustomer(userId);

    var accountId = accountDAO.createAccount(customerId, account.getBranch(), "CHECKING");

    accountDAO.createCheckingAccount(accountId, account);
  }

  public void createSavingsAccount(int userId, SavingsAccount account) throws Exception {
    var customerId = accountDAO.createCustomer(userId);

    var accountId = accountDAO.createAccount(customerId, account.getBranch(), "SAVINGS");

    accountDAO.createSavingsAccount(accountId, account);
  }

  public Map<String, Object> getAccountDetails(String accountNumber) throws Exception {
    return accountDAO.getAccountDetailsByNumber(accountNumber);
  }

  public boolean closeAccount(String accountNumber) throws Exception {
    return accountDAO.deleteAccount(accountNumber);
  }
}
