package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.controllers.UserController;
import org.bancomaldaver.utils.FontHelper;
import org.bancomaldaver.utils.NavigationManager;

public class CustomerLoginPage extends QWidget {
  private final QLineEdit cpfField;
  private final QLineEdit passwordField;
  private final QComboBox branchDropdown;

  public CustomerLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Cliente");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);
    var topLayout = new QHBoxLayout();
    var centerLayout = new QVBoxLayout();
    topLayout.setAlignment(Qt.AlignmentFlag.AlignLeft);

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

    var titleLabel = new QLabel("Login Cliente");
    titleLabel.setFont(FontHelper.getBaseFont(28));
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    titleLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);");

    cpfField = new QLineEdit();
    cpfField.setInputMask("000.000.000-00;_");
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setStyleSheet(
        "padding: 8px;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;"
            + "placeholder-text-color: #ffffff;");

    branchDropdown = new QComboBox();
    branchDropdown.addItem("1 - Agência DF", "DF");
    branchDropdown.addItem("2 - Agência GO", "GO");
    branchDropdown.addItem("3 - Agência SP", "SP");
    branchDropdown.addItem("4 - Agência RN", "RN");
    branchDropdown.setFont(FontHelper.getBaseFont(16));

    passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Senha");
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setStyleSheet(
        "padding: 8px;"
            + "placeholder-text-color: #ffffff;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;");

    var loginButton = new QPushButton("Entrar");
    loginButton.setFont(FontHelper.getBaseFont(16));
    loginButton.setStyleSheet(
        "QPushButton {"
            + "background-color: #4CAF50;"
            + "placeholder-text-color: #999;"
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

    loginButton.clicked.connect(() -> onLoginClicked(mainWindow));
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new MainMenuPage()));

    topLayout.addWidget(backButton);

    centerLayout.addWidget(titleLabel);
    centerLayout.addWidget(branchDropdown);
    centerLayout.addWidget(cpfField);
    centerLayout.addWidget(passwordField);
    centerLayout.addWidget(loginButton);

    mainLayout.addLayout(topLayout);
    mainLayout.addLayout(centerLayout);

    centralWidget.setStyleSheet(
        "background-color: #f5f5f5;" + "border-radius: 10px;" + "padding: 20px;");
    setLayout(mainLayout);
  }

  private void onLoginClicked(QMainWindow mainWindow) {
    try {
      var cpf = cpfField.text().replaceAll("\\D", "");
      var password = passwordField.text();
      var branch = branchDropdown.currentData().toString();

      var controller = new UserController();
      var accountId = controller.validateCustomerLogin(cpf, password, branch);

      if (accountId > 0) {
        QMessageBox.information(this, "Sucesso", "Login efetuado com sucesso!");
        NavigationManager.getInstance().navigateTo(new CustomerMainPage(mainWindow, accountId));
      } else {
        QMessageBox.warning(this, "Erro", "CPF, senha ou agência incorretos.");
      }
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro durante o login: " + e.getMessage());
    }
  }
}
