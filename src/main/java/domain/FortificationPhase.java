package domain;

public class FortificationPhase {
  private boolean moved;

  public FortificationPhase(Player player, GameMap map) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
  }

  public boolean isMoved() {
    return moved;
  }
}
