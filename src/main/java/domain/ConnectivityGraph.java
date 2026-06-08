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
}
