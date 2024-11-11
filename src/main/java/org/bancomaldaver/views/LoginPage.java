package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.utils.FontHelper;

public class LoginPage extends QWidget {

  public LoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login");

    var layout = new QVBoxLayout(this);

    var titleLabel = new QLabel("Login");
    titleLabel.setFont(FontHelper.getBaseFont(24));
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    titleLabel.setStyleSheet("font-weight: bold;");
    layout.addWidget(titleLabel);

    var usernameField = new QLineEdit();
    usernameField.setPlaceholderText("Usuário");
    usernameField.setFont(FontHelper.getBaseFont(16));
    layout.addWidget(usernameField);

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Senha");
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    layout.addWidget(passwordField);

    var loginButton = new QPushButton("Entrar");
    loginButton.setFont(FontHelper.getBaseFont(16));
    layout.addWidget(loginButton);

    var forgotPasswordButton = new QPushButton("Esqueci minha senha");
    forgotPasswordButton.setFont(FontHelper.getBaseFont(16));
    layout.addWidget(forgotPasswordButton);

    var backButton = new QPushButton("Voltar");
    backButton.setFont(FontHelper.getBaseFont(16));
    layout.addWidget(backButton);

    loginButton.clicked.connect(() -> onLoginClicked(usernameField.text(), passwordField.text()));

    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new MainMenu()));

    setLayout(layout);
  }

  private void onLoginClicked(String username, String password) {
    if (username.equals("admin") && password.equals("admin")) {
      QMessageBox.information(this, "Sucesso", "Login efetuado com sucesso!");
    } else {
      QMessageBox.warning(this, "Login falhou", "Usuário ou senha inválidos.");
    }
  }
}
