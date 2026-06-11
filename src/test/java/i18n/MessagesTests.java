package i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class MessagesTests {

  private static final Locale SPANISH = new Locale("es");

  @AfterEach
  public void resetLocale() {
    Messages.setLocale(Locale.ENGLISH);
  }

  @Test
  public void get_englishLocale_returnsEnglishLabel() {
    Messages.setLocale(Locale.ENGLISH);
    assertEquals("Skip", Messages.get("ui.button.skip"));
    assertEquals("Attack", Messages.get("ui.phase.attack"));
  }

  @Test
  public void setLocale_spanish_getReturnsSpanishLabel() {
    Messages.setLocale(SPANISH);
    assertEquals("Omitir", Messages.get("ui.button.skip"));
    assertEquals("Ataque", Messages.get("ui.phase.attack"));
  }

  @Test
  public void setLocale_switchedBackToEnglish_getReturnsEnglishLabel() {
    Messages.setLocale(SPANISH);
    Messages.setLocale(Locale.ENGLISH);
    assertEquals("Skip", Messages.get("ui.button.skip"));
  }

  @Test
  public void constructor_isPrivateAndInvocable() throws Exception {
    Constructor<Messages> constructor = Messages.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}
