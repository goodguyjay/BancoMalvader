package org.bancomaldaver.views;

import io.qt.gui.QIntValidator;
import io.qt.widgets.*;
import java.time.LocalDate;
import org.bancomaldaver.controllers.UserController;
import org.bancomaldaver.models.Address;
import org.bancomaldaver.models.BrazilianStates;
import org.bancomaldaver.models.Customer;
import org.bancomaldaver.models.SavingsAccount;
import org.bancomaldaver.utils.FontHelper;

public class CreateSavingsAccountPage extends QWidget {
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
  private final QComboBox agencyDropdown;

  public CreateSavingsAccountPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Criar Conta Poupança");

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

    // Agency Dropdown
    agencyDropdown = new QComboBox();
    agencyDropdown.addItem("1 - Agência DF", "DF");
    agencyDropdown.addItem("2 - Agência GO", "GO");
    agencyDropdown.addItem("3 - Agência SP", "SP");
    agencyDropdown.addItem("4 - Agência RN", "RN");
    agencyDropdown.setFont(FontHelper.getBaseFont(16));
    formLayout.addRow("Agência:", agencyDropdown);

    // Full Name
    usernameField = new QLineEdit();
    usernameField.setFont(FontHelper.getBaseFont(16));
    usernameField.setPlaceholderText("Digite seu nome completo");
    formLayout.addRow("Nome Completo:", usernameField);

    // CPF with Mask
    cpfField = new QLineEdit();
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setPlaceholderText("Digite seu CPF");
    cpfField.setInputMask("000.000.000-00;_");
    formLayout.addRow("CPF:", cpfField);

    // Birth Date
    birthDateField = new QDateEdit();
    birthDateField.setFont(FontHelper.getBaseFont(16));
    birthDateField.setCalendarPopup(true);
    formLayout.addRow("Data de Nascimento:", birthDateField);

    phoneField = new QLineEdit();
    phoneField.setFont(FontHelper.getBaseFont(16));
    phoneField.setPlaceholderText("Digite seu telefone");
    formLayout.addRow("Telefone:", phoneField);

    // CEP with Mask
    cepField = new QLineEdit();
    cepField.setFont(FontHelper.getBaseFont(16));
    cepField.setPlaceholderText("xxxxx-xxx");
    cepField.setInputMask("00000-000;_");
    formLayout.addRow("CEP:", cepField);

    // Street with Character Limit Note
    streetField = new QLineEdit();
    streetField.setFont(FontHelper.getBaseFont(16));
    streetField.setPlaceholderText("Logradouro (máximo 50 caracteres)");
    formLayout.addRow("Logradouro:", streetField);

    // House Number
    houseNumberField = new QLineEdit();
    houseNumberField.setFont(FontHelper.getBaseFont(16));
    houseNumberField.setPlaceholderText("Digite o número");
    houseNumberField.setValidator(new QIntValidator(0, 99999));
    formLayout.addRow("Número:", houseNumberField);

    // Neighborhood
    neighborhoodField = new QLineEdit();
    neighborhoodField.setFont(FontHelper.getBaseFont(16));
    neighborhoodField.setPlaceholderText("Digite o bairro");
    formLayout.addRow("Bairro:", neighborhoodField);

    // City
    cityField = new QLineEdit();
    cityField.setFont(FontHelper.getBaseFont(16));
    cityField.setPlaceholderText("Digite a cidade");
    formLayout.addRow("Cidade:", cityField);

    // State
    stateDropdown = new QComboBox();
    for (BrazilianStates state : BrazilianStates.values()) {
      stateDropdown.addItem(
          state.getAbbreviation() + " - " + state.getFullName(), state.getAbbreviation());
    }
    stateDropdown.setFont(FontHelper.getBaseFont(16));
    formLayout.addRow("Estado:", stateDropdown);

    // Password
    passwordField = new QLineEdit();
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    formLayout.addRow("Senha:", passwordField);

    // Register Button
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

    // Assemble layouts
    mainLayout.addLayout(formLayout);
    centralWidget.setLayout(mainLayout);
    setLayout(mainLayout);
  }

  private void onRegisterClicked() {
    try {
      // Collect customer data
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

      // Collect account data
      var savingsAccount = new SavingsAccount();
      savingsAccount.setBranch(agencyDropdown.currentData().toString());
      savingsAccount.setInterestRate(0.5); // Default interest rate

      // Call controller
      var controller = new UserController();
      controller.createSavingsAccount(customer, savingsAccount);

      QMessageBox.information(this, "Sucesso", "Conta poupança criada com sucesso!");
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao criar conta poupança: " + e.getMessage());
    }
  }
}