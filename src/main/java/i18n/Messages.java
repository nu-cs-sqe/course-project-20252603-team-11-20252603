package i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

  private static final ResourceBundle BUNDLE =
      ResourceBundle.getBundle("i18n.messages", Locale.getDefault());

  private Messages() {}

  public static String get(String key) {
    return BUNDLE.getString(key);
  }
}
