package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.controllers.UserController;
import org.bancomaldaver.utils.FontHelper;

public class CustomerLoginPage extends QWidget {
  private final QLineEdit cpfField;
  private final QLineEdit passwordField;
  private final QComboBox branchDropdown;

  public CustomerLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Cliente");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    var topLayout = new QHBoxLayout();
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
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new MainMenuPage()));
    topLayout.addWidget(backButton);

    var centerLayout = new QVBoxLayout();

    var titleLabel = new QLabel("Login Cliente");
    titleLabel.setFont(FontHelper.getBaseFont(28));
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    titleLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);");
    centerLayout.addWidget(titleLabel);

    // CPF Field
    cpfField = new QLineEdit();
    cpfField.setPlaceholderText("Digite seu CPF");
    cpfField.setInputMask("000.000.000-00;_"); // CPF mask
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setStyleSheet(
        "padding: 8px;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;"
            + "background-color: #ffffff;"
            + "color: #333;"
            + "font-size: 16px;"
            + "placeholder-text-color: #999;");
    centerLayout.addWidget(cpfField);

    // Branch Dropdown
    branchDropdown = new QComboBox();
    branchDropdown.addItem("1 - Agência DF", "DF");
    branchDropdown.addItem("2 - Agência GO", "GO");
    branchDropdown.addItem("3 - Agência SP", "SP");
    branchDropdown.addItem("4 - Agência RN", "RN");
    branchDropdown.setFont(FontHelper.getBaseFont(16));
    centerLayout.addWidget(branchDropdown);

    // Password Field
    passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Senha");
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setStyleSheet(
        "padding: 8px;"
            + "placeholder-text-color: #999;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;"
            + "background-color: #ffffff;");
    centerLayout.addWidget(passwordField);

    var loginButton = new QPushButton("Entrar");
    loginButton.setFont(FontHelper.getBaseFont(16));
    loginButton.setStyleSheet(
        "QPushButton {"
            + "background-color: #4CAF50;"
            + "placeholder-text-color: #999;"
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
    centerLayout.addWidget(loginButton);

    var forgotPasswordButton = new QPushButton("Esqueci minha senha");
    forgotPasswordButton.setFont(FontHelper.getBaseFont(16));
    forgotPasswordButton.setStyleSheet(
        "QPushButton {"
            + "color: #4CAF50;"
            + "background: none;"
            + "border: none;"
            + "text-decoration: underline;"
            + "}"
            + "QPushButton:hover {"
            + "color: #388E3C;"
            + "}");
    centerLayout.addWidget(forgotPasswordButton);

    loginButton.clicked.connect(this::onLoginClicked);

    mainLayout.addLayout(topLayout);
    mainLayout.addLayout(centerLayout);

    centralWidget.setStyleSheet(
        "background-color: #f5f5f5;" + "border-radius: 10px;" + "padding: 20px;");
    setLayout(mainLayout);
  }

  private void onLoginClicked() {
    try {
      var cpf = cpfField.text().replaceAll("\\D", ""); // Remove mask characters
      var password = passwordField.text();
      var branch = branchDropdown.currentData().toString();

      UserController controller = new UserController();
      boolean isValid = controller.validateCustomerLogin(cpf, password, branch);

      if (isValid) {
        QMessageBox.information(this, "Sucesso", "Login efetuado com sucesso!");
      } else {
        QMessageBox.warning(this, "Erro", "CPF, senha ou agência incorretos.");
      }
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro durante o login: " + e.getMessage());
    }
  }
}
