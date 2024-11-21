package org.bancomaldaver.utils;

import io.qt.core.QMetaObject;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QWidget;

public final class ButtonUtils {
  private ButtonUtils() {
    throw new UnsupportedOperationException("Essa classe não deve ser instanciada");
  }

  public static QPushButton createButton(String text, Runnable onClick, QWidget parent) {
    var button = new QPushButton(text, parent);
    button.setFont(FontHelper.getBaseFont(16));
    button.setFixedHeight(50);
    button.setStyleSheet(
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
    button.clicked.connect((QMetaObject.Slot0) onClick::run);
    return button;
  }

  public static QPushButton createBackButton(String text, Runnable onClick, QWidget parent) {
    var button = new QPushButton(text, parent);
    button.setFont(FontHelper.getBaseFont(16));
    button.setStyleSheet(
        "QPushButton {"
            + "background-color: #cccccc;"
            + "color: black;"
            + "border-radius: 8px;"
            + "padding: 8px 12px;"
            + "}"
            + "QPushButton:hover {"
            + "background-color: #b3b3b3;"
            + "}");
    button.clicked.connect((QMetaObject.Slot0) onClick::run);
    return button;
  }
}
