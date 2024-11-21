package org.bancomaldaver.utils;

import io.qt.widgets.QDialog;
import io.qt.widgets.QMainWindow;
import io.qt.widgets.QWidget;
import java.util.Stack;

public final class NavigationManager {
  private static NavigationManager instance;
  private final Stack<QWidget> navigationStack = new Stack<>();
  private QMainWindow mainWindow;

  private NavigationManager() {}

  public static synchronized NavigationManager getInstance() {
    if (instance == null) {
      instance = new NavigationManager();
    }
    return instance;
  }

  public void initialize(QMainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }

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

  public void goBack(QWidget page) {
    if (page != null && navigationStack.size() > 1) {
      navigationStack.pop().close();
      mainWindow.setCentralWidget(page);
      return;
    }

    throw new IllegalArgumentException("Página inválida ou a stack de navegação está vazia.");
  }

  public void openDialog(QDialog dialog) {
    dialog.exec();
  }

  public void clearStackAndNavigateTo(QWidget page) {
    while (!navigationStack.isEmpty()) {
      navigationStack.pop().close();
    }
    navigationStack.push(page);
    mainWindow.setCentralWidget(page);
  }
}
