package org.bancomaldaver.controllers;

import java.util.Map;
import org.bancomaldaver.dao.AccountDAO;
import org.bancomaldaver.models.AccountClosureData;
import org.bancomaldaver.models.CheckingAccount;
import org.bancomaldaver.models.SavingsAccount;

/**
 * controlador responsável por gerenciar as operações relacionadas às contas bancárias. atua como um
 * intermediário entre os dados (dao) e a interface.
 */
public final class AccountController {

  /**
   * instância de {@code AccountDAO} para acessar operações no banco de dados relacionadas às
   * contas.
   */
  private final AccountDAO accountDAO;

  /** construtor padrão que inicializa o controlador com uma instância do {@code AccountDAO}. */
  public AccountController() {
    this.accountDAO = new AccountDAO();
  }

  /**
   * recupera os dados necessários para o encerramento de uma conta com base no cpf do cliente. os
   * dados incluem o id do usuário, id do cliente e o número da conta associada.
   *
   * @param cpf o cpf do cliente.
   * @return uma instância de {@code AccountClosureData} contendo os dados para o encerramento.
   * @throws Exception se ocorrer um erro ao buscar os dados.
   */
  public AccountClosureData getAccountClosureData(String cpf) throws Exception {
    var userId = accountDAO.getUserIdByCpf(cpf);
    var customerId = accountDAO.getCustomerIdByUserId(userId);
    var accountNumber = accountDAO.getAccountNumberByCustomerId(customerId);

    return new AccountClosureData(cpf, userId, customerId, accountNumber);
  }

  /**
   * cria uma conta corrente para um usuário com os dados fornecidos.
   *
   * @param userId o id do usuário.
   * @param account os dados da conta corrente, incluindo limite de crédito e agência.
   * @throws Exception se ocorrer um erro durante a criação.
   */
  public void createCheckingAccount(int userId, CheckingAccount account) throws Exception {
    var customerId = accountDAO.createCustomer(userId);

    var accountId = accountDAO.createAccount(customerId, account.getBranch(), "CHECKING");

    accountDAO.createCheckingAccount(accountId, account);
  }

  /**
   * cria uma conta poupança para um usuário com os dados fornecidos.
   *
   * @param userId o id do usuário.
   * @param account os dados da conta poupança, incluindo taxa de rendimento e agência.
   * @throws Exception se ocorrer um erro durante a criação.
   */
  public void createSavingsAccount(int userId, SavingsAccount account) throws Exception {
    var customerId = accountDAO.createCustomer(userId);

    var accountId = accountDAO.createAccount(customerId, account.getBranch(), "SAVINGS");

    accountDAO.createSavingsAccount(accountId, account);
  }

  /**
   * busca os detalhes de uma conta com base no número da conta.
   *
   * @param accountNumber o número da conta.
   * @return um mapa contendo os detalhes da conta, como saldo e tipo de conta.
   * @throws Exception se ocorrer um erro ao buscar os detalhes.
   */
  public Map<String, Object> getAccountDetails(String accountNumber) throws Exception {
    return accountDAO.getAccountDetailsByNumber(accountNumber);
  }

  /**
   * encerra uma conta com base no número da conta fornecido.
   *
   * @param accountNumber o número da conta a ser encerrada.
   * @return true se a conta foi encerrada com sucesso, false caso contrário.
   * @throws Exception se ocorrer um erro ao tentar encerrar a conta.
   */
  public boolean closeAccount(String accountNumber) throws Exception {
    return accountDAO.deleteAccount(accountNumber);
  }
}
