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

  public void moveTroops(Territory s, Territory d, int n) {
    validateMove(s, d, n);
  }

  public void validateMove(Territory s, Territory d, int n) {
    if (s == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
  }
}
