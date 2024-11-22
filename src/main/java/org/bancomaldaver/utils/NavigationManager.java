package org.bancomaldaver.utils;

import io.qt.widgets.QMainWindow;
import io.qt.widgets.QWidget;
import java.util.Stack;

/**
 * gerenciador de navegação para a aplicação. permite gerenciar a troca de páginas dentro da janela
 * principal. utiliza uma pilha (stack) para controlar a navegação e o histórico de páginas. obs:
 * essa classe não está sendo utilizada em todo o projeto por limitações de tempo e complexidade.
 */
public final class NavigationManager {

  /** instância única do gerenciador de navegação (singleton pattern). */
  private static NavigationManager instance;

  /** pilha que armazena as páginas navegadas. */
  private final Stack<QWidget> navigationStack = new Stack<>();

  /** referência à janela principal da aplicação. */
  private QMainWindow mainWindow;

  /** construtor privado para evitar a instanciação direta. */
  private NavigationManager() {}

  /**
   * retorna a instância única do gerenciador de navegação. se a instância ainda não foi criada, uma
   * nova será inicializada.
   *
   * @return instância do {@code NavigationManager}.
   */
  public static synchronized NavigationManager getInstance() {
    if (instance == null) {
      instance = new NavigationManager();
    }
    return instance;
  }

  /**
   * inicializa o gerenciador de navegação com a janela principal.
   *
   * @param mainWindow a janela principal da aplicação.
   */
  public void initialize(QMainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }

  /**
   * navega para a página fornecida. adiciona a página atual à pilha antes de trocar para a nova
   * página.
   *
   * @param page o widget da nova página a ser exibida.
   */
  public void navigateTo(QWidget page) {
    if (!navigationStack.isEmpty()) {
      var currentPage = navigationStack.peek();
      navigationStack.push(page);
      mainWindow.setCentralWidget(page);
      currentPage.close();
    } else {
      navigationStack.push(page);
      mainWindow.setCentralWidget(page);
    }
  }

  /**
   * retorna à página anterior na pilha de navegação. fecha a página atual e exibe a página anterior
   * como a central.
   *
   * @param page a página que será definida como central ao retornar.
   * @throws IllegalArgumentException se a página for inválida ou se a pilha de navegação estiver
   *     vazia.
   */
  public void goBack(QWidget page) {
    if (page != null && navigationStack.size() > 1) {
      navigationStack.pop().close();
      mainWindow.setCentralWidget(page);
      return;
    }

    throw new IllegalArgumentException("Página inválida ou a stack de navegação está vazia.");
  }
}
