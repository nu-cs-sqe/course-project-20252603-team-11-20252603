package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TradeBonusTests {

  @Test
  public void constructor_initialValueZero_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new TradeBonus(0, 1));
  }

  @Test
  public void constructor_initialValueOne_constructsSuccessfully() {
    TradeBonus bonus = new TradeBonus(1, 1);
    assertEquals(1, bonus.getValue());
  }

  @Test
  public void constructor_incrementStepZero_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new TradeBonus(1, 0));
  }
}
