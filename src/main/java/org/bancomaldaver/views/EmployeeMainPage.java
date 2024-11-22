package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.bancomaldaver.controllers.AccountController;
import org.bancomaldaver.controllers.EmployeeController;
import org.bancomaldaver.controllers.UserController;
import org.bancomaldaver.models.AccountClosureData;
import org.bancomaldaver.utils.ButtonUtils;
import org.bancomaldaver.utils.NavigationManager;

public final class EmployeeMainPage extends QWidget {

  public EmployeeMainPage(QMainWindow mainWindow) {
    setWindowTitle("Banco Malvader - Página Principal do Funcionário");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    var backButton =
        ButtonUtils.createBackButton(
            "Sair", () -> NavigationManager.getInstance().goBack(new MainMenuPage()), this);
    mainLayout.addWidget(backButton, 0, Qt.AlignmentFlag.AlignTop);

    var titleLabel = new QLabel("Menu Principal do Funcionário");
    titleLabel.setStyleSheet(
        "font-size: 24px; font-weight: bold; color: #333; text-align: center; margin-bottom: 20px;");
    titleLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    mainLayout.addWidget(titleLabel);

    var consultDataButton =
        ButtonUtils.createButton(
            "Consulta de Dados", () -> openDataConsultationDialog(mainWindow), this);

    var createAccountButton =
        ButtonUtils.createButton("Criar Contas", () -> openCreateAccountDialog(mainWindow), this);
    var alterDataButton =
        ButtonUtils.createButton("Alterar Dados", () -> openAlterDataDialog(mainWindow), this);
    var closeAccountButton =
        ButtonUtils.createButton("Encerrar Conta", () -> openCloseAccountDialog(mainWindow), this);

    var generateReportButton =
        ButtonUtils.createButton(
            "Gerar Relatório de Movimentações", this::generateFinancialReportDialog, this);
    mainLayout.addWidget(generateReportButton);

    var bottomLayout = new QVBoxLayout();
    bottomLayout.addWidget(consultDataButton);
    bottomLayout.addWidget(createAccountButton);
    bottomLayout.addWidget(alterDataButton);
    bottomLayout.addWidget(closeAccountButton);

    bottomLayout.setSpacing(15);
    mainLayout.addLayout(bottomLayout);

    centralWidget.setStyleSheet("background-color: #f5f5f5; padding: 20px;");
    setLayout(mainLayout);
  }

  private void generateFinancialReportDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Autenticação do Funcionário");

    var layout = new QVBoxLayout(dialog);

    var passwordLabel = new QLabel("Digite a senha do funcionário:");
    var passwordField = new QLineEdit();
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    layout.addWidget(passwordLabel);
    layout.addWidget(passwordField);

    var buttonLayout = new QHBoxLayout();
    var confirmButton = new QPushButton("Confirmar");
    var cancelButton = new QPushButton("Cancelar");

    buttonLayout.addWidget(confirmButton);
    buttonLayout.addWidget(cancelButton);
    layout.addLayout(buttonLayout);

    confirmButton.clicked.connect(
        () -> {
          String password = passwordField.text().trim();
          if (authenticateEmployee(password)) {
            dialog.accept();
            generateFinancialReport();
          } else {
            QMessageBox.warning(this, "Erro", "Senha do funcionário incorreta.");
          }
        });

    cancelButton.clicked.connect(dialog::reject);

