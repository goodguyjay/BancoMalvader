package org.bancomaldaver;

import io.qt.core.Qt;
import io.qt.widgets.QApplication;
import io.qt.widgets.QMainWindow;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bancomaldaver.utils.NavigationManager;
import org.bancomaldaver.views.MainMenuPage;

/**
 * classe principal do sistema. responsável por inicializar a aplicação e configurar o ambiente
 * gráfico.
 */
public class Main {
  /** logger para registrar mensagens e eventos durante a execução da aplicação. */
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    // configura o console para registrar todos os níveis de log
    var consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    logger.setLevel(Level.ALL);

    try {
      logger.info("Iniciando a aplicação...");

      // inicializa a aplicação
      QApplication.initialize(args);

      // força o uso de OpenGL de desktop para melhorar o desempenho gráfico
      QApplication.setAttribute(Qt.ApplicationAttribute.AA_UseDesktopOpenGL);

      // cria a janela principal da aplicação
      var mainWindow = new QMainWindow();
      mainWindow.setWindowTitle("Banco Malvader");

      // inicializa o gerenciador de navegação e define a página inicial
      NavigationManager.getInstance().initialize(mainWindow);
      NavigationManager.getInstance().navigateTo(new MainMenuPage());

      // exibe a janela principal na tela e define o tamanho
      mainWindow.show();
      mainWindow.resize(1366, 768);

      // inicia o loop principal da aplicação
      QApplication.exec();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Erro ao iniciar a aplicação: " + e.getMessage());
    }
  }
}
