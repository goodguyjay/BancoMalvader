DROP DATABASE IF EXISTS banco_malvader;

CREATE DATABASE IF NOT EXISTS banco_malvader;

USE banco_malvader;

CREATE TABLE user (
                      id_user INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      cpf VARCHAR(11) NOT NULL UNIQUE,
                      birth_date DATE NOT NULL,
                      phone VARCHAR(15),
                      password VARCHAR(50) NOT NULL,
                      user_type ENUM('CUSTOMER', 'EMPLOYEE') NOT NULL
);

CREATE TABLE employee (
                          id_employee INT AUTO_INCREMENT PRIMARY KEY,
                          employee_code VARCHAR(20) NOT NULL UNIQUE,
                          role VARCHAR(50) NOT NULL,
                          id_user INT NOT NULL,
                          FOREIGN KEY (id_user) REFERENCES user(id_user)
);

CREATE TABLE customer (
                          id_customer INT AUTO_INCREMENT PRIMARY KEY,
                          id_user INT NOT NULL,
                          FOREIGN KEY (id_user) REFERENCES user(id_user)
);

CREATE TABLE address (
                         id_address INT AUTO_INCREMENT PRIMARY KEY,
                         zip_code VARCHAR(10) NOT NULL,
                         street VARCHAR(100),
                         house_number INT,
                         neighborhood VARCHAR(50),
                         city VARCHAR(50),
                         state VARCHAR(2),
                         id_user INT NOT NULL,
                         FOREIGN KEY (id_user) REFERENCES user(id_user)
);

CREATE TABLE account (
                         id_account INT AUTO_INCREMENT PRIMARY KEY,
                         account_number INT NOT NULL UNIQUE,
                         id_customer INT NOT NULL,
                         branch VARCHAR(10) NOT NULL,
                         account_type ENUM('SAVINGS', 'CHECKING') NOT NULL,
                         balance DECIMAL(15, 2) DEFAULT 0,
                         FOREIGN KEY (id_customer) REFERENCES customer(id_customer)
);

CREATE TABLE savings_account (
                                 id_savings_account INT AUTO_INCREMENT PRIMARY KEY,
                                 id_account INT NOT NULL,
                                 interest_rate DECIMAL(5, 2) NOT NULL,
                                 FOREIGN KEY (id_account) REFERENCES account(id_account)
);

CREATE TABLE checking_account (
                                  id_checking_account INT AUTO_INCREMENT PRIMARY KEY,
                                  id_account INT NOT NULL,
                                  credit_limit DECIMAL(15, 2) NOT NULL,
                                  due_date DATE NOT NULL,
                                  FOREIGN KEY (id_account) REFERENCES account(id_account)
);

CREATE TABLE transaction (
                             id_transaction INT AUTO_INCREMENT PRIMARY KEY,
                             transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
                             amount DECIMAL(15, 2) NOT NULL,
                             transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             id_account INT NOT NULL,
                             FOREIGN KEY (id_account) REFERENCES account(id_account)
);

CREATE TABLE report (
                        id_report INT AUTO_INCREMENT PRIMARY KEY,
                        report_type VARCHAR(50) NOT NULL,
                        generation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        content TEXT,
                        id_employee INT NOT NULL,
                        FOREIGN KEY (id_employee) REFERENCES employee(id_employee)
);
