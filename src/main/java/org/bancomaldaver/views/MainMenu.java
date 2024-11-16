package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import java.util.logging.Logger;
import org.bancomaldaver.utils.FontHelper;

public class MainMenu extends QMainWindow {
  private static final Logger logger = Logger.getLogger(MainMenu.class.getName());

  public MainMenu() {
    setWindowTitle("Banco Malvader - Menu Principal");

    var centralWidget = new QWidget();
    centralWidget.setStyleSheet(
        "background-color: #f5f5f5;" + "border-radius: 15px;" + "border: 1px solid #ccc;");

    var layout = new QVBoxLayout(centralWidget);

    layout.setContentsMargins(40, 40, 40, 30);
    layout.setSpacing(20);

    var bankNameLabel = new QLabel("Banco Malvader");
    bankNameLabel.setFont(FontHelper.getBaseFont(32));
    bankNameLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    bankNameLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);");
    layout.addWidget(bankNameLabel);

    String[] menuOptions = {"Login", "Fazer Cadastro", "Sair"};

    for (var option : menuOptions) {
      QPushButton button = new QPushButton(option);
      button.setFont(FontHelper.getBaseFont(18));
      button.setFixedHeight(50);
      layout.addWidget(button);

      button.setStyleSheet(
          "QPushButton {"
              + "background-color: #4CAF50;"
              + "color: white;"
              + "border-radius: 8px;"
              + "padding: 10px;"
              + "border: none;"
              + "transition: background-color 0.3s;"
              + "font-weight: bold;"
              + "}"
              + "QPushButton:hover {"
              + "background-color: #45a049;"
              + "}"
              + "QPushButton:pressed {"
              + "background-color: #388E3C;"
              + "}");

      if (option.equals("Sair")) {
        button.clicked.connect(this, "terminate()");

      } else if (option.equals("Login")) {
        button.clicked.connect(
            () -> {
              var loginPage = new LoginPage(this);
              setCentralWidget(loginPage);
            });
      } else if (option.equals("Fazer Cadastro")) {
        button.clicked.connect(
            () -> {
              var dialog = new QDialog(this);
              dialog.setWindowTitle("Escolha o tipo de conta");

              var dialogLayout = new QVBoxLayout(dialog);

              var label = new QLabel("Selecione o tipo de conta para cadastro:");
              label.setFont(FontHelper.getBaseFont(18));
              label.setAlignment(Qt.AlignmentFlag.AlignCenter);
              dialogLayout.addWidget(label);

              var buttonCheckingAccount = new QPushButton("Conta Corrente");
              buttonCheckingAccount.setFont(FontHelper.getBaseFont(16));
              buttonCheckingAccount.setStyleSheet(
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

              dialogLayout.addWidget(buttonCheckingAccount);

              var buttonSavingsAccount = new QPushButton("Conta poupança");
              buttonSavingsAccount.setFont(FontHelper.getBaseFont(16));
              buttonSavingsAccount.setStyleSheet(buttonCheckingAccount.styleSheet());
              dialogLayout.addWidget(buttonSavingsAccount);

              buttonSavingsAccount.clicked.connect(
                  () -> {
                    dialog.accept();
                    var registerSavingsAccountPage = new SavingsAccountPage(this);
                    setCentralWidget(registerSavingsAccountPage);
                  });

              buttonCheckingAccount.clicked.connect(
                  () -> {
                    dialog.accept();

                    QMessageBox.information(
                        this, "Em breve", "Cadastro de conta corrente em breve!");
                  });

              dialog.setLayout(dialogLayout);
              dialog.exec();
            });
      }
    }

    centralWidget.setLayout(layout);
    setCentralWidget(centralWidget);
    resize(1366, 768);
  }

  private void terminate() {
    logger.info("fechando aplicação...");
    QApplication.quit();
  }
}
