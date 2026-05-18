package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FortificationPhase {
  private final Player player;
  private final GameMap map;
  private boolean moved;

  public FortificationPhase(Player player, GameMap map) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    this.player = player;
    this.map = map;
  }

  public boolean isMoved() {
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
    return !findPath(s, d).isEmpty();
  }

  public List<Territory> findPath(Territory s, Territory d) {
    if (s == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
    if (d == null) {
      throw new IllegalArgumentException("Destination territory cannot be null");
    }
    if (s == d) {
      throw new IllegalArgumentException("Source and destination cannot be the same territory");
    }
    Map<Territory, Territory> parent = new HashMap<>();
    Queue<Territory> queue = new LinkedList<>();
    queue.add(s);
    parent.put(s, null);
    while (!queue.isEmpty()) {
      Territory current = queue.poll();
      for (Territory neighbor : map.getNeighbors(current)) {
        if (neighbor == d) {
          List<Territory> path = new ArrayList<>();
          path.add(d);
          Territory cur = current;
          while (cur != null) {
            path.add(0, cur);
            cur = parent.get(cur);
          }
          return path;
        }
        if (neighbor.getOwner() == player && !parent.containsKey(neighbor)) {
          parent.put(neighbor, current);
          queue.add(neighbor);
        }
      }
    }
    return new ArrayList<>();
  }
}
