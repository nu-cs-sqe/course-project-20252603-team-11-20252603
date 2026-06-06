package domain;

import java.util.List;

public class CardTradePhase {
  private final Player player;
  private final TradeBonus tradeBonus;
  private final boolean mandatory;
  private final CardTradeValidator validator;

  public CardTradePhase(Player player, TradeBonus tradeBonus, boolean mandatory) {
    this.player = player;
    this.tradeBonus = tradeBonus;
    this.mandatory = mandatory;
    this.validator = new CardTradeValidator();
  }

  public boolean isComplete() {
    return !mandatory || !validator.mustTrade(player);
  }

  public boolean validateSet(List<RiskCard> cards) {
    return false;
  }

  public void run() {
  }
}
