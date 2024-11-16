package org.bancomaldaver.utils;

public final class SQLQueries {

  private SQLQueries() {
    throw new UnsupportedOperationException("Essa classe não pode ser instanciada diretamente.");
  }

  // User Queries
  public static final String INSERT_USER =
      "INSERT INTO user (name, cpf, birth_date, phone, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";
  public static final String SELECT_USER_BY_CPF = "SELECT * FROM user WHERE cpf = ?";
  public static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id_user = ?";

  // Employee Queries
  public static final String INSERT_EMPLOYEE =
      "INSERT INTO employee (employee_code, role, id_user) VALUES (?, ?, ?)";
  public static final String SELECT_EMPLOYEE_BY_CODE =
      "SELECT * FROM employee WHERE employee_code = ?";

  // Customer Queries
  public static final String INSERT_CUSTOMER = "INSERT INTO customer (id_user) VALUES (?)";
  public static final String SELECT_CUSTOMER_BY_USER_ID =
      "SELECT * FROM customer WHERE id_user = ?";

  // Address Queries
  public static final String INSERT_ADDRESS =
      "INSERT INTO address (id_user, zip_code, street, house_number, neighborhood, city, state) VALUES (?, ?, ?, ?, ?, ?, ?)";
  public static final String SELECT_ADDRESS_BY_USER_ID = "SELECT * FROM address WHERE id_user = ?";

  // Account Queries
  public static final String INSERT_ACCOUNT =
      "INSERT INTO account (id_customer, branch, account_type, balance, account_number) VALUES (?, ?, ?, ?, ?)";
  public static final String SELECT_ACCOUNT_BY_CUSTOMER_ID =
      "SELECT * FROM account WHERE id_customer = ?";
  public static final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM account WHERE id_account = ?";

  // Savings Account Queries
  public static final String INSERT_SAVINGS_ACCOUNT =
      "INSERT INTO savings_account (id_account, interest_rate) VALUES (?, ?)";
  public static final String SELECT_SAVINGS_ACCOUNT_BY_ID =
      "SELECT * FROM savings_account WHERE id_account = ?";

  // Checking Account Queries
  public static final String INSERT_CHECKING_ACCOUNT =
      "INSERT INTO checking_account (id_account, credit_limit, due_date) VALUES (?, ?, ?)";
  public static final String SELECT_CHECKING_ACCOUNT_BY_ID =
      "SELECT * FROM checking_account WHERE id_account = ?";

  // Transaction Queries
  public static final String INSERT_TRANSACTION =
      "INSERT INTO transaction (transaction_type, amount, transaction_date, id_account) VALUES (?, ?, ?, ?)";
  public static final String SELECT_TRANSACTIONS_BY_ACCOUNT_ID =
      "SELECT * FROM transaction WHERE id_account = ?";

  // Report Queries
  public static final String INSERT_REPORT =
      "INSERT INTO report (report_type, generation_date, content, id_employee) VALUES (?, ?, ?, ?)";
  public static final String SELECT_REPORTS_BY_EMPLOYEE_ID =
      "SELECT * FROM report WHERE id_employee = ?";
}
