package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import java.util.List;
import java.util.Map;
import org.bancomaldaver.controllers.CustomerController;
import org.bancomaldaver.utils.ButtonUtils;
import org.bancomaldaver.utils.FontHelper;

/**
 * página principal para clientes. permite acesso a funcionalidades como consultar saldo, realizar
 * depósitos, saques, visualizar extrato e consultar limite de crédito.
 */
public final class CustomerMainPage extends QWidget {

  /**
   * construtor da página principal do cliente.
   *
   * @param mainWindow a janela principal da aplicação.
   * @param accountId o identificador da conta do cliente.
   */
  public CustomerMainPage(QMainWindow mainWindow, int accountId) {
    setWindowTitle("Banco Malvader - Cliente");

    var mainLayout = new QVBoxLayout();

    // Cabeçalho com boas-vindas
    var headerLabel = new QLabel("Bem-vindo, Cliente!");
    headerLabel.setFont(FontHelper.getBaseFont(24));
    headerLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    mainLayout.addWidget(headerLabel);

    // Botões para funcionalidades principais
    var balanceButton =
        ButtonUtils.createButton("Consultar Saldo", () -> showBalanceDialog(accountId), this);
    var depositButton =
        ButtonUtils.createButton("Depósito", () -> showDepositDialog(accountId), this);
    var withdrawalButton =
        ButtonUtils.createButton("Saque", () -> showWithdrawalDialog(accountId), this);
    var statementButton =
        ButtonUtils.createButton("Extrato", () -> showStatementDialog(accountId), this);

    mainLayout.addWidget(balanceButton);
    mainLayout.addWidget(depositButton);
    mainLayout.addWidget(withdrawalButton);
    mainLayout.addWidget(statementButton);

    try {
      // Verifica o tipo de conta e adiciona botão para limite se for conta corrente
      var controller = new CustomerController();
      var accountType = controller.getAccountType(accountId);

      if ("CHECKING".equals(accountType)) {
        var creditLimitButton =
            ButtonUtils.createButton(
                "Consultar Limite", () -> showCreditLimitDialog(accountId), this);
        mainLayout.addWidget(creditLimitButton);
      }
    } catch (Exception e) {
      QMessageBox.critical(this, "Erro", "Erro ao carregar tipo de conta: " + e.getMessage());
    }

    // Botão para voltar ao menu principal
    var backButton = ButtonUtils.createButton("Voltar ao Menu Principal", mainWindow::close, this);
    mainLayout.addWidget(backButton);

    setLayout(mainLayout);
  }

  /**
   * exibe um diálogo para consultar o saldo da conta.
   *
   * @param accountId o identificador da conta do cliente.
   */
  private void showBalanceDialog(int accountId) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consultar Saldo");
    var layout = new QVBoxLayout(dialog);

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    var fetchButton = new QPushButton("Consultar");
    fetchButton.clicked.connect(
        () -> {
          try {
            var controller = new CustomerController();
            double balance = controller.getBalance(accountId, passwordField.text());
            QMessageBox.information(
                this, "Saldo", "Seu saldo é: R$ " + String.format("%.2f", balance));
            dialog.accept();
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro: " + e.getMessage());
          }
        });

    layout.addWidget(passwordField);
    layout.addWidget(fetchButton);
    dialog.setLayout(layout);
    dialog.exec();
  }

  /**
   * exibe um diálogo para realizar depósito.
   *
   * @param accountId o identificador da conta do cliente.
   */
  private void showDepositDialog(int accountId) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Depósito");
    var layout = new QVBoxLayout(dialog);

    var amountField = new QLineEdit();
    amountField.setPlaceholderText("Digite o valor do depósito");

    var depositButton = new QPushButton("Depositar");
    depositButton.clicked.connect(
        () -> {
          try {
            double amount = Double.parseDouble(amountField.text());
            var controller = new CustomerController();
            controller.deposit(accountId, amount);
            QMessageBox.information(this, "Sucesso", "Depósito realizado com sucesso!");
            dialog.accept();
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro: " + e.getMessage());
          }
        });

    layout.addWidget(amountField);
    layout.addWidget(depositButton);
    dialog.setLayout(layout);
    dialog.exec();
  }

  /**
   * exibe um diálogo para realizar saque.
   *
   * @param accountId o identificador da conta do cliente.
   */
  private void showWithdrawalDialog(int accountId) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Saque");
    var layout = new QVBoxLayout(dialog);

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    var amountField = new QLineEdit();
    amountField.setPlaceholderText("Digite o valor do saque");

    var withdrawButton = new QPushButton("Sacar");
    withdrawButton.clicked.connect(
        () -> {
          try {
            double amount = Double.parseDouble(amountField.text());
            var controller = new CustomerController();
            controller.withdraw(accountId, amount, passwordField.text());
            QMessageBox.information(this, "Sucesso", "Saque realizado com sucesso!");
            dialog.accept();
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro: " + e.getMessage());
          }
        });

    layout.addWidget(passwordField);
    layout.addWidget(amountField);
    layout.addWidget(withdrawButton);
    dialog.setLayout(layout);
    dialog.exec();
  }

  /**
   * exibe um diálogo para visualizar o extrato da conta.
   *
   * @param accountId o identificador da conta do cliente.
   */
  private void showStatementDialog(int accountId) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Extrato");
    var layout = new QVBoxLayout(dialog);

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    var fetchButton = new QPushButton("Consultar Extrato");
    fetchButton.clicked.connect(
        () -> {
          try {
            var controller = new CustomerController();
            List<Map<String, String>> transactions =
                controller.getStatement(accountId, passwordField.text());

            StringBuilder statement = new StringBuilder();
            statement.append("Extrato:\n");
            for (var transaction : transactions) {
              statement
                  .append(transaction.get("transaction_date"))
                  .append(" - ")
                  .append(transaction.get("transaction_type"))
                  .append(": R$ ")
                  .append(transaction.get("amount"))
                  .append("\n");
            }

            QMessageBox.information(this, "Extrato", statement.toString());
            dialog.accept();
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro: " + e.getMessage());
          }
        });

    layout.addWidget(passwordField);
    layout.addWidget(fetchButton);
    dialog.setLayout(layout);
    dialog.exec();
  }

  /**
   * exibe um diálogo para consultar o limite de crédito.
   *
   * @param accountId o identificador da conta do cliente.
   */
  private void showCreditLimitDialog(int accountId) {
    var dialog = new QDialog(this);
    dialog.setWindowTitle("Consultar Limite");
    var layout = new QVBoxLayout(dialog);

    var passwordField = new QLineEdit();
    passwordField.setPlaceholderText("Digite sua senha");
    passwordField.setEchoMode(QLineEdit.EchoMode.Password);

    var fetchButton = new QPushButton("Consultar Limite");
    fetchButton.clicked.connect(
        () -> {
          try {
            var controller = new CustomerController();
            var creditLimit = controller.getCreditLimit(accountId, passwordField.text());
            QMessageBox.information(
                this, "Limite", "Seu limite é: R$ " + String.format("%.2f", creditLimit));
            dialog.accept();
          } catch (Exception e) {
            QMessageBox.critical(this, "Erro", "Erro: " + e.getMessage());
          }
        });

    layout.addWidget(passwordField);
    layout.addWidget(fetchButton);
    dialog.setLayout(layout);
    dialog.exec();
  }
}
