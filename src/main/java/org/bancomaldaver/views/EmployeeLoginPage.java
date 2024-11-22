package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.controllers.EmployeeController;
import org.bancomaldaver.utils.FontHelper;
import org.bancomaldaver.utils.NavigationManager;

/**
 * página de interface do usuário para o login de funcionários. permite que funcionários acessem o
 * sistema do banco utilizando código e senha. também oferece a funcionalidade de criar novas contas
 * de funcionários com autenticação de administrador.
 */
public final class EmployeeLoginPage extends QWidget {

  // Campos para entrada de código do funcionário, nome e senha
  private final QLineEdit employeeCodeField;
  private final QLineEdit employeeNameField;
  private final QLineEdit passwordField;

  /**
   * construtor da página de login de funcionário.
   *
   * @param mainWindow a janela principal da aplicação para navegação entre páginas.
   */
  public EmployeeLoginPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Login Funcionário");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Layout para botão "Voltar"
    var topLayout = new QHBoxLayout();
    topLayout.setAlignment(Qt.AlignmentFlag.AlignLeft);
    var backButton = createBackButton(mainWindow);
    topLayout.addWidget(backButton);

    // Layout central com o título
    var centerLayout = new QVBoxLayout();
    centerLayout.setAlignment(Qt.AlignmentFlag.AlignCenter);
    var titleLabel = createTitleLabel("Login Funcionário");
    centerLayout.addWidget(titleLabel);

    // Formulário de login
    var formLayout = new QVBoxLayout();
    formLayout.setAlignment(Qt.AlignmentFlag.AlignCenter);

    // Campos do formulário
    employeeCodeField = createLineEdit("Digite o código do funcionário");
    formLayout.addWidget(createFormRow("Código do Funcionário:", employeeCodeField));

    employeeNameField = createLineEdit("Nome do Funcionário");
    employeeNameField.setReadOnly(true);
    formLayout.addWidget(createFormRow("Nome do Funcionário:", employeeNameField));

    passwordField = createPasswordField();
    formLayout.addWidget(createFormRow("Senha:", passwordField));

    // Botões de login e criação de conta de funcionário
    var loginButton = createLoginButton(mainWindow);
    formLayout.addWidget(loginButton);

    var createEmployeeButton = createCreateEmployeeButton();
    formLayout.addWidget(createEmployeeButton);

    // Define tamanhos fixos para os campos
    setFixedFieldSizes(employeeCodeField, employeeNameField, passwordField);

    // Adiciona layouts ao layout principal
    mainLayout.addLayout(topLayout);
    mainLayout.addLayout(centerLayout);
    mainLayout.addLayout(formLayout);

    // Estilo visual do widget principal
    centralWidget.setStyleSheet("background-color: #f5f5f5;" + "padding: 20px;");
    setLayout(mainLayout);

    // Configura os eventos
    setupEventHandlers(mainWindow, loginButton, createEmployeeButton);
  }

  /**
   * cria o botão "Voltar" para navegar de volta ao menu principal.
   *
   * @param mainWindow a janela principal da aplicação.
   * @return o botão configurado.
   */
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

  /**
   * cria um rótulo de título para a página.
   *
   * @param text a janela principal da aplicação.
   * @return o botão configurado.
   */
  private QLabel createTitleLabel(String text) {
    var titleLabel = new QLabel(text);
    titleLabel.setFont(FontHelper.getBaseFont(28));
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    titleLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);");
    return titleLabel;
  }

  /**
   * cria um campo de texto para entrada de dados.
   *
   * @param placeholder o texto de placeholder para o campo.
   * @return o campo de texto configurado.
   */
  private QLineEdit createLineEdit(String placeholder) {
    var lineEdit = new QLineEdit();
    lineEdit.setFont(FontHelper.getBaseFont(16));
    lineEdit.setPlaceholderText(placeholder);
    lineEdit.setStyleSheet("padding: 8px;" + "border: 1px solid #ccc;" + "border-radius: 5px;");
    return lineEdit;
  }

  /**
   * cria um campo de senha para entrada de dados.
   *
   * @return o campo de senha configurado.
   */
  private QLineEdit createPasswordField() {
    var passwordField = createLineEdit("Senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    return passwordField;
  }

  /**
   * cria o botão de login para autenticação de funcionários.
   *
   * @param mainWindow a janela principal da aplicação.
   * @return o botão configurado.
   */
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

  /**
   * cria o botão para criar uma nova conta de funcionário.
   *
   * @return o botão configurado.
   */
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

  /**
   * cria uma linha de formulário com um rótulo e um campo de texto.
   *
   * @param labelText o texto do rótulo.
   * @param field o campo de texto.
   * @return o layout da linha de formulário.
   */
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

  /**
   * define o tamanho fixo para os campos de texto.
   *
   * @param fields os campos de texto.
   */
  private void setFixedFieldSizes(QLineEdit... fields) {
    int fixedWidth = 683;
    for (QLineEdit field : fields) {
      field.setFixedWidth(fixedWidth);
    }
  }

  /**
   * configura os eventos de interação da página, incluindo login e criação de conta de funcionário.
   *
   * @param mainWindow a janela principal da aplicação.
   * @param loginButton o botão de login.
   * @param createEmployeeButton o botão para criar conta de funcionário.
   */
  private void setupEventHandlers(
      QMainWindow mainWindow, QPushButton loginButton, QPushButton createEmployeeButton) {
    employeeCodeField.textChanged.connect(this::onEmployeeCodeChanged);
    loginButton.clicked.connect(() -> onLoginClicked(mainWindow));
    createEmployeeButton.clicked.connect(this::showAdminPasswordDialog);
  }

  /**
   * evento disparado quando o campo de código do funcionário é alterado. tenta buscar e preencher o
   * nome do funcionário associado ao código.
   */
  private void onEmployeeCodeChanged() {
    var code = employeeCodeField.text().trim();
    if (!code.isEmpty()) {
      try {
        var controller = new EmployeeController();
        var name = controller.getEmployeeNameByCode(code);
        employeeNameField.setText(name != null ? name : "Funcionário não encontrado");
      } catch (Exception e) {
        employeeNameField.setText("Erro ao buscar nome");
      }
    } else {
      employeeNameField.clear();
    }
  }

  /**
   * realiza a validação de login do funcionário.
   *
   * @param mainWindow a janela principal da aplicação.
   */
  private void onLoginClicked(QMainWindow mainWindow) {
    var code = employeeCodeField.text().trim();
    var password = passwordField.text();
    try {
      var controller = new EmployeeController();
      var isValid = controller.validateEmployeeLogin(code, password);

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

  /**
   * exibe um diálogo para autenticação de administrador antes de criar uma nova conta de
   * funcionário.
   */
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
