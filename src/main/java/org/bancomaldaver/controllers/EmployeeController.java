package org.bancomaldaver.controllers;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.dao.EmployeeDAO;
import org.bancomaldaver.models.Address;
import org.bancomaldaver.models.Employee;
import org.bancomaldaver.utils.DatabaseWrapper;
import org.bancomaldaver.utils.SQLQueries;

/**
 * controlador responsável por gerenciar as operações relacionadas aos funcionários. permite criar,
 * autenticar e consultar informações de funcionários e suas transações financeiras.
 */
public class EmployeeController {

  private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());

  /**
   * cria um novo funcionário e insere suas informações no banco de dados. valida o cpf, dados
   * pessoais e endereço antes da inserção.
   *
   * @param employee o objeto {@code Employee} contendo os dados do funcionário.
   * @throws Exception se o cpf já existir ou se os dados estiverem incompletos.
   */
  public void createEmployee(Employee employee) throws Exception {
    if (doesCpfExist(employee.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado.");
    }

    validateEmployee(employee);

    var userId = insertUser(employee);

    insertAddress(userId, employee.getAddress());

    insertEmployee(userId, employee);
  }

  /**
   * retorna o nome de um funcionário com base no código fornecido.
   *
   * @param code o código do funcionário.
   * @return o nome do funcionário.
   * @throws Exception se ocorrer um erro na consulta.
   */
  public String getEmployeeNameByCode(String code) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar
    final String query =
        "SELECT name FROM user u "
            + "INNER JOIN employee e ON u.id_user = e.id_user "
            + "WHERE e.employee_code = ?";
    return DatabaseWrapper.executeQueryForSingleString(query, code);
  }

  /**
   * valida as credenciais de login de um funcionário.
   *
   * @param code o código do funcionário.
   * @param password a senha do funcionário.
   * @return true se as credenciais forem válidas, false caso contrário.
   * @throws Exception se ocorrer um erro na validação.
   */
  public boolean validateEmployeeLogin(String code, String password) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar
    final String query =
        "SELECT COUNT(*) FROM user u "
            + "INNER JOIN employee e ON u.id_user = e.id_user "
            + "WHERE e.employee_code = ? AND u.password = ?";
    var count = DatabaseWrapper.executeQueryForSingleInt(query, code, password);
    return count > 0;
  }

  /**
   * autentica um funcionário com base na senha fornecida.
   *
   * @param password a senha do funcionário.
   * @return true se a autenticação for bem-sucedida, false caso contrário.
   * @throws Exception se ocorrer um erro durante a autenticação.
   */
  public boolean authenticate(String password) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar
    final String query = "SELECT COUNT(*) FROM user WHERE password = ? AND user_type = 'EMPLOYEE'";
    int count = DatabaseWrapper.executeQueryForSingleInt(query, password);
    return count > 0;
  }

  /**
   * busca os detalhes de um funcionário com base no código do funcionário.
   *
   * @param employeeCode o código do funcionário.
   * @return um mapa contendo os detalhes do funcionário.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public Map<String, String> getEmployeeDetails(String employeeCode) throws Exception {
    return EmployeeDAO.getEmployeeDetailsByCode(employeeCode);
  }

  /**
   * retorna todas as transações financeiras registradas no sistema.
   *
   * @return uma lista de mapas onde cada mapa representa uma transação financeira.
   * @throws Exception se ocorrer um erro durante a consulta.
   */
  public List<Map<String, String>> getAllFinancialTransactions() throws Exception {
    return EmployeeDAO.getFinancialTransactions();
  }

  /**
   * verifica se um cpf já está registrado no sistema.
   *
   * @param cpf o cpf a ser verificado.
   * @return true se o cpf já existir, false caso contrário.
   * @throws Exception se ocorrer um erro na consulta.
   */
  private boolean doesCpfExist(String cpf) throws Exception {
    var count = DatabaseWrapper.executeQueryForSingleInt(SQLQueries.CHECK_CPF_EXISTS, cpf);
    return count > 0;
  }

  /**
   * valida os dados de um funcionário antes da inserção no banco.
   *
   * @param employee o objeto {@code Employee} contendo os dados do funcionário.
   * @throws IllegalArgumentException se os dados forem incompletos.
   */
  private void validateEmployee(Employee employee) {
    if (employee.getName() == null || employee.getCpf() == null || employee.getPassword() == null) {
      throw new IllegalArgumentException("As informações do funcionário estão incompletas.");
    }

    var address = employee.getAddress();

    if (address == null || address.getZipCode() == null || address.getCity() == null) {
      throw new IllegalArgumentException("As informações do endereço estão incompletas.");
    }

    if (employee.getEmployeeCode() == null || employee.getRole() == null) {
      throw new IllegalArgumentException(
          "As informações do cargo do funcionário estão incompletas.");
    }
  }

  /**
   * insere um novo usuário no banco de dados.
   *
   * @param employee o objeto {@code Employee} contendo os dados do funcionário.
   * @return o id do usuário inserido.
   * @throws Exception se ocorrer um erro durante a inserção.
   */
  private int insertUser(Employee employee) throws Exception {
    var userId =
        DatabaseWrapper.executeQuery(
            SQLQueries.INSERT_USER,
            employee.getName(),
            employee.getCpf(),
            employee.getBirthDate().toString(),
            employee.getPhone(),
            employee.getPassword(),
            "EMPLOYEE");

    if (userId == 0) {
      throw new IllegalArgumentException("Erro ao inserir usuário.");
    }

    logger.log(Level.INFO, "Usuário inserido com sucesso com ID: {0}", userId);
    return userId;
  }

  /**
   * insere o endereço de um usuário no banco de dados.
   *
   * @param userId o id do usuário.
   * @param address o objeto {@code Address} contendo os dados do endereço.
   * @throws Exception se ocorrer um erro durante a inserção.
   */
  private void insertAddress(int userId, Address address) throws Exception {
    DatabaseWrapper.executeQuery(
        SQLQueries.INSERT_ADDRESS,
        userId,
        address.getZipCode(),
        address.getStreet(),
        address.getHouseNumber(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState());
  }

  /**
   * insere os dados de um funcionário no banco de dados.
   *
   * @param userId o id do usuário associado ao funcionário.
   * @param employee o objeto {@code Employee} contendo os dados do funcionário.
   * @throws Exception se ocorrer um erro durante a inserção.
   */
  private void insertEmployee(int userId, Employee employee) throws Exception {
    DatabaseWrapper.executeQuery(
        SQLQueries.INSERT_EMPLOYEE, employee.getEmployeeCode(), employee.getRole(), userId);
  }
}