    dialog.setLayout(layout);
    dialog.exec();
  }

  private boolean authenticateEmployee(String password) {
    try {
      var employeeController = new EmployeeController();
      return employeeController.authenticate(password);
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro na autenticação: " + e.getMessage());
      return false;
    }
  }

  private void generateFinancialReport() {
    try {
      var employeeController = new EmployeeController();
      var transactions = employeeController.getAllFinancialTransactions();

      var userHome = System.getenv("USERPROFILE");

      if (userHome == null || userHome.isEmpty()) {
        throw new Exception("Não foi possível determinar o diretório do usuário.");
      }

      // aqui eu tô unindo a variável do powershell com a pasta que eu quero
      var downloadsFolder = userHome + "\\Downloads";

      // nome do relatório
      var fileName = downloadsFolder + "\\financial_report.csv";

      // gera o csv
      try (var writer =
          new PrintWriter(
              new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
        // escreve nosso querido BOM... excel lê melhor com ele
        writer.write('\uFEFF');

        // headers do csv
        writer.println(
            "Transaction ID;Account Number;Account Type;Transaction Type;Amount;Date;Customer Name;CPF");

        // dados
        for (var transaction : transactions) {
          writer.println(
              String.join(
                  ";",
                  transaction.get("id_transaction"),
                  transaction.get("account_number"),
                  transaction.get("account_type"),
                  transaction.get("transaction_type"),
                  String.format("%.2f", Double.parseDouble(transaction.get("amount"))),
                  transaction.get("transaction_date"),
                  transaction.get("name"),
                  transaction.get("cpf")));
        }
      }

      QMessageBox.information(
          this, "Relatório Gerado", "Relatório financeiro gerado com sucesso:\n" + fileName);
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao gerar o relatório: " + e.getMessage());
    }
  }

  private void openDataConsultationDialog(QMainWindow mainWindow) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consulta de Dados");
    var dialogLayout = new QVBoxLayout(dialog);

    var consultAccountButton =
        ButtonUtils.createButton("Consultar Conta", () -> showAccountDetailsDialog(), this);
    var consultEmployeeButton =
        ButtonUtils.createButton("Consultar Funcionário", () -> showEmployeeDetailsDialog(), this);
    var consultCustomerButton =
        ButtonUtils.createButton("Consultar Cliente", () -> showCustomerDetailsDialog(), this);

    dialogLayout.addWidget(consultAccountButton);
    dialogLayout.addWidget(consultEmployeeButton);
    dialogLayout.addWidget(consultCustomerButton);

    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void showAccountDetailsDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consultar Conta");
    var layout = new QVBoxLayout(dialog);

    var accountNumberField = new QLineEdit();
    accountNumberField.setPlaceholderText("Digite o número da conta");

    var fetchButton = new QPushButton("Consultar");
    layout.addWidget(accountNumberField);
    layout.addWidget(fetchButton);

    fetchButton.clicked.connect(
        () -> {
          try {
            var accountNumber = accountNumberField.text().trim();
            var accountController = new AccountController();
            var details = accountController.getAccountDetails(accountNumber);

            var message = new StringBuilder();
            message.append("Tipo: ").append(details.get("account_type")).append("\n");
            message.append("Nome: ").append(details.get("name")).append("\n");
            message.append("CPF: ").append(details.get("cpf")).append("\n");
            message
                .append("Saldo: ")
                .append(String.format("%.2f", details.get("balance")))
                .append("\n");

            if ("CHECKING".equals(details.get("account_type"))) {
              message
                  .append("Limite: ")
                  .append(String.format("%.2f", details.get("credit_limit")))
                  .append("\n");
              message.append("Vencimento: ").append(details.get("due_date")).append("\n");
            } else if ("SAVINGS".equals(details.get("account_type"))) {
              message.append("Taxa de Juros: ").append(details.get("interest_rate")).append("\n");
            }

            QMessageBox.information(this, "Detalhes da Conta", message.toString());
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro ao consultar conta: " + e.getMessage());
          }
        });

    dialog.setLayout(layout);
    dialog.exec();
  }

  private void showEmployeeDetailsDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consultar Funcionário");
    var layout = new QVBoxLayout(dialog);

    var employeeCodeField = new QLineEdit();
    employeeCodeField.setPlaceholderText("Digite o código do funcionário");

    var fetchButton = new QPushButton("Consultar");
    layout.addWidget(employeeCodeField);
    layout.addWidget(fetchButton);

    fetchButton.clicked.connect(
        () -> {
          try {
            var employeeCode = employeeCodeField.text().trim();
            var employeeController = new EmployeeController();
            var details = employeeController.getEmployeeDetails(employeeCode);

            var message = new StringBuilder();
            message.append("Código: ").append(details.get("employee_code")).append("\n");
            message.append("Cargo: ").append(details.get("role")).append("\n");
            message.append("Nome: ").append(details.get("name")).append("\n");
            message.append("CPF: ").append(details.get("cpf")).append("\n");
            message.append("Data de Nascimento: ").append(details.get("birth_date")).append("\n");
            message.append("Telefone: ").append(details.get("phone")).append("\n");
            message.append("Endereço: ").append(details.get("address")).append("\n");

            QMessageBox.information(this, "Detalhes do Funcionário", message.toString());
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro ao consultar funcionário: " + e.getMessage());
          }
        });

    dialog.setLayout(layout);
    dialog.exec();
  }

  private void showCustomerDetailsDialog() {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consultar Cliente");
    var layout = new QVBoxLayout(dialog);

    var cpfField = new QLineEdit();
    cpfField.setPlaceholderText("Digite o CPF do cliente");

    var fetchButton = new QPushButton("Consultar");
    layout.addWidget(cpfField);
    layout.addWidget(fetchButton);

    fetchButton.clicked.connect(
        () -> {
          try {
            var cpf = cpfField.text().trim();
            var customerController = new UserController();
            var details = customerController.getCustomerDetails(cpf);

            var message = new StringBuilder();
            message.append("Nome: ").append(details.get("name")).append("\n");
            message.append("CPF: ").append(details.get("cpf")).append("\n");
            message.append("Data de Nascimento: ").append(details.get("birth_date")).append("\n");
            message.append("Telefone: ").append(details.get("phone")).append("\n");
            message.append("Endereço: ").append(details.get("address")).append("\n");

            QMessageBox.information(this, "Detalhes do Cliente", message.toString());
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro ao consultar cliente: " + e.getMessage());
          }
        });

    dialog.setLayout(layout);
    dialog.exec();
  }

  private void openCreateAccountDialog(QMainWindow mainWindow) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Criar Contas");
    var dialogLayout = new QVBoxLayout(dialog);

    var createCheckingAccountButton =
        ButtonUtils.createButton(
            "Criar Conta Corrente",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new CreateCheckingAccountPage((QMainWindow) parent()));
            },
            this);

    var createSavingsAccountButton =
        ButtonUtils.createButton(
            "Criar Conta Poupança",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new CreateSavingsAccountPage((QMainWindow) parent()));
            },
            this);

    var createEmployeeButton =
        ButtonUtils.createButton(
            "Criar Funcionário",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new CreateEmployeeAccountPage((QMainWindow) parent()));
            },
            this);

    dialogLayout.addWidget(createCheckingAccountButton);
    dialogLayout.addWidget(createSavingsAccountButton);
    dialogLayout.addWidget(createEmployeeButton);
    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void openAlterDataDialog(QMainWindow mainWindow) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Alterar Dados");
    var dialogLayout = new QVBoxLayout(dialog);

    var alterCheckingAccountButton =
        ButtonUtils.createButton(
            "Alterar Limite/Data de Conta Corrente",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new AlterCheckingAccountPage((QMainWindow) parent()));
            },
            this);

    var alterEmployeeDataButton =
        ButtonUtils.createButton(
            "Alterar Dados do Funcionário",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new AlterEmployeeDataPage((QMainWindow) parent()));
            },
            this);

    dialogLayout.addWidget(alterCheckingAccountButton);
    dialogLayout.addWidget(alterEmployeeDataButton);
    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void openAlterCustomerDataDialog(QMainWindow mainWindow) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Alterar Dados do Cliente");
    var dialogLayout = new QVBoxLayout(dialog);

    var alterCustomerDataButton =
        ButtonUtils.createButton(
            "Alterar Telefone/Endereço do Cliente",
            () -> {
              dialog.accept();
              mainWindow.setCentralWidget(new AlterCustomerDataPage((QMainWindow) parent()));
            },
            this);

    dialogLayout.addWidget(alterCustomerDataButton);
    dialog.setLayout(dialogLayout);
    dialog.exec();
  }

  private void openCloseAccountDialog(QMainWindow mainWindow) {
    var adminPasswordDialog = new QDialog(this);
    adminPasswordDialog.setWindowTitle("Autenticação de Administrador");
    var dialogLayout = new QVBoxLayout(adminPasswordDialog);

    var passwordLabel = new QLabel("Digite a senha de administrador:");
    var passwordField = new QLineEdit();
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    dialogLayout.addWidget(passwordLabel);
    dialogLayout.addWidget(passwordField);

    var buttonLayout = new QHBoxLayout();
    var confirmButton = new QPushButton("Confirmar");
    var cancelButton = new QPushButton("Cancelar");
    buttonLayout.addWidget(confirmButton);
    buttonLayout.addWidget(cancelButton);
    dialogLayout.addLayout(buttonLayout);

    confirmButton.clicked.connect(
        () -> {
          if (passwordField.text().equals("admin")) {
            adminPasswordDialog.accept();
            showAccountClosureDialog();
          } else {
            QMessageBox.warning(this, "Erro", "Senha de administrador incorreta.");
          }
        });

    cancelButton.clicked.connect(adminPasswordDialog::reject);

    adminPasswordDialog.setLayout(dialogLayout);
    adminPasswordDialog.exec();
  }

  private void showAccountClosureDialog() {
    var closeAccountDialog = new QDialog(this);
    closeAccountDialog.setWindowTitle("Encerramento de Conta");
    var dialogLayout = new QVBoxLayout(closeAccountDialog);

    var cpfLabel = new QLabel("Digite o CPF do cliente:");
    var cpfField = new QLineEdit();

    dialogLayout.addWidget(cpfLabel);
    dialogLayout.addWidget(cpfField);

    var buttonLayout = new QHBoxLayout();
    var fetchButton = new QPushButton("Buscar Dados");
    var closeButton = new QPushButton("Encerrar Conta");
    var cancelButton = new QPushButton("Cancelar");
    buttonLayout.addWidget(fetchButton);
    buttonLayout.addWidget(closeButton);
    buttonLayout.addWidget(cancelButton);
    dialogLayout.addLayout(buttonLayout);

    fetchButton.clicked.connect(
        () -> {
          var cpf = cpfField.text().trim();
          if (!cpf.isEmpty()) {
            try {
              AccountController accountController = new AccountController();
              AccountClosureData data = accountController.getAccountClosureData(cpf);

              QMessageBox.information(
                  this,
                  "Confirmação",
                  String.format(
                      "CPF do cliente: %s\nNome do cliente: ID %d\nNúmero da conta: %s",
                      data.getCpf(), data.getUserId(), data.getAccountNumber()));
            } catch (Exception e) {
              QMessageBox.warning(this, "Erro", "Erro ao buscar dados: " + e.getMessage());
            }
          } else {
            QMessageBox.warning(this, "Erro", "Por favor, insira o CPF.");
          }
        });

    closeButton.clicked.connect(
        () -> {
          var cpf = cpfField.text().trim();
          if (!cpf.isEmpty()) {
            try {
              var accountController = new AccountController();
              var data = accountController.getAccountClosureData(cpf);

              int response =
                  QMessageBox.question(
                      this,
                      "Confirmar Encerramento",
                      String.format(
                          "Tem certeza que deseja encerrar a conta?\nCPF: %s\nNúmero da conta: %s",
                          data.getCpf(), data.getAccountNumber()),
                      QMessageBox.StandardButton.Yes,
                      QMessageBox.StandardButton.No);

              if (response == QMessageBox.StandardButton.Yes.value()) {
                var success = accountController.closeAccount(data.getAccountNumber());
                if (success) {
                  QMessageBox.information(this, "Sucesso", "Conta encerrada com sucesso.");
                  closeAccountDialog.accept();
                } else {
                  QMessageBox.warning(this, "Erro", "Falha ao encerrar a conta.");
                }
              }
            } catch (Exception e) {
              QMessageBox.critical(this, "Erro", "Erro ao encerrar a conta: " + e.getMessage());
            }
          } else {
            QMessageBox.warning(this, "Erro", "Por favor, insira o CPF.");
          }
        });

    cancelButton.clicked.connect(closeAccountDialog::reject);

    closeAccountDialog.setLayout(dialogLayout);
    closeAccountDialog.exec();
  }
}
