package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CardTradePhase {
  public static final int PRE_TURN_THRESHOLD = 5;
  public static final int POST_ELIMINATION_THRESHOLD = 6;

  private final Player player;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "URF_UNREAD_FIELD"},
      justification = "Player is stored for upcoming card-trade implementation; shared domain "
          + "object stored by reference by design."
  )
  public CardTradePhase(Player player) {
    this.player = player;
  }

  public static void runIfRequired(Player player, int threshold) {
    if (player.getCards().size() >= threshold) {
      new CardTradePhase(player).execute();
    }
  }

  public void execute() {
    // TODO: implement in follow-up ticket
  }
}
