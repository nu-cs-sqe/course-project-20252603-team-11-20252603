package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameMapTests {
  // ! Constructor tests
  @Test
  public void constructor_emptyMap_initialStateIsCorrect() {
    GameMap map = new GameMap();
    assertTrue(map.getTerritories().isEmpty());
  }

  // ! addTerritory tests
  @Test
  public void addTerritory_nullTerritory_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    assertThrows(IllegalArgumentException.class, () -> map.addTerritory(null));
  }

  @Test
  public void addTerritory_emptyMap_sizeBecomesOne() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);
    map.addTerritory(t1);

    assertEquals(1, map.getTerritories().size());
    assertTrue(map.getTerritories().contains(t1));
    EasyMock.verify(t1);
  }

  @Test
  public void addTerritory_oneExisting_sizeBecomesTwo() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);

    map.addTerritory(t1);
    assertEquals(1, map.getTerritories().size());
    assertTrue(map.getTerritories().contains(t1));

    map.addTerritory(t2);
    assertEquals(2, map.getTerritories().size());
    assertTrue(map.getTerritories().contains(t2));

    EasyMock.verify(t1, t2);
  }

  @Test
  public void addTerritory_duplicateTerritory_sizeRemainsOne() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);

    map.addTerritory(t1);
    map.addTerritory(t1);

    assertEquals(1, map.getTerritories().size());
    EasyMock.verify(t1);
  }

  // ! addConnection tests
  @Test
  public void addConnection_nullA_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);
    map.addTerritory(t);

    assertThrows(IllegalArgumentException.class, () -> map.addConnection(null, t));
    EasyMock.verify(t);
  }

  @Test
  public void addConnection_nullB_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);
    map.addTerritory(t);

    assertThrows(IllegalArgumentException.class, () -> map.addConnection(t, null));
    EasyMock.verify(t);
  }

  @Test
  public void addConnection_selfConnection_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);
    map.addTerritory(t1);

    assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t1));
    EasyMock.verify(t1);
  }

  @Test
  public void addConnection_aNotInMap_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t2);

    assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void addConnection_bNotInMap_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);

    assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void addConnection_validPair_doesNotThrow() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);

    assertDoesNotThrow(() -> map.addConnection(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void addConnection_duplicatePair_isNoOp() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);

    map.addConnection(t1, t2);
    assertDoesNotThrow(() -> map.addConnection(t1, t2));
    assertEquals(1, map.getNeighbors(t1).size());

    EasyMock.verify(t1, t2);
  }

  @Test
  public void addConnection_duplicateReversedPair_isNoOp() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);

    map.addConnection(t1, t2);
    assertDoesNotThrow(() -> map.addConnection(t2, t1));
    assertEquals(1, map.getNeighbors(t1).size());

    EasyMock.verify(t1, t2);
  }

  // ! getNeighbors tests
  @Test
  public void getNeighbors_nullTerritory_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    assertThrows(IllegalArgumentException.class, () -> map.getNeighbors(null));
  }

  @Test
  public void getNeighbors_territoryNotInMap_returnsEmptyList() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);

    assertTrue(map.getNeighbors(t1).isEmpty());
    EasyMock.verify(t1);
  }

  @Test
  public void getNeighbors_territoryWithNoConnections_returnsEmptyList() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);
    map.addTerritory(t1);

    assertTrue(map.getNeighbors(t1).isEmpty());
    EasyMock.verify(t1);
  }

  @Test
  public void getNeighbors_oneNeighbor_returnsSingletonList() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addConnection(t1, t2);

    assertEquals(1, map.getNeighbors(t1).size());
    assertTrue(map.getNeighbors(t1).contains(t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void getNeighbors_multipleNeighbors_returnsAllNeighbors() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);
    Territory t3 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2, t3);
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addTerritory(t3);
    map.addConnection(t1, t2);
    map.addConnection(t1, t3);

    assertEquals(2, map.getNeighbors(t1).size());
    assertTrue(map.getNeighbors(t1).contains(t2));
    assertTrue(map.getNeighbors(t1).contains(t3));
    EasyMock.verify(t1, t2, t3);
  }

  // ! areAdjacent tests
  @Test
  public void areAdjacent_nullA_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);
    map.addTerritory(t);

    assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(null, t));
    EasyMock.verify(t);
  }

  @Test
  public void areAdjacent_nullB_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);
    map.addTerritory(t);

    assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t, null));
    EasyMock.verify(t);
  }

  @Test
  public void areAdjacent_aNotInMap_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t2);

    assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void areAdjacent_bNotInMap_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);

    assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void areAdjacent_notConnected_returnsFalse() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);

    assertFalse(map.areAdjacent(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void areAdjacent_connectedAB_returnsTrue() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addConnection(t1, t2);

    assertTrue(map.areAdjacent(t1, t2));
    EasyMock.verify(t1, t2);
  }

  @Test
  public void areAdjacent_connectedAB_reverseQuery_returnsTrue() {
    GameMap map = new GameMap();
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);
    map.addTerritory(t1);
    map.addTerritory(t2);
    map.addConnection(t1, t2);

    assertTrue(map.areAdjacent(t2, t1));
    EasyMock.verify(t1, t2);
  }

  // ! findPath tests
  @Test
  public void findPath_nullSource_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, d);
    assertThrows(IllegalArgumentException.class, () -> map.findPath(null, d, player));
    EasyMock.verify(player, d);
  }

  @Test
  public void findPath_nullDestination_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, s);
    assertThrows(IllegalArgumentException.class, () -> map.findPath(s, null, player));
    EasyMock.verify(player, s);
  }

  @Test
  public void findPath_sourceEqualsDestination_throwsIllegalArgumentException() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, t);
    assertThrows(IllegalArgumentException.class, () -> map.findPath(t, t, player));
    EasyMock.verify(player, t);
  }

  @Test
  public void findPath_directNeighbors_returnsPathOfSizeTwo() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, s, d);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    List<Territory> path = map.findPath(s, d, player);
    assertEquals(List.of(s, d), path);
    EasyMock.verify(player, s, d);
  }

  @Test
  public void findPath_twoHopPath_returnsPathOfSizeThree() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory mid = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(mid.getOwner()).andReturn(player);
    EasyMock.replay(player, s, mid, d);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    List<Territory> path = map.findPath(s, d, player);
    assertEquals(3, path.size());
    assertEquals(s, path.get(0));
    assertEquals(mid, path.get(1));
    assertEquals(d, path.get(2));
    EasyMock.verify(player, s, mid, d);
  }

  @Test
  public void findPath_threeHopPath_returnsPathOfSizeFour() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory mid1 = EasyMock.createMock(Territory.class);
    Territory mid2 = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(mid1.getOwner()).andReturn(player);
    EasyMock.expect(mid2.getOwner()).andReturn(player);
    EasyMock.replay(player, s, mid1, mid2, d);
    map.addTerritory(s);
    map.addTerritory(mid1);
    map.addTerritory(mid2);
    map.addTerritory(d);
    map.addConnection(s, mid1);
    map.addConnection(mid1, mid2);
    map.addConnection(mid2, d);
    List<Territory> path = map.findPath(s, d, player);
    assertEquals(4, path.size());
    assertEquals(s, path.get(0));
    assertEquals(mid1, path.get(1));
    assertEquals(mid2, path.get(2));
    assertEquals(d, path.get(3));
    EasyMock.verify(player, s, mid1, mid2, d);
  }

  @Test
  public void findPath_noPath_returnsEmptyList() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, s, d);
    map.addTerritory(s);
    map.addTerritory(d);
    assertTrue(map.findPath(s, d, player).isEmpty());
    EasyMock.verify(player, s, d);
  }

  @Test
  public void findPath_pathThroughEnemyTerritory_returnsEmptyList() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory mid = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(mid.getOwner()).andReturn(enemy);
    EasyMock.replay(player, enemy, s, mid, d);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    assertTrue(map.findPath(s, d, player).isEmpty());
    EasyMock.verify(player, enemy, s, mid, d);
  }

  @Test
  public void findPath_directNeighbors_pathStartsAtSourceAndEndsAtDestination() {
    GameMap map = new GameMap();
    Player player = EasyMock.createMock(Player.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, s, d);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    List<Territory> path = map.findPath(s, d, player);
    assertFalse(path.isEmpty());
    assertSame(s, path.get(0));
    assertSame(d, path.get(path.size() - 1));
    EasyMock.verify(player, s, d);
  }
}
