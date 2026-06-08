package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
}
