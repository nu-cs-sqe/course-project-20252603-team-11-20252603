package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
