package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import java.util.logging.Logger;
import org.bancomaldaver.utils.FontHelper;

public class MainMenuPage extends QMainWindow {
  private static final Logger logger = Logger.getLogger(MainMenuPage.class.getName());

  public MainMenuPage() {
    setWindowTitle("Banco Malvader - Menu Principal");

    // Central widget setup
    var centralWidget = new QWidget();
    centralWidget.setStyleSheet(
        "background-color: #f5f5f5;" + "border-radius: 15px;" + "border: 1px solid #ccc;");

    var layout = new QVBoxLayout(centralWidget);

    layout.setContentsMargins(40, 40, 40, 30);
    layout.setSpacing(20);

    // Bank name label
    var bankNameLabel = new QLabel("Banco Malvader");
    bankNameLabel.setFont(FontHelper.getBaseFont(32));
    bankNameLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    bankNameLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);");
    layout.addWidget(bankNameLabel);

    // Menu options
    String[] menuOptions = {"Login", "Fazer Cadastro", "Sair"};

    for (var option : menuOptions) {
      QPushButton button = new QPushButton(option);
      button.setFont(FontHelper.getBaseFont(18));
      button.setFixedHeight(50);
      layout.addWidget(button);

      button.setStyleSheet(
          "QPushButton {"
              + "background-color: #4CAF50;"
              + "color: white;"
              + "border-radius: 8px;"
              + "padding: 10px;"
              + "border: none;"
              + "transition: background-color 0.3s;"
              + "font-weight: bold;"
              + "}"
              + "QPushButton:hover {"
              + "background-color: #45a049;"
              + "}"
              + "QPushButton:pressed {"
              + "background-color: #388E3C;"
              + "}");

      switch (option) {
        case "Sair" -> button.clicked.connect(this, "terminate()");
        case "Login" -> button.clicked.connect(this::showLoginDialog);
        case "Fazer Cadastro" -> button.clicked.connect(this::showRegisterDialog);
      }
    }

    centralWidget.setLayout(layout);
    setCentralWidget(centralWidget);
    resize(1366, 768);
  }

  // Show the login type dialog
  private void showLoginDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Tipo de Login");

    var dialogLayout = new QVBoxLayout(dialog);

    var label = new QLabel("Selecione o tipo de login:");
    label.setFont(FontHelper.getBaseFont(18));
    label.setAlignment(Qt.AlignmentFlag.AlignCenter);
    dialogLayout.addWidget(label);

    // User Login Button
    var userLoginButton = new QPushButton("Usuário");
    userLoginButton.setFont(FontHelper.getBaseFont(16));
    userLoginButton.setStyleSheet(
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
    dialogLayout.addWidget(userLoginButton);

    // Employee Login Button
    var employeeLoginButton = new QPushButton("Funcionário");
    employeeLoginButton.setFont(FontHelper.getBaseFont(16));
    employeeLoginButton.setStyleSheet(userLoginButton.styleSheet());
    dialogLayout.addWidget(employeeLoginButton);

    // Connect buttons to actions
    userLoginButton.clicked.connect(
        () -> {
          dialog.accept();
          var loginPage = new CustomerLoginPage(this);
          setCentralWidget(loginPage);
        });

    employeeLoginButton.clicked.connect(
        () -> {
          dialog.accept();
          showAdminPasswordDialog();
        });

    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  // Show the register account type dialog
  private void showRegisterDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Escolha o tipo de conta");

    var dialogLayout = new QVBoxLayout(dialog);

    var label = new QLabel("Selecione o tipo de conta para cadastro:");
    label.setFont(FontHelper.getBaseFont(18));
    label.setAlignment(Qt.AlignmentFlag.AlignCenter);
    dialogLayout.addWidget(label);

    var buttonCheckingAccount = new QPushButton("Conta Corrente");
    buttonCheckingAccount.setFont(FontHelper.getBaseFont(16));
    buttonCheckingAccount.setStyleSheet(
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
    dialogLayout.addWidget(buttonCheckingAccount);

    var buttonSavingsAccount = new QPushButton("Conta Poupança");
    buttonSavingsAccount.setFont(FontHelper.getBaseFont(16));
    buttonSavingsAccount.setStyleSheet(buttonCheckingAccount.styleSheet());
    dialogLayout.addWidget(buttonSavingsAccount);

    var buttonEmployeeAccount = new QPushButton("Conta Funcionário");
    buttonEmployeeAccount.setFont(FontHelper.getBaseFont(16));
    buttonEmployeeAccount.setStyleSheet(buttonCheckingAccount.styleSheet());
    dialogLayout.addWidget(buttonEmployeeAccount);

    buttonSavingsAccount.clicked.connect(
        () -> {
          dialog.accept();
          var registerSavingsAccountPage = new CreateSavingsAccountPage(this);
          setCentralWidget(registerSavingsAccountPage);
        });

    buttonCheckingAccount.clicked.connect(
        () -> {
          dialog.accept();
          var registerCheckingAccountPage = new CreateCheckingAccountPage(this);
          setCentralWidget(registerCheckingAccountPage);
        });

    buttonEmployeeAccount.clicked.connect(
        () -> {
          dialog.accept();
          var registerEmployeePage = new CreateEmployeePage(this);
          setCentralWidget(registerEmployeePage);
        });

    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void showAdminPasswordDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Autenticação de Administrador");

    var dialogLayout = new QVBoxLayout(dialog);

    var label = new QLabel("Digite a senha de administrador:");
    label.setFont(FontHelper.getBaseFont(18));
    label.setAlignment(Qt.AlignmentFlag.AlignCenter);
    dialogLayout.addWidget(label);

    var passwordField = new QLineEdit();
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setPlaceholderText("Senha de administrador");
    dialogLayout.addWidget(passwordField);

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
    dialogLayout.addWidget(loginButton);

    loginButton.clicked.connect(
        () -> {
          if (passwordField.text().equals("admin")) {
            dialog.accept();
            var employeeLoginPage = new EmployeeLoginPage(this);
            setCentralWidget(employeeLoginPage);
          } else {
            QMessageBox.warning(this, "Erro", "Senha de administrador incorreta.");
          }
        });

    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void terminate() {
    logger.info("fechando aplicação...");
    QApplication.quit();
  }
}
