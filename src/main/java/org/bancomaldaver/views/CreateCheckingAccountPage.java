package org.bancomaldaver.views;

import io.qt.gui.QDoubleValidator;
import io.qt.gui.QIntValidator;
import io.qt.widgets.*;
import io.qt.widgets.QMainWindow;
import java.time.LocalDate;
import org.bancomaldaver.controllers.AccountController;
import org.bancomaldaver.controllers.UserController;
import org.bancomaldaver.models.*;
import org.bancomaldaver.utils.FontHelper;

public class CreateCheckingAccountPage extends QWidget {
  private final QLineEdit usernameField;
  private final QLineEdit cpfField;
  private final QDateEdit birthDateField;
  private final QLineEdit phoneField;
  private final QLineEdit cepField;
  private final QLineEdit streetField;
  private final QLineEdit houseNumberField;
  private final QLineEdit neighborhoodField;
  private final QLineEdit cityField;
  private final QComboBox stateDropdown;
  private final QLineEdit passwordField;
  private final QLineEdit limitField;
  private final QDateEdit dueDateField;
  private final QComboBox agencyDropdown;

  public CreateCheckingAccountPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Criar Conta Corrente");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

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

    var formLayout = new QFormLayout();

    agencyDropdown = new QComboBox();
    agencyDropdown.addItem("1 - Agência DF", "DF");
    agencyDropdown.addItem("2 - Agência GO", "GO");
    agencyDropdown.addItem("3 - Agência SP", "SP");
    agencyDropdown.addItem("4 - Agência RN", "RN");
    agencyDropdown.setFont(FontHelper.getBaseFont(16));
    formLayout.addRow("Agência:", agencyDropdown);

    usernameField = new QLineEdit();
    usernameField.setFont(FontHelper.getBaseFont(16));
    usernameField.setPlaceholderText("Digite seu nome completo");
    formLayout.addRow("Nome Completo:", usernameField);

    cpfField = new QLineEdit();
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setPlaceholderText("Digite seu CPF");
    cpfField.setInputMask("000.000.000-00;_");
    formLayout.addRow("CPF:", cpfField);

    birthDateField = new QDateEdit();
    birthDateField.setFont(FontHelper.getBaseFont(16));
    birthDateField.setCalendarPopup(true);
    formLayout.addRow("Data de Nascimento:", birthDateField);

    phoneField = new QLineEdit();
    phoneField.setFont(FontHelper.getBaseFont(16));
    phoneField.setPlaceholderText("Digite seu telefone");
    formLayout.addRow("Telefone:", phoneField);

    cepField = new QLineEdit();
    cepField.setFont(FontHelper.getBaseFont(16));
    cepField.setPlaceholderText("xxxxx-xxx");
    cepField.setInputMask("00000-000;_");
    formLayout.addRow("CEP:", cepField);

    streetField = new QLineEdit();
    streetField.setFont(FontHelper.getBaseFont(16));
    streetField.setPlaceholderText("Logradouro (máximo 50 caracteres)");
    formLayout.addRow("Logradouro:", streetField);

    houseNumberField = new QLineEdit();
    houseNumberField.setFont(FontHelper.getBaseFont(16));
    houseNumberField.setPlaceholderText("Digite o número");
    houseNumberField.setValidator(new QIntValidator(0, 99999));
    formLayout.addRow("Número:", houseNumberField);

    neighborhoodField = new QLineEdit();
    neighborhoodField.setFont(FontHelper.getBaseFont(16));
    neighborhoodField.setPlaceholderText("Digite o bairro");
    formLayout.addRow("Bairro:", neighborhoodField);

    cityField = new QLineEdit();
    cityField.setFont(FontHelper.getBaseFont(16));
    cityField.setPlaceholderText("Digite a cidade");
    formLayout.addRow("Cidade:", cityField);

    stateDropdown = new QComboBox();
    for (BrazilianStates state : BrazilianStates.values()) {
      stateDropdown.addItem(
          state.getAbbreviation() + " - " + state.getFullName(), state.getAbbreviation());
    }
    stateDropdown.setFont(FontHelper.getBaseFont(16));
    formLayout.addRow("Estado:", stateDropdown);

    passwordField = new QLineEdit();
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    formLayout.addRow("Senha:", passwordField);

    limitField = new QLineEdit();
    limitField.setFont(FontHelper.getBaseFont(16));
    limitField.setPlaceholderText("Digite o limite da conta");
    limitField.setValidator(
        new QDoubleValidator(0, 1000000, 2)); // Accept numeric values with decimals
    formLayout.addRow("Limite da Conta:", limitField);

    dueDateField = new QDateEdit();
    dueDateField.setFont(FontHelper.getBaseFont(16));
    dueDateField.setCalendarPopup(true);
    formLayout.addRow("Data de Vencimento:", dueDateField);

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

  private void onRegisterClicked() {
    try {
      var customer = new Customer();
      customer.setName(usernameField.text());
      customer.setCpf(cpfField.text().replaceAll("\\D", ""));
      customer.setBirthDate(LocalDate.parse(birthDateField.date().toString("yyyy-MM-dd")));
      customer.setPhone(phoneField.text());
      customer.setPassword(passwordField.text());

      var selectedState = stateDropdown.currentData().toString();

      var address = new Address();
      address.setZipCode(cepField.text());
      address.setStreet(streetField.text());
      address.setHouseNumber(Integer.parseInt(houseNumberField.text()));
      address.setNeighborhood(neighborhoodField.text());
      address.setCity(cityField.text());
      address.setState(selectedState);
      customer.setAddress(address);

      var checkingAccount = new CheckingAccount();
      checkingAccount.setBranch(agencyDropdown.currentData().toString());
      checkingAccount.setLimit(Double.parseDouble(limitField.text()));
      checkingAccount.setDueDate(LocalDate.parse(dueDateField.date().toString("yyyy-MM-dd")));

      var userController = new UserController();
      var userId = userController.createUserWithAddress(customer);

      var accountController = new AccountController();
      accountController.createCheckingAccount(userId, checkingAccount);

      QMessageBox.information(this, "Sucesso", "Conta corrente criada com sucesso!");
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao criar conta corrente: " + e.getMessage());
    }
  }
}
