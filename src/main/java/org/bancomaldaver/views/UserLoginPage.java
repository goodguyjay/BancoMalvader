package org.bancomaldaver.views;

import io.qt.widgets.*;
import org.bancomaldaver.utils.FontHelper;

public class UserLoginPage extends QWidget {
  public UserLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Usuário");

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

    // Form layout
    var formLayout = new QFormLayout();

    // CPF Field with Mask
    var cpfField = new QLineEdit();
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setPlaceholderText("Digite seu CPF");
    cpfField.setInputMask("000.000.000-00;_"); // CPF mask
    formLayout.addRow("CPF:", cpfField);

    // Password Field
    var passwordField = new QLineEdit();
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setPlaceholderText("Digite sua senha");
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

    // Connect login button to authentication logic
    loginButton.clicked.connect(
        () -> {
          String cpf = cpfField.text();
          String password = passwordField.text();
          authenticateUser(cpf, password);
        });

    mainLayout.addLayout(formLayout);
    setLayout(mainLayout);
  }

  // Authentication logic
  private void authenticateUser(String cpf, String password) {
    try {
      // Call controller or service to validate user credentials
      boolean isAuthenticated = validateUserCredentials(cpf, password);
      if (isAuthenticated) {
        QMessageBox.information(this, "Sucesso", "Login realizado com sucesso!");
      } else {
        QMessageBox.warning(this, "Erro", "CPF ou senha inválidos.");
      }
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao realizar login: " + e.getMessage());
    }
  }

  // Placeholder for user validation logic (replace with actual implementation)
  private boolean validateUserCredentials(String cpf, String password) {
    // TODO: Replace this with actual database query
    return "123.456.789-00".equals(cpf) && "password".equals(password);
  }
}
