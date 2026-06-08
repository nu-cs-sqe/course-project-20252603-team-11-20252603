package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

public class FortificationPhase {
  private final Player player;
  private final ConnectivityGraph connectivity;
  private boolean moved;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification = "Player is an aggregate domain object intentionally shared by reference; "
          + "the phase needs to read the same instance as the rest of the Turn. "
          + "Class is non-final because EasyMock subclasses it to mock in tests."
  )
  public FortificationPhase(Player player, GameMap map) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    this.player = player;
    this.connectivity = new ConnectivityGraph(map);
  }

  public boolean isMoved() {
    return moved;
  }

  public boolean isComplete() {
    return moved;
  }

  public void moveTroops(Territory s, Territory d, int n) {
    validateMove(s, d, n);
    s.removeTroops(n);
    d.addTroops(n);
    moved = true;
  }

  public void validateMove(Territory s, Territory d, int n) {
    if (moved) {
      throw new IllegalStateException("Fortification phase already completed");
    }
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
    if (n >= s.getTroopCount()) {
      throw new IllegalArgumentException("Source must retain at least 1 troop");
    }
    if (!isConnected(s, d)) {
      throw new IllegalArgumentException("No player-owned path between source and destination");
    }
  }

  public void skipPhase() {
    if (moved) {
      throw new IllegalStateException("Fortification phase already completed");
    }
    moved = true;
  }

  public boolean isConnected(Territory s, Territory d) {
    if (s == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
    if (d == null) {
      throw new IllegalArgumentException("Destination territory cannot be null");
    }
    if (s == d) {
      throw new IllegalArgumentException("Source and destination cannot be the same territory");
    }
    return connectivity.isConnected(s, d, player);
  }

  public List<Territory> findPath(Territory s, Territory d) {
    if (s == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
    return connectivity.findPath(s, d, player);
  }
}
