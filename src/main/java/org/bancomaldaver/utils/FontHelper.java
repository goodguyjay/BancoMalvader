package org.bancomaldaver.utils;

import io.qt.gui.QFont;
import io.qt.gui.QFontDatabase;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

public final class FontHelper {
  private static final Logger logger = Logger.getLogger(FontHelper.class.getName());
  private static QFont baseFont;

  static {
    try {
      var fontPath =
          Paths.get(
                  Objects.requireNonNull(
                          FontHelper.class.getClassLoader().getResource("fonts/Segoe-UI.ttf"))
                      .toURI())
              .toString();

      var fontId = QFontDatabase.addApplicationFont(fontPath);

      if (fontId == -1) {
        throw new RuntimeException("Falha ao carregar a fonte.");
      }

      var fontFamily = QFontDatabase.applicationFontFamilies(fontId).getFirst();
      baseFont = new QFont(fontFamily);
      baseFont.setHintingPreference(QFont.HintingPreference.PreferNoHinting);
    } catch (Exception e) {
      logger.warning("Erro ao carregar a fonte: " + e.getMessage());
      baseFont = new QFont("Arial");
      baseFont.setHintingPreference(QFont.HintingPreference.PreferNoHinting);
    }
  }

  /**
   * Retorna uma fonte configurada com o tamanho especificado. (Segoe UI)
   *
   * @param size O tamanho da fonte.
   * @return Uma instância do QFont com o tamanho especificado.
   */
  public static QFont getBaseFont(int size) {
    var font = new QFont(baseFont);
    font.setPointSize(size);
    return font;
  }
}
