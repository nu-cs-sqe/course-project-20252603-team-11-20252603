package domain;

import java.util.Random;

public class Turn {
  private final Player currentPlayer;
  private final Game game;
  private final Random random;

  public Turn(Player currentPlayer, Game game, Random random) {
    if (currentPlayer == null) {
      throw new IllegalArgumentException("currentPlayer cannot be null.");
    }
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.currentPlayer = currentPlayer;
    this.game = game;
    this.random = random;
  }
}
