package org.bancomaldaver.views;

import io.qt.widgets.*;
import org.bancomaldaver.utils.FontHelper;

public class EmployeeLoginPage extends QWidget {
  public EmployeeLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Funcionário");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Back button
    var topLayout = new QHBoxLayout();
    var backButton = new QPushButton("Voltar");
    backButton.setFont(FontHelper.getBaseFont(16));
    backButton.setStyleSheet(
        "QPushButton {"
            + "background-color: #cccccc;"
            + "color: black;"
            + "border-radius: 8px;"
            + "padding: 8px 12px;"
            + "}"
            + "QPushButton:hover {"
            + "background-color: #b3b3b3;"
            + "}");
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new MainMenuPage()));
    topLayout.addWidget(backButton);
    mainLayout.addLayout(topLayout);

    // Employee Code Field
    var formLayout = new QFormLayout();
    var employeeCodeField = new QLineEdit();
    employeeCodeField.setFont(FontHelper.getBaseFont(16));
    employeeCodeField.setPlaceholderText("Digite o código do funcionário");
    formLayout.addRow("Código do Funcionário:", employeeCodeField);

    // Readonly Employee Name Field
    var employeeNameField = new QLineEdit();
    employeeNameField.setFont(FontHelper.getBaseFont(16));
    employeeNameField.setReadOnly(true);
    employeeNameField.setPlaceholderText("Nome do Funcionário");
    formLayout.addRow("Nome do Funcionário:", employeeNameField);

    // Password Field
    var passwordField = new QLineEdit();
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setPlaceholderText("Senha");
    formLayout.addRow("Senha:", passwordField);

    // Login Button
    var loginButton = new QPushButton("Entrar");
    loginButton.setFont(FontHelper.getBaseFont(16));
    loginButton.setStyleSheet(
        "QPushButton {"
            + "background-color: #4CAF50;"
            + "color: white;"
            + "border-radius: 8px;"
            + "padding: 10px;"
            + "font-weight: bold;"
            + "}"
            + "QPushButton:hover {"
            + "background-color: #45a049;"
            + "}"
            + "QPushButton:pressed {"
            + "background-color: #388E3C;"
            + "}");
    formLayout.addRow(loginButton);

    // Add functionality for fetching employee name
    employeeCodeField.textChanged.connect(
        () -> {
          String code = employeeCodeField.text();
          // Logic to fetch the employee's name goes here
          employeeNameField.setText("Exemplo Nome"); // Placeholder logic
        });

    loginButton.clicked.connect(
        () -> {
          // Placeholder for actual login logic
          QMessageBox.information(this, "Login", "Login realizado com sucesso!");
        });

    mainLayout.addLayout(formLayout);
    setLayout(mainLayout);
  }
}
