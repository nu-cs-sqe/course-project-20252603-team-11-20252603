package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class TradeBonus {
  private static final int MINIMUM_VALUE = 1;
  private static final int MINIMUM_STEP = 1;

  private int currentValue;
  private final int incrementStep;

  @SuppressFBWarnings(
      value = "CT_CONSTRUCTOR_THROW",
      justification = "Guard clauses reject invalid arguments; no partial state is exposed."
  )
  public TradeBonus(int initialValue, int incrementStep) {
    if (initialValue < MINIMUM_VALUE) {
      throw new IllegalArgumentException("initialValue must be at least 1.");
    }
    if (incrementStep < MINIMUM_STEP) {
      throw new IllegalArgumentException("incrementStep must be at least 1.");
    }
    this.currentValue = initialValue;
    this.incrementStep = incrementStep;
  }

  public int getValue() {
    return currentValue;
  }

  public void increment() {
    currentValue += incrementStep;
  }
}
