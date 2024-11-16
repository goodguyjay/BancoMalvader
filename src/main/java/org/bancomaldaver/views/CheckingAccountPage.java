package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import io.qt.widgets.QMainWindow;
import org.bancomaldaver.utils.FontHelper;

public class CheckingAccountPage extends QWidget {

  public CheckingAccountPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Conta Corrente");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Top-left layout for the back button
    var topLayout = new QHBoxLayout();
    topLayout.setAlignment(Qt.AlignmentFlag.AlignLeft);

    // Back button styling for the top-left corner
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
    topLayout.addWidget(backButton);

    // Center layout for the form
    var formLayout = new QFormLayout();
    formLayout.setSpacing(15);

    // Fields (Same as Poupança plus two additional fields)
    var agencyField = new QLineEdit();
    agencyField.setPlaceholderText("Agência");
    agencyField.setFont(FontHelper.getBaseFont(16));
    agencyField.setStyleSheet(
        "padding: 8px;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;"
            + "background-color: #ffffff;"
            + "color: #333;"
            + "font-size: 16px;"
            + "placeholder-text-color: #999;");

    var accountNumberField = new QLineEdit();
    accountNumberField.setPlaceholderText("Número da Conta");
    accountNumberField.setFont(FontHelper.getBaseFont(16));
    accountNumberField.setStyleSheet(agencyField.styleSheet());

    var usernameField = new QLineEdit();
    usernameField.setPlaceholderText("Nome Completo");
    usernameField.setFont(FontHelper.getBaseFont(16));
    usernameField.setStyleSheet(agencyField.styleSheet());

    var cpfField = new QLineEdit();
    cpfField.setPlaceholderText("CPF");
    cpfField.setFont(FontHelper.getBaseFont(16));
    cpfField.setStyleSheet(agencyField.styleSheet());

    var birthDateField = new QDateEdit();
    birthDateField.setFont(FontHelper.getBaseFont(16));
    birthDateField.setCalendarPopup(true);
    birthDateField.setStyleSheet(
        "padding: 6px;"
            + "border: 1px solid #ccc;"
            + "border-radius: 5px;"
            + "background-color: #ffffff;"
            + "color: #333;"
            + "font-size: 16px;");

    var cepField = new QLineEdit();
    cepField.setPlaceholderText("CEP");
    cepField.setFont(FontHelper.getBaseFont(16));
    cepField.setStyleSheet(agencyField.styleSheet());

    var streetField = new QLineEdit();
    streetField.setPlaceholderText("Logradouro");
    streetField.setFont(FontHelper.getBaseFont(16));
    streetField.setStyleSheet(agencyField.styleSheet());

    var houseNumberField = new QLineEdit();
    houseNumberField.setPlaceholderText("Número");
    houseNumberField.setFont(FontHelper.getBaseFont(16));
    houseNumberField.setStyleSheet(agencyField.styleSheet());

    var neighborhoodField = new QLineEdit();
    neighborhoodField.setPlaceholderText("Bairro");
    neighborhoodField.setFont(FontHelper.getBaseFont(16));
    neighborhoodField.setStyleSheet(agencyField.styleSheet());

    var cityField = new QLineEdit();
    cityField.setPlaceholderText("Cidade");
    cityField.setFont(FontHelper.getBaseFont(16));
    cityField.setStyleSheet(agencyField.styleSheet());

    var stateField = new QLineEdit();
    stateField.setPlaceholderText("Estado");
    stateField.setFont(FontHelper.getBaseFont(16));
    stateField.setStyleSheet(agencyField.styleSheet());

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Senha");
    passwordField.setFont(FontHelper.getBaseFont(16));
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);
    passwordField.setStyleSheet(agencyField.styleSheet());

    // Additional fields for Conta Corrente
    var limiteContaField = new QLineEdit();
    limiteContaField.setPlaceholderText("Limite da Conta");
    limiteContaField.setFont(FontHelper.getBaseFont(16));
    limiteContaField.setStyleSheet(agencyField.styleSheet());

    var dataVencimentoField = new QDateEdit();
    dataVencimentoField.setFont(FontHelper.getBaseFont(16));
    dataVencimentoField.setCalendarPopup(true);
    dataVencimentoField.setStyleSheet(birthDateField.styleSheet());

    // Add fields to form
    formLayout.addRow("Agência:", agencyField);
    formLayout.addRow("Número da Conta:", accountNumberField);
    formLayout.addRow("Nome Completo:", usernameField);
    formLayout.addRow("CPF:", cpfField);
    formLayout.addRow("Data de Nascimento:", birthDateField);
    formLayout.addRow("CEP:", cepField);
    formLayout.addRow("Logradouro:", streetField);
    formLayout.addRow("Número:", houseNumberField);
    formLayout.addRow("Bairro:", neighborhoodField);
    formLayout.addRow("Cidade:", cityField);
    formLayout.addRow("Estado:", stateField);
    formLayout.addRow("Senha:", passwordField);
    formLayout.addRow("Limite da Conta:", limiteContaField);
    formLayout.addRow("Data de Vencimento:", dataVencimentoField);

    // Register button
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

    // Set up button actions
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new MainMenuPage()));
    registerButton.clicked.connect(this::onRegisterClicked);

    // Assemble layouts
    mainLayout.addLayout(topLayout);
    mainLayout.addLayout(formLayout);
    mainLayout.addWidget(registerButton);

    centralWidget.setStyleSheet(
        "background-color: #f5f5f5;" + "border-radius: 10px;" + "padding: 20px;");
    setLayout(mainLayout);
  }

  private void onRegisterClicked() {
    QMessageBox.information(this, "Cadastro", "Cadastro realizado com sucesso!");
  }
}
