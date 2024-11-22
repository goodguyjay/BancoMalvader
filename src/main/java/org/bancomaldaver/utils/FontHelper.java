package org.bancomaldaver.utils;

import io.qt.gui.QFont;
import io.qt.gui.QFontDatabase;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * classe utilitaria para gerenciar fontes usadas na aplicação. carrega a fonte Segoe UI e fornece
 * métodos para configurar fontes personalizadas.
 */
public final class FontHelper {

  /** logger utilizado para registrar mensagens relacionadas ao carregamento de fontes. */
  private static final Logger logger = Logger.getLogger(FontHelper.class.getName());

  /** fonte base utilizada como padrão na aplicação. */
  private static QFont baseFont;

  static {
    try {
      // caminho para o arquivo de fonte "Segoe UI"
      var fontPath =
          Paths.get(
                  Objects.requireNonNull(
                          FontHelper.class.getClassLoader().getResource("fonts/Segoe-UI.ttf"))
                      .toURI())
              .toString();

      // adiciona a fonte ao banco de dados de fontes da aplicação
      var fontId = QFontDatabase.addApplicationFont(fontPath);

      if (fontId == -1) {
        throw new RuntimeException("Falha ao carregar a fonte.");
      }

      // obtém o nome da família da fonte carregada e define como a fonte base
      var fontFamily = QFontDatabase.applicationFontFamilies(fontId).getFirst();
      baseFont = new QFont(fontFamily);
      baseFont.setHintingPreference(QFont.HintingPreference.PreferNoHinting);
    } catch (Exception e) {
      // registra o erro e utiliza Arial caso azedar
      logger.warning("Erro ao carregar a fonte: " + e.getMessage());
      baseFont = new QFont("Arial");
      baseFont.setHintingPreference(QFont.HintingPreference.PreferNoHinting);
    }
  }

  /**
   * retorna uma fonte configurada com o tamanho especificado.
   *
   * @param size o tamanho da fonte em pontos.
   * @return uma instância de {@code QFont} com o tamanho especificado.
   */
  public static QFont getBaseFont(int size) {
    var font = new QFont(baseFont);
    font.setPointSize(size);
    return font;
  }
}
