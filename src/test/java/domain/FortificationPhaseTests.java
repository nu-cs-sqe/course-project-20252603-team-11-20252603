package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class FortificationPhaseTests {
  // TC1
  @Test
  public void constructor_nullPlayer_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(map);
    assertThrows(
        IllegalArgumentException.class, () -> new FortificationPhase(null, map));
    EasyMock.verify(map);
  }

  // TC2
  @Test
  public void constructor_nullMap_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    EasyMock.replay(player);
    assertThrows(IllegalArgumentException.class, () -> new FortificationPhase(player, null));
    EasyMock.verify(player);
  }

  // TC50
  @Test
  public void skipPhase_calledTwice_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(player, map);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertThrows(IllegalStateException.class, () -> phase.skipPhase());
    EasyMock.verify(player, map);
  }

  // TC49
  @Test
  public void skipPhase_calledAfterMoveTroops_throwsIllegalStateException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertThrows(IllegalStateException.class, () -> phase.skipPhase());
  }

  // TC17
  @Test
  public void moveTroops_calledAfterSkipPhase_throwsIllegalStateException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertThrows(IllegalStateException.class, () -> phase.moveTroops(s, d, 1));
  }

  // TC48
  @Test
  public void skipPhase_freshPhase_movedBecomesTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    map.addTerritory(s);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertTrue(phase.isMoved());
    assertEquals(2, s.getTroopCount());
  }

  // TC35
  @Test
  public void validateMove_nullDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, null, 1));
    EasyMock.verify(player, map, s);
  }

  // TC36
  @Test
  public void validateMove_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(t, t, 1));
    EasyMock.verify(map, t);
  }

  // TC37
  @Test
  public void validateMove_sourceNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player other = new Player("Bob");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(map, s, d);
  }

  // TC38
  @Test
  public void validateMove_destinationNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player other = new Player("Bob");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(other);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(map, s, d);
  }

  // TC39
  @Test
  public void validateMove_neitherOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player other = new Player("Bob");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(map, s, d);
  }

  // TC40
  @Test
  public void validateMove_nIsZero_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 0));
    EasyMock.verify(map, s, d);
  }

  // TC41
  @Test
  public void validateMove_nIsNegative_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, -1));
    EasyMock.verify(map, s, d);
  }

  // TC42
  @Test
  public void validateMove_nEqualsTroopCount_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 2));
    EasyMock.verify(map, s, d);
  }

  // TC43
  @Test
  public void validateMove_movedAlreadyTrue_throwsIllegalStateException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertThrows(IllegalStateException.class, () -> phase.validateMove(s, d, 1));
  }

  // TC44
  @Test
  public void validateMove_noPath_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
  }

  // TC45
  @Test
  public void validateMove_pathThroughEnemyTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player enemy = new Player("Bob");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", enemy, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
  }

  // TC46
  @Test
  public void validateMove_nIsOne_minValid_noExceptionThrown() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertDoesNotThrow(() -> phase.validateMove(s, d, 1));
  }

  // TC47
  @Test
  public void validateMove_nIsTroopCountMinusOne_maxValid_noExceptionThrown() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertDoesNotThrow(() -> phase.validateMove(s, d, 2));
  }

  // TC34
  @Test
  public void validateMove_nullSource_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(null, d, 1));
    EasyMock.verify(player, map, d);
  }

  // TC29
  @Test
  public void findPath_directNeighbors_returnsPathOfSizeTwo() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    List<Territory> path = phase.findPath(s, d);
    assertEquals(2, path.size());
    assertEquals(s, path.get(0));
    assertEquals(d, path.get(1));
  }

  // TC30
  @Test
  public void findPath_twoHopPath_returnsPathOfSizeThree() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", player, 1);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    List<Territory> path = phase.findPath(s, d);
    assertEquals(3, path.size());
    assertEquals(s, path.get(0));
    assertEquals(mid, path.get(1));
    assertEquals(d, path.get(2));
  }

  // TC31
  @Test
  public void findPath_threeHopPath_returnsPathOfSizeFour() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid1 = new Territory("Mid1", player, 1);
    Territory mid2 = new Territory("Mid2", player, 1);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid1);
    map.addTerritory(mid2);
    map.addTerritory(d);
    map.addConnection(s, mid1);
    map.addConnection(mid1, mid2);
    map.addConnection(mid2, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    List<Territory> path = phase.findPath(s, d);
    assertEquals(4, path.size());
    assertEquals(s, path.get(0));
    assertEquals(mid1, path.get(1));
    assertEquals(mid2, path.get(2));
    assertEquals(d, path.get(3));
  }

  // TC32
  @Test
  public void findPath_noPath_returnsEmptyList() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.findPath(s, d).isEmpty());
  }

  // TC33
  @Test
  public void findPath_pathThroughEnemyTerritory_returnsEmptyList() {
    Player player = new Player("Alice");
    Player enemy = new Player("Bob");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", enemy, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.findPath(s, d).isEmpty());
  }

  // TC28
  @Test
  public void findPath_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.findPath(t, t));
    EasyMock.verify(map, t);
  }

  // TC27
  @Test
  public void findPath_nullDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.findPath(s, null));
    EasyMock.verify(player, map, s);
  }

  // TC26
  @Test
  public void findPath_nullSource_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.findPath(null, d));
    EasyMock.verify(player, map, d);
  }

  // TC25
  @Test
  public void isConnected_pathThroughEnemyTerritory_returnsFalse() {
    Player player = new Player("Alice");
    Player enemy = new Player("Bob");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", enemy, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isConnected(s, d));
  }

  // TC24
  @Test
  public void isConnected_noPathBetweenTerritories_returnsFalse() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isConnected(s, d));
  }

  // TC23
  @Test
  public void isConnected_threeHopPathAllOwned_returnsTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid1 = new Territory("Mid1", player, 1);
    Territory mid2 = new Territory("Mid2", player, 1);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid1);
    map.addTerritory(mid2);
    map.addTerritory(d);
    map.addConnection(s, mid1);
    map.addConnection(mid1, mid2);
    map.addConnection(mid2, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
  }

  // TC22
  @Test
  public void isConnected_twoHopPathAllOwned_returnsTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", player, 1);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
  }

  // TC21
  @Test
  public void isConnected_directNeighborsBothOwned_returnsTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
  }

  // TC20
  @Test
  public void isConnected_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.isConnected(t, t));
    EasyMock.verify(map, t);
  }

  // TC19
  @Test
  public void isConnected_nullDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.isConnected(s, null));
    EasyMock.verify(player, map, s);
  }

  // TC18
  @Test
  public void isConnected_nullSource_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.isConnected(null, d));
    EasyMock.verify(player, map, d);
  }

  // TC16
  @Test
  public void moveTroops_calledAfterSuccessfulMove_throwsIllegalStateException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertThrows(IllegalStateException.class, () -> phase.moveTroops(s, d, 1));
  }

  // TC15
  @Test
  public void moveTroops_nIsTroopCountMinusOne_maxValid_troopsTransferredAndMovedIsTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 2);
    assertEquals(1, s.getTroopCount());
    assertEquals(3, d.getTroopCount());
    assertTrue(phase.isMoved());
  }

  // TC14
  @Test
  public void moveTroops_nIsOne_minValid_troopsTransferredAndMovedIsTrue() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    map.addConnection(s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertEquals(1, s.getTroopCount());
    assertEquals(2, d.getTroopCount());
    assertTrue(phase.isMoved());
  }

  // TC13
  @Test
  public void moveTroops_pathThroughEnemyTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player enemy = new Player("Bob");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory mid = new Territory("Mid", enemy, 3);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(mid);
    map.addTerritory(d);
    map.addConnection(s, mid);
    map.addConnection(mid, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
  }

  // TC12
  @Test
  public void moveTroops_noPathBetweenSourceAndDestination_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = new GameMap();
    Territory s = new Territory("S", player, 2);
    Territory d = new Territory("D", player, 1);
    map.addTerritory(s);
    map.addTerritory(d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
  }

  // TC11
  @Test
  public void moveTroops_nEqualsTroopCount_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 2));
    EasyMock.verify(map, s, d);
  }

  // TC10
  @Test
  public void moveTroops_nIsNegative_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, -1));
    EasyMock.verify(map, s, d);
  }

  // TC9
  @Test
  public void moveTroops_nIsZero_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 0));
    EasyMock.verify(map, s, d);
  }

  // TC8
  @Test
  public void moveTroops_destinationNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player other = new Player("Bob");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(other);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(map, s, d);
  }

  // TC7
  @Test
  public void moveTroops_sourceNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Player other = new Player("Bob");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(map, s, d);
  }

  // TC6
  @Test
  public void moveTroops_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(t, t, 1));
    EasyMock.verify(map, t);
  }

  // TC5
  @Test
  public void moveTroops_nullDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, null, 1));
    EasyMock.verify(player, map, s);
  }

  // TC4
  @Test
  public void moveTroops_nullSource_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(null, d, 1));
    EasyMock.verify(player, map, d);
  }

  // TC3
  @Test
  public void constructor_validPlayerAndMap_movedIsFalse() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(player, map);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isMoved());
    EasyMock.verify(player, map);
  }
}
