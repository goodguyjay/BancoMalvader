package org.bancomaldaver;

import io.qt.core.Qt;
import io.qt.widgets.QApplication;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.views.MainMenuPage;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    var consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    logger.setLevel(Level.ALL);

    try {
      logger.info("Iniciando a aplicação...");

      QApplication.initialize(args);

      QApplication.setAttribute(Qt.ApplicationAttribute.AA_UseDesktopOpenGL);

      var mainMenu = new MainMenuPage();
      mainMenu.show();

      QApplication.exec();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erro ao iniciar a aplicação: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
