package domain;

public class FortificationPhase {
  private final Player player;
  private boolean moved;

  public FortificationPhase(Player player, GameMap map) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    this.player = player;
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
    if (d == null) {
      throw new IllegalArgumentException("Destination territory cannot be null");
    }
    if (s == d) {
      throw new IllegalArgumentException("Source and destination cannot be the same territory");
    }
    if (s.getOwner() != player) {
      throw new IllegalArgumentException("Source territory must be owned by the player");
    }
    if (d.getOwner() != player) {
      throw new IllegalArgumentException("Destination territory must be owned by the player");
    }
    if (n < 1) {
      throw new IllegalArgumentException("Number of troops must be at least 1");
    }
  }
}
