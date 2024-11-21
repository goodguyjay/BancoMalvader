package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.controllers.EmployeeController;
import org.bancomaldaver.utils.FontHelper;
import org.bancomaldaver.utils.NavigationManager;

public class EmployeeLoginPage extends QWidget {
  private final QLineEdit employeeCodeField;
  private final QLineEdit employeeNameField;
  private final QLineEdit passwordField;

  public EmployeeLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Funcionário");

    // Central widget and main layout
    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Top layout with back button
    var topLayout = new QHBoxLayout();
    topLayout.setAlignment(Qt.AlignmentFlag.AlignLeft);
    var backButton = createBackButton(mainWindow);
    topLayout.addWidget(backButton);

    // Center layout with title and form fields
    var centerLayout = new QVBoxLayout();
    centerLayout.setAlignment(Qt.AlignmentFlag.AlignCenter);
    var titleLabel = createTitleLabel("Login Funcionário");
    centerLayout.addWidget(titleLabel);

    // Form layout for fields and buttons
    var formLayout = new QVBoxLayout();
    formLayout.setAlignment(Qt.AlignmentFlag.AlignCenter);

    employeeCodeField = createLineEdit("Digite o código do funcionário");
    formLayout.addWidget(createFormRow("Código do Funcionário:", employeeCodeField));

    employeeNameField = createLineEdit("Nome do Funcionário");
    employeeNameField.setReadOnly(true);
    formLayout.addWidget(createFormRow("Nome do Funcionário:", employeeNameField));

    passwordField = createPasswordField("Senha");
    formLayout.addWidget(createFormRow("Senha:", passwordField));

    var loginButton = createLoginButton(mainWindow);
    formLayout.addWidget(loginButton);

    var createEmployeeButton = createCreateEmployeeButton();
    formLayout.addWidget(createEmployeeButton);

    // Set fixed sizes for text fields only
    setFixedFieldSizes(employeeCodeField, employeeNameField, passwordField);

    // Add layouts to the main layout
    mainLayout.addLayout(topLayout);
    //    mainLayout.addStretch(); // Push content to the center
    mainLayout.addLayout(centerLayout);
    mainLayout.addLayout(formLayout);
    //    mainLayout.addStretch(); // Push content to the center

    centralWidget.setStyleSheet("background-color: #f5f5f5;" + "padding: 20px;");
    setLayout(mainLayout);

    // Event handlers
    setupEventHandlers(mainWindow, loginButton, createEmployeeButton);
  }

  private QPushButton createBackButton(QMainWindow mainWindow) {
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
    return backButton;
  }

  private QLabel createTitleLabel(String text) {
    var titleLabel = new QLabel(text);
    titleLabel.setFont(FontHelper.getBaseFont(28));
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    titleLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);");
    return titleLabel;
  }

  private QLineEdit createLineEdit(String placeholder) {
    var lineEdit = new QLineEdit();
    lineEdit.setFont(FontHelper.getBaseFont(16));
    lineEdit.setPlaceholderText(placeholder);
    lineEdit.setStyleSheet("padding: 8px;" + "border: 1px solid #ccc;" + "border-radius: 5px;");
    return lineEdit;
  }

  private QLineEdit createPasswordField(String placeholder) {
    var passwordField = createLineEdit(placeholder);
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    return passwordField;
  }

  private QPushButton createLoginButton(QMainWindow mainWindow) {
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
    return loginButton;
  }

  private QPushButton createCreateEmployeeButton() {
    var button = new QPushButton("Criar Conta de Funcionário");
    button.setFont(FontHelper.getBaseFont(16));
    button.setStyleSheet(
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
    return button;
  }

  private QWidget createFormRow(String labelText, QLineEdit field) {
    var row = new QWidget();
    var layout = new QHBoxLayout(row);

    var label = new QLabel(labelText);
    label.setFont(FontHelper.getBaseFont(16));
    layout.addWidget(label);

    layout.addWidget(field);
    layout.setAlignment(Qt.AlignmentFlag.AlignCenter);
    return row;
  }

  private void setFixedFieldSizes(QLineEdit... fields) {
    int fixedWidth = 683; // 50% of 1366px
    for (QLineEdit field : fields) {
      field.setFixedWidth(fixedWidth);
    }
  }

  private void setupEventHandlers(
      QMainWindow mainWindow, QPushButton loginButton, QPushButton createEmployeeButton) {
    employeeCodeField.textChanged.connect(this::onEmployeeCodeChanged);
    loginButton.clicked.connect(() -> onLoginClicked(mainWindow));
    createEmployeeButton.clicked.connect(this::showAdminPasswordDialog);
  }

  private void onEmployeeCodeChanged() {
    String code = employeeCodeField.text().trim();
    if (!code.isEmpty()) {
      try {
        var controller = new EmployeeController();
        String name = controller.getEmployeeNameByCode(code);
        employeeNameField.setText(name != null ? name : "Funcionário não encontrado");
      } catch (Exception e) {
        employeeNameField.setText("Erro ao buscar nome");
      }
    } else {
      employeeNameField.clear();
    }
  }

  private void onLoginClicked(QMainWindow mainWindow) {
    String code = employeeCodeField.text().trim();
    String password = passwordField.text();
    try {
      var controller = new EmployeeController();
      boolean isValid = controller.validateEmployeeLogin(code, password);

      if (isValid) {
        QMessageBox.information(this, "Login", "Login realizado com sucesso!");
        mainWindow.setCentralWidget(new EmployeeMainPage(mainWindow));
      } else {
        QMessageBox.warning(this, "Login", "Código ou senha incorretos.");
      }
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro durante o login: " + e.getMessage());
    }
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

    var loginButton = createLoginButton(null);
    dialogLayout.addWidget(loginButton);

    loginButton.clicked.connect(
        () -> {
          if ("admin".equals(passwordField.text())) {
            dialog.accept();
            NavigationManager.getInstance()
                .navigateTo(new CreateEmployeeAccountPage((QMainWindow) parent()));
          } else {
            QMessageBox.warning(this, "Erro", "Senha de administrador incorreta.");
          }
        });

    dialog.setLayout(dialogLayout);
    dialog.exec();
  }
}
