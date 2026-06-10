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

  @Test
  public void constructor_incrementStepOne_constructsSuccessfully() {
    TradeBonus bonus = new TradeBonus(4, 1);
    assertEquals(4, bonus.getValue());
  }

  @Test
  public void getValue_noIncrements_returnsInitialValue() {
    TradeBonus bonus = new TradeBonus(4, 2);
    assertEquals(4, bonus.getValue());
  }

  @Test
  public void getValue_oneIncrement_returnsInitialValuePlusStep() {
    TradeBonus bonus = new TradeBonus(4, 2);
    bonus.increment();
    assertEquals(6, bonus.getValue());
  }

  @Test
  public void increment_calledOnce_increasesValueByStep() {
    TradeBonus bonus = new TradeBonus(4, 2);
    bonus.increment();
    assertEquals(6, bonus.getValue());
  }

  @Test
  public void increment_calledTwice_accumulatesStepTwice() {
    TradeBonus bonus = new TradeBonus(4, 2);
    bonus.increment();
    bonus.increment();
    assertEquals(8, bonus.getValue());
  }
}
