DROP DATABASE IF EXISTS banco_malvader;

CREATE DATABASE IF NOT EXISTS banco_malvader;

USE banco_malvader;

CREATE TABLE user
(
    id_user    INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)                  NOT NULL,
    cpf        VARCHAR(11)                   NOT NULL UNIQUE,
    birth_date DATE                          NOT NULL,
    phone      VARCHAR(15),
    password   VARCHAR(50)                   NOT NULL,
    user_type  ENUM ('CUSTOMER', 'EMPLOYEE') NOT NULL
);

CREATE TABLE employee
(
    id_employee   INT AUTO_INCREMENT PRIMARY KEY,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    role          VARCHAR(50) NOT NULL,
    id_user       INT         NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user (id_user) ON DELETE CASCADE
);

CREATE TABLE customer
(
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    id_user     INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user (id_user) ON DELETE CASCADE
);

CREATE TABLE address
(
    id_address   INT AUTO_INCREMENT PRIMARY KEY,
    zip_code     VARCHAR(10) NOT NULL,
    street       VARCHAR(100),
    house_number INT,
    neighborhood VARCHAR(50),
    city         VARCHAR(50),
    state        VARCHAR(2),
    id_user      INT         NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user (id_user) ON DELETE CASCADE
);

CREATE TABLE account
(
    id_account     INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT                          NOT NULL UNIQUE,
    id_customer    INT                          NOT NULL,
    branch         VARCHAR(10)                  NOT NULL,
    account_type   ENUM ('SAVINGS', 'CHECKING') NOT NULL,
    balance        DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (id_customer) REFERENCES customer (id_customer) ON DELETE CASCADE
);

CREATE TABLE savings_account
(
    id_savings_account INT AUTO_INCREMENT PRIMARY KEY,
    id_account         INT           NOT NULL,
    interest_rate      DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (id_account) REFERENCES account (id_account) ON DELETE CASCADE
);

CREATE TABLE checking_account
(
    id_checking_account INT AUTO_INCREMENT PRIMARY KEY,
    id_account          INT            NOT NULL,
    credit_limit        DECIMAL(15, 2) NOT NULL,
    due_date            DATE           NOT NULL,
    FOREIGN KEY (id_account) REFERENCES account (id_account) ON DELETE CASCADE
);

CREATE TABLE transaction
(
    id_transaction   INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
    amount           DECIMAL(15, 2)                             NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_account       INT                                        NOT NULL,
    FOREIGN KEY (id_account) REFERENCES account (id_account) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE report
(
    id_report       INT AUTO_INCREMENT PRIMARY KEY,
    report_type     VARCHAR(50) NOT NULL,
    generation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content         TEXT,
    id_employee     INT         NOT NULL,
    FOREIGN KEY (id_employee) REFERENCES employee (id_employee) ON DELETE CASCADE
);

-- dados aleatorios

INSERT INTO user (name, cpf, birth_date, phone, password, user_type)
VALUES ('John Doe', '11111111111', '1985-05-20', '1234567890', 'password123', 'EMPLOYEE'),
       ('Jane Smith', '22222222222', '1990-07-15', '9876543210', 'securepass', 'EMPLOYEE');

INSERT INTO employee (employee_code, role, id_user)
VALUES ('E001', 'Manager', 1),
       ('E002', 'Analyst', 2);

INSERT INTO user (name, cpf, birth_date, phone, password, user_type)
VALUES ('Alice Johnson', '33333333333', '1995-03-12', '555667788', 'alicepass', 'CUSTOMER'),
       ('Bob Brown', '44444444444', '1988-11-25', '444555666', 'bobpass', 'CUSTOMER');

INSERT INTO customer (id_user)
VALUES (3),
       (4);

INSERT INTO address (zip_code, street, house_number, neighborhood, city, state, id_user)
VALUES ('12345-678', 'Main Street', 101, 'Downtown', 'Springfield', 'SP', 3),
       ('98765-432', 'Elm Avenue', 202, 'Suburbia', 'Riverdale', 'RJ', 4);


-- Insert checking accounts
INSERT INTO account (account_number, id_customer, branch, account_type, balance)
VALUES (10001, 1, 'DF', 'CHECKING', 500.00),
       (10002, 2, 'SP', 'CHECKING', 300.00);

-- Insert savings accounts
INSERT INTO account (account_number, id_customer, branch, account_type, balance)
VALUES (10003, 1, 'DF', 'SAVINGS', 1000.00),
       (10004, 2, 'SP', 'SAVINGS', 2000.00);

INSERT INTO transaction (transaction_type, amount, transaction_date, id_account)
VALUES ('DEPOSIT', 200.00, '2024-11-01', 1),
       ('WITHDRAWAL', 100.00, '2024-11-05', 1),
       ('TRANSFER', 150.00, '2024-11-10', 1),
       ('DEPOSIT', 300.00, '2024-11-02', 2),
       ('WITHDRAWAL', 50.00, '2024-11-06', 2),
       ('DEPOSIT', 500.00, '2024-11-03', 3),
       ('TRANSFER', 200.00, '2024-11-08', 3),
       ('WITHDRAWAL', 400.00, '2024-11-09', 4);


