package org.bancomaldaver.views;

import io.qt.gui.QIntValidator;
import io.qt.widgets.*;
import java.time.LocalDate;
import org.bancomaldaver.controllers.EmployeeController;
import org.bancomaldaver.models.Address;
import org.bancomaldaver.models.Employee;
import org.bancomaldaver.utils.FontHelper;

/**
 * página de interface do usuário para criar contas de funcionários. permite inserir dados pessoais,
 * endereço e cargo do funcionário para registro no sistema.
 */
public final class CreateEmployeeAccountPage extends QWidget {

  // Campos de entrada para os dados do funcionário
  private final QLineEdit nameField;
  private final QLineEdit cpfField;
  private final QDateEdit birthDateField;
  private final QLineEdit phoneField;
  private final QLineEdit zipCodeField;
  private final QLineEdit streetField;
  private final QLineEdit houseNumberField;
  private final QLineEdit neighborhoodField;
  private final QLineEdit cityField;
  private final QLineEdit stateField;
  private final QLineEdit employeeCodeField;
  private final QLineEdit roleField;
  private final QLineEdit passwordField;

  /**
   * construtor da página para criar uma nova conta de funcionário.
   *
   * @param mainWindow a janela principal da aplicação para navegação entre páginas.
   */
  public CreateEmployeeAccountPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Criar Funcionário");

    // Configuração do layout principal
    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Botão "Voltar" no topo
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

    // Formulário de entrada de dados
    var formLayout = new QFormLayout();

    // Configuração dos campos do formulário
    nameField = new QLineEdit();
    nameField.setFont(FontHelper.getBaseFont(16));
    nameField.setPlaceholderText("Digite o nome completo");
    formLayout.addRow("Nome Completo:", nameField);

    cpfField = new QLineEdit();
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setPlaceholderText("Digite o CPF");
    cpfField.setInputMask("000.000.000-00;_");
    formLayout.addRow("CPF:", cpfField);

    birthDateField = new QDateEdit();
    birthDateField.setFont(FontHelper.getBaseFont(16));
    birthDateField.setCalendarPopup(true);
    formLayout.addRow("Data de Nascimento:", birthDateField);

    phoneField = new QLineEdit();
    phoneField.setFont(FontHelper.getBaseFont(16));
    phoneField.setPlaceholderText("Digite o telefone");
    formLayout.addRow("Telefone:", phoneField);

    zipCodeField = new QLineEdit();
    zipCodeField.setFont(FontHelper.getBaseFont(16));
    zipCodeField.setPlaceholderText("xxxxx-xxx");
    zipCodeField.setInputMask("00000-000;_");
    formLayout.addRow("CEP:", zipCodeField);

    streetField = new QLineEdit();
    streetField.setFont(FontHelper.getBaseFont(16));
    streetField.setPlaceholderText("Digite o logradouro");
    formLayout.addRow("Logradouro:", streetField);

    houseNumberField = new QLineEdit();
    houseNumberField.setFont(FontHelper.getBaseFont(16));
    houseNumberField.setPlaceholderText("Digite o número da casa");
    houseNumberField.setValidator(new QIntValidator(1, 99999));
    formLayout.addRow("Número:", houseNumberField);

    neighborhoodField = new QLineEdit();
    neighborhoodField.setFont(FontHelper.getBaseFont(16));
    neighborhoodField.setPlaceholderText("Digite o bairro");
    formLayout.addRow("Bairro:", neighborhoodField);

    cityField = new QLineEdit();
    cityField.setFont(FontHelper.getBaseFont(16));
    cityField.setPlaceholderText("Digite a cidade");
    formLayout.addRow("Cidade:", cityField);

    stateField = new QLineEdit();
    stateField.setFont(FontHelper.getBaseFont(16));
    stateField.setPlaceholderText("Digite o estado");
    formLayout.addRow("Estado:", stateField);

    employeeCodeField = new QLineEdit();
    employeeCodeField.setFont(FontHelper.getBaseFont(16));
    employeeCodeField.setPlaceholderText("Digite o código do funcionário");
    formLayout.addRow("Código do Funcionário:", employeeCodeField);

    roleField = new QLineEdit();
    roleField.setFont(FontHelper.getBaseFont(16));
    roleField.setPlaceholderText("Digite o cargo do funcionário");
    formLayout.addRow("Cargo:", roleField);

    passwordField = new QLineEdit();
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setPlaceholderText("Digite a senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    formLayout.addRow("Senha:", passwordField);

    // Botão "Cadastrar"
    var registerButton = new QPushButton("Cadastrar");
    registerButton.setFont(FontHelper.getBaseFont(16));
    registerButton.setStyleSheet(
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
    registerButton.clicked.connect(this::onRegisterClicked);
    formLayout.addRow(registerButton);

    mainLayout.addLayout(formLayout);
    centralWidget.setLayout(mainLayout);
    setLayout(mainLayout);
  }

  /**
   * ação executada ao clicar no botão "Cadastrar". coleta os dados do formulário, cria os objetos
   * necessários e realiza o registro no sistema.
   */
  private void onRegisterClicked() {
    try {
      var employee = new Employee();
      employee.setName(nameField.text());
      employee.setCpf(cpfField.text().replaceAll("\\D", ""));
      employee.setBirthDate(LocalDate.parse(birthDateField.date().toString("yyyy-MM-dd")));
      employee.setPhone(phoneField.text());
      employee.setPassword(passwordField.text());
      employee.setEmployeeCode(employeeCodeField.text());
      employee.setRole(roleField.text());

      var address = new Address();
      address.setZipCode(zipCodeField.text());
      address.setStreet(streetField.text());
      address.setHouseNumber(Integer.parseInt(houseNumberField.text()));
      address.setNeighborhood(neighborhoodField.text());
      address.setCity(cityField.text());
      address.setState(stateField.text());
      employee.setAddress(address);

      var controller = new EmployeeController();
      controller.createEmployee(employee);

      QMessageBox.information(this, "Sucesso", "Funcionário criado com sucesso!");
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao criar funcionário: " + e.getMessage());
    }
  }
}
