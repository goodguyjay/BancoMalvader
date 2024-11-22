package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import java.util.logging.Logger;
import org.bancomaldaver.utils.FontHelper;
import org.bancomaldaver.utils.NavigationManager;

/**
 * classe que representa a página principal do menu do Banco Malvader. contém opções para Login de
 * Usuário, Login de Funcionário e Sair.
 */
public final class MainMenuPage extends QMainWindow {
  private static final Logger logger = Logger.getLogger(MainMenuPage.class.getName());

  /** construtor da classe MainMenuPage. configura a interface principal do menu. */
  public MainMenuPage() {
    setWindowTitle("Banco Malvader - Menu Principal");

    var centralWidget = new QWidget();
    centralWidget.setStyleSheet("border-radius: 15px;" + "border: 1px solid #ccc;");

    var layout = new QVBoxLayout(centralWidget);

    layout.setContentsMargins(40, 40, 40, 30);
    layout.setSpacing(20);

    var bankNameLabel = new QLabel("Banco Malvader");
    bankNameLabel.setFont(FontHelper.getBaseFont(32));
    bankNameLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    bankNameLabel.setStyleSheet(
        "color: #4CAF50;" + "font-weight: bold;" + "text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);");
    layout.addWidget(bankNameLabel);

    String[] menuOptions = {"Login Usuário", "Login Funcionário", "Sair"};

    for (var option : menuOptions) {
      var button = new QPushButton(option);
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

      switch (option) {
        case "Sair" -> button.clicked.connect(this, "terminate()");
        case "Login Usuário" ->
            button.clicked.connect(
                () -> {
                  NavigationManager.getInstance()
                      .navigateTo(new CustomerLoginPage((QMainWindow) parent()));
                });
        case "Login Funcionário" ->
            button.clicked.connect(
                () -> {
                  NavigationManager.getInstance()
                      .navigateTo(new EmployeeLoginPage((QMainWindow) parent()));
                });
      }
    }

    centralWidget.setLayout(layout);
    setCentralWidget(centralWidget);
  }

  /** termina a aplicação. */
  private void terminate() {
    logger.info("fechando aplicação...");
    QApplication.quit();
  }
}
