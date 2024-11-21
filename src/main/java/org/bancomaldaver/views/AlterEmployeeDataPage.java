package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.utils.FontHelper;

public class AlterEmployeeDataPage extends QWidget {
  public AlterEmployeeDataPage(QMainWindow mainWindow) {
    setWindowTitle("Alterar Dados do Funcionário");

    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // Back button
    var topLayout = new QHBoxLayout();
    var backButton = new QPushButton("Voltar");
    backButton.setFont(FontHelper.getBaseFont(16));
    backButton.setStyleSheet(
        "QPushButton {"
            + "background-color: #cccccc;"
            + "color: black;"
            + "border-radius: 8px;"
            + "padding: 8px 12px;"
            + "}"
            + "QPushButton:hover {"
            + "background-color: #b3b3b3;"
            + "}");
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new EmployeeMainPage(mainWindow)));
    topLayout.addWidget(backButton);
    mainLayout.addLayout(topLayout);

    // Placeholder Label
    var placeholderLabel =
        new QLabel(
            "Funcionalidade para alterar código, cargo, telefone e endereço de funcionários.");
    placeholderLabel.setFont(FontHelper.getBaseFont(18));
    placeholderLabel.setAlignment(Qt.AlignmentFlag.AlignCenter);
    mainLayout.addWidget(placeholderLabel);

    setLayout(mainLayout);
  }
}
