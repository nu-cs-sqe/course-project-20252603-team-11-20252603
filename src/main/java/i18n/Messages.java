package i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

  static ResourceBundle bundle =
      ResourceBundle.getBundle("i18n.messages", Locale.ENGLISH);

  private Messages() {}

  public static void setLocale(Locale locale) {
    bundle = ResourceBundle.getBundle("i18n.messages", locale);
  }

  public static String get(String key) {
    return bundle.getString(key);
  }
}
