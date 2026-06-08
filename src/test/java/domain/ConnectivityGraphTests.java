package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class ConnectivityGraphTests {

  @Test
  public void constructor_nullMap_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new ConnectivityGraph(null));
  }

  @Test
  public void constructor_validMap_doesNotThrow() {
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(map);
    assertDoesNotThrow(() -> new ConnectivityGraph(map));
    EasyMock.verify(map);
  }

  @Test
  public void isConnected_nullSource_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory d = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.replay(map, d, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertThrows(IllegalArgumentException.class, () -> graph.isConnected(null, d, owner));
    EasyMock.verify(map, d, owner);
  }

  @Test
  public void isConnected_nullDestination_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.replay(map, s, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertThrows(IllegalArgumentException.class, () -> graph.isConnected(s, null, owner));
    EasyMock.verify(map, s, owner);
  }

  @Test
  public void isConnected_sourceEqualsDestination_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.replay(map, t, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertThrows(IllegalArgumentException.class, () -> graph.isConnected(t, t, owner));
    EasyMock.verify(map, t, owner);
  }

  @Test
  public void isConnected_pathExists_returnsTrue() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(map.findPath(s, d, owner)).andReturn(List.of(s, d));
    EasyMock.replay(map, s, d, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertTrue(graph.isConnected(s, d, owner));
    EasyMock.verify(map, s, d, owner);
  }

  @Test
  public void isConnected_noPath_returnsFalse() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(map.findPath(s, d, owner)).andReturn(List.of());
    EasyMock.replay(map, s, d, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertFalse(graph.isConnected(s, d, owner));
    EasyMock.verify(map, s, d, owner);
  }

  @Test
  public void findPath_pathFound_delegatesAndReturnsPath() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(map.findPath(s, d, owner)).andReturn(List.of(s, d));
    EasyMock.replay(map, s, d, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertEquals(List.of(s, d), graph.findPath(s, d, owner));
    EasyMock.verify(map, s, d, owner);
  }

  @Test
  public void findPath_noPath_returnsEmptyList() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(map.findPath(s, d, owner)).andReturn(List.of());
    EasyMock.replay(map, s, d, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertTrue(graph.findPath(s, d, owner).isEmpty());
    EasyMock.verify(map, s, d, owner);
  }

  @Test
  public void getReachable_nullSrc_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.replay(map, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertThrows(IllegalArgumentException.class, () -> graph.getReachable(null, owner));
    EasyMock.verify(map, owner);
  }

  @Test
  public void getReachable_nullOwner_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory src = EasyMock.createMock(Territory.class);
    EasyMock.replay(map, src);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertThrows(IllegalArgumentException.class, () -> graph.getReachable(src, null));
    EasyMock.verify(map, src);
  }

  @Test
  public void getReachable_srcNotOwnedByOwner_returnsEmptySet() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory src = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    EasyMock.expect(src.getOwner()).andReturn(other);
    EasyMock.replay(map, src, owner, other);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertTrue(graph.getReachable(src, owner).isEmpty());
    EasyMock.verify(map, src, owner, other);
  }

  @Test
  public void getReachable_srcOwnedNoNeighbors_returnsSingletonSet() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory src = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(src.getOwner()).andReturn(owner);
    EasyMock.expect(map.getNeighbors(src)).andReturn(List.of());
    EasyMock.replay(map, src, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertEquals(Set.of(src), graph.getReachable(src, owner));
    EasyMock.verify(map, src, owner);
  }

  @Test
  public void getReachable_srcOwnedWithOneOwnedNeighbor_returnsBothTerritories() {
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory src = EasyMock.createMock(Territory.class);
    Territory neighbor = EasyMock.createMock(Territory.class);
    Player owner = EasyMock.createMock(Player.class);
    EasyMock.expect(src.getOwner()).andReturn(owner);
    EasyMock.expect(map.getNeighbors(src)).andReturn(List.of(neighbor));
    EasyMock.expect(neighbor.getOwner()).andReturn(owner);
    EasyMock.expect(map.getNeighbors(neighbor)).andReturn(List.of());
    EasyMock.replay(map, src, neighbor, owner);
    ConnectivityGraph graph = new ConnectivityGraph(map);
    assertEquals(Set.of(src, neighbor), graph.getReachable(src, owner));
    EasyMock.verify(map, src, neighbor, owner);
  }
}
