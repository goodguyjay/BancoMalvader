package org.bancomaldaver.views;

import io.qt.core.Qt;
import io.qt.widgets.*;
import org.bancomaldaver.utils.FontHelper;

/**
 * página de interface do usuário para alterar os dados de funcionários. permite ao funcionário
 * modificar o código, cargo, telefone e endereço de outros funcionários.
 */
public final class AlterEmployeeDataPage extends QWidget {

  /**
   * construtor da página de alteração de dados do funcionário.
   *
   * @param mainWindow a janela principal da aplicação para navegação entre páginas.
   */
  public AlterEmployeeDataPage(QMainWindow mainWindow) {
    // define o título da janela
    setWindowTitle("Alterar Dados do Funcionário");

    // cria o widget central e o layout principal
    var centralWidget = new QWidget();
    var mainLayout = new QVBoxLayout(centralWidget);

    // cria o layout superior com o botão "Voltar"
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

    // ação do botão "Voltar" para navegar de volta para a página principal do funcionário
    backButton.clicked.connect(() -> mainWindow.setCentralWidget(new EmployeeMainPage(mainWindow)));
    topLayout.addWidget(backButton);
    mainLayout.addLayout(topLayout);

    // cria um rótulo para exibir a mensagem de placeholder
    var placeholderLabel =
        new QLabel(
            "Funcionalidade para alterar código, cargo, telefone e endereço de funcionários.");
    placeholderLabel.setFont(FontHelper.getBaseFont(18));
    placeholderLabel.setAlignment(Qt.AlignmentFlag.AlignCenter); // centraliza o texto no rótulo
    mainLayout.addWidget(placeholderLabel);

    // define o layout principal para a página
    setLayout(mainLayout);
  }
}
