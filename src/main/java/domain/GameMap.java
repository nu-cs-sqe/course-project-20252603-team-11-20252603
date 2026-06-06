package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GameMap {
  private final List<Territory> territories;
  private final Map<Territory, Set<Territory>> adjacency;

  public GameMap() {
    this.territories = new ArrayList<>();
    this.adjacency = new HashMap<>();
  }

  public List<Territory> getTerritories() {
    return Collections.unmodifiableList(territories);
  }

  public void addTerritory(Territory territory) {
    if (territory == null) {
      throw new IllegalArgumentException("Territory cannot be null.");
    }
    if (!territories.contains(territory)) {
      territories.add(territory);
      adjacency.put(territory, new HashSet<>());
    }
  }

  public void addConnection(Territory a, Territory b) {
    if (a == null || b == null) {
      throw new IllegalArgumentException("Territories cannot be null.");
    }
    if (a == b) {
      throw new IllegalArgumentException("Cannot connect a territory to itself.");
    }
    if (!territories.contains(a) || !territories.contains(b)) {
      throw new IllegalArgumentException("Both territories must be in the map.");
    }
    adjacency.get(a).add(b);
    adjacency.get(b).add(a);
  }

  public List<Territory> getNeighbors(Territory territory) {
    if (territory == null) {
      throw new IllegalArgumentException("Territory cannot be null.");
    }
    Set<Territory> neighbors = adjacency.get(territory);
    if (neighbors == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(new ArrayList<>(neighbors));
  }

  public List<Territory> findPath(Territory s, Territory d, Player player) {
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
      for (Territory neighbor : getNeighbors(current)) {
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
        if (!parent.containsKey(neighbor) && neighbor.getOwner() == player) {
          parent.put(neighbor, current);
          queue.add(neighbor);
        }
      }
    }
    return new ArrayList<>();
  }

  public boolean areAdjacent(Territory a, Territory b) {
    if (a == null || b == null) {
      throw new IllegalArgumentException("Territories cannot be null.");
    }
    if (!territories.contains(a) || !territories.contains(b)) {
      throw new IllegalArgumentException("Both territories must be in the map.");
    }
    return adjacency.get(a).contains(b);
  }
}
