package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ConnectivityGraph {
  private final GameMap map;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification = "GameMap is a shared aggregate domain object; ConnectivityGraph reads graph "
          + "state from the same instance as the rest of the domain. "
          + "Class is non-final because EasyMock subclasses it to mock in tests."
  )
  public ConnectivityGraph(GameMap map) {
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    this.map = map;
  }

  public boolean isConnected(Territory s, Territory d, Player owner) {
    if (s == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
    if (d == null) {
      throw new IllegalArgumentException("Destination territory cannot be null");
    }
    if (s == d) {
      throw new IllegalArgumentException("Source and destination cannot be the same territory");
    }
    return !findPath(s, d, owner).isEmpty();
  }

  public List<Territory> findPath(Territory s, Territory d, Player owner) {
    return map.findPath(s, d, owner);
  }

  public Set<Territory> getReachable(Territory src, Player owner) {
    if (src == null) {
      throw new IllegalArgumentException("Source territory cannot be null");
    }
    if (owner == null) {
      throw new IllegalArgumentException("Owner cannot be null");
    }
    Set<Territory> visited = new HashSet<>();
    Queue<Territory> queue = new LinkedList<>();
    if (src.getOwner() == owner) {
      visited.add(src);
      queue.add(src);
    }
    while (!queue.isEmpty()) {
      Territory current = queue.poll();
      for (Territory neighbor : map.getNeighbors(current)) {
        if (!visited.contains(neighbor) && neighbor.getOwner() == owner) {
          visited.add(neighbor);
          queue.add(neighbor);
        }
      }
    }
    return visited;
  }
}
