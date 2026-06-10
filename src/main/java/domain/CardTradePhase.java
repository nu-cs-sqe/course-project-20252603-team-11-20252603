package domain;

import java.util.List;

public class CardTradePhase {
  public static final int PRE_TURN_THRESHOLD = 5;
  public static final int POST_ELIMINATION_THRESHOLD = 6;

  private final Player player;
  private final TradeBonus tradeBonus;
  private final boolean mandatory;
  private final CardTradeValidator validator;

  public CardTradePhase(Player player, TradeBonus tradeBonus, boolean mandatory,
      CardTradeValidator validator) {
    this.player = player;
    this.tradeBonus = tradeBonus;
    this.mandatory = mandatory;
    this.validator = validator;
  }

  public boolean isComplete() {
    return !mandatory || !validator.mustTrade(player);
  }

  public boolean validateSet(List<RiskCard> cards) {
    return validator.isValidSet(cards);
  }

  public void run() {
    player.setAvailableTroops(player.getAvailableTroops() + tradeBonus.getValue());
    tradeBonus.increment();
  }
}
