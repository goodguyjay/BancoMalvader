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
    var layout = new QVBoxLayout(centralWidget);

    layout.setContentsMargins(20, 20, 20, 20);
    layout.setSpacing(20);

    var bankNameLabel = new QLabel("Banco Malvader");
    bankNameLabel.setFont(FontHelper.getBaseFont(32));
    bankNameLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    layout.addWidget(bankNameLabel);

    String[] menuOptions = {"Login", "Trocar Senha", "Fazer Cadastro", "Sair"};

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
              + "}"
              + "QPushButton:hover {"
              + "background-color: #45a049;"
              + "}");

      if (option.equals("Sair")) {
        button.clicked.connect(this, "terminate()");

      } else if (option.equals("Login")) {
        button.clicked.connect(
            () -> {
              var loginPage = new LoginPage(this);
              setCentralWidget(loginPage);
            });
      }
    }

    centralWidget.setStyleSheet("background-color: #f0f0f0;");

    centralWidget.setLayout(layout);
    setCentralWidget(centralWidget);
    resize(1366, 768);
  }

  private void terminate() {
    logger.info("fechando aplicação...");
    QApplication.quit();
  }
}
