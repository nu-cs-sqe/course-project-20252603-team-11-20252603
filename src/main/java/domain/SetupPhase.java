package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SetupPhase {
  private final Game game;
  private final int playerCount;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification = "Game is the shared aggregate root intentionally stored by reference; "
          + "class is non-final because tests subclass it."
  )
  public SetupPhase(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
    this.playerCount = game.getPlayerCount();
  }

  public int getPlayerCount() {
    return playerCount;
  }
}
