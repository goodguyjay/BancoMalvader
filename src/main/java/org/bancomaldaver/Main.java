package org.bancomaldaver;

import io.qt.core.Qt;
import io.qt.widgets.QApplication;
import java.util.logging.Logger;
import org.bancomaldaver.utils.DatabaseConnection;
import org.bancomaldaver.views.MainMenu;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    logger.info("starting application...");
    QApplication.initialize(args);
    try {
      if (DatabaseConnection.getConnection() != null) {
        logger.info("connected to the database");
      } else {
        logger.warning("could not connect to the database");
      }
    } catch (Exception exception) {
      logger.severe(exception.getMessage());
    }

    QApplication.setAttribute(Qt.ApplicationAttribute.AA_UseDesktopOpenGL);

    var mainMenu = new MainMenu();
    mainMenu.show();

    QApplication.exec();
  }
}
