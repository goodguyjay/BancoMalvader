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

public class EmployeeController {
  private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());

  public void createEmployee(Employee employee) throws Exception {
    if (doesCpfExist(employee.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado.");
    }

    validateEmployee(employee);

    var userId = insertUser(employee);

    insertAddress(userId, employee.getAddress());

    insertEmployee(userId, employee);
  }

  public String getEmployeeNameByCode(String code) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar
    final String query =
        "SELECT name FROM user u "
            + "INNER JOIN employee e ON u.id_user = e.id_user "
            + "WHERE e.employee_code = ?";
    return DatabaseWrapper.executeQueryForSingleString(query, code);
  }

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

  public boolean authenticate(String password) throws Exception {
    // Não era pra ter uma query aqui... mas por questões de tempo, não tenho tempo de refatorar e
    // testar
    final String query = "SELECT COUNT(*) FROM user WHERE password = ? AND user_type = 'EMPLOYEE'";
    int count = DatabaseWrapper.executeQueryForSingleInt(query, password);
    return count > 0;
  }

  public Map<String, String> getEmployeeDetails(String employeeCode) throws Exception {
    return EmployeeDAO.getEmployeeDetailsByCode(employeeCode);
  }

  public List<Map<String, String>> getAllFinancialTransactions() throws Exception {
    return EmployeeDAO.getFinancialTransactions();
  }

  private boolean doesCpfExist(String cpf) throws Exception {
    var count = DatabaseWrapper.executeQueryForSingleInt(SQLQueries.CHECK_CPF_EXISTS, cpf);
    return count > 0;
  }

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

  private void insertEmployee(int userId, Employee employee) throws Exception {
    DatabaseWrapper.executeQuery(
        SQLQueries.INSERT_EMPLOYEE, employee.getEmployeeCode(), employee.getRole(), userId);
  }
}
