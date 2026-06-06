package domain;

import java.util.List;

public class CardTradeValidator {
  private static final int TRADE_SET_SIZE = 3;
  private static final int MANDATORY_TRADE_THRESHOLD = 5;

  public boolean isValidSet(List<RiskCard> cards) {
    if (cards.size() != TRADE_SET_SIZE) {
      throw new IllegalArgumentException("A trade set must contain exactly 3 cards.");
    }
    RiskCardType first = cards.get(0).getType();
    boolean allSame = cards.get(1).getType() == first && cards.get(2).getType() == first;
    if (allSame) {
      return true;
    }
    return false;
  }

  public boolean isMandatory(Player player) {
    return false;
  }

  public boolean mustTrade(Player player) {
    return false;
  }
}
