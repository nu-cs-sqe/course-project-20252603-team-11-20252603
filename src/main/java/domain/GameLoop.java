package domain;

public class GameLoop {
  private final Game game;

  public GameLoop(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
  }

  public Game getGame() {
    return game;
  }
}
