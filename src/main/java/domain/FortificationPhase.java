package domain;

public class FortificationPhase {
  public FortificationPhase(Player player, GameMap map) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
  }
}
