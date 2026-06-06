package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class FortificationPhaseTests {
  @Test
  public void constructor_nullPlayer_throwsIllegalArgumentException() {
    GameMap map = EasyMock.createMock(GameMap.class);
    assertThrows(
        IllegalArgumentException.class, () -> new FortificationPhase(null, map));
  }

  @Test
  public void constructor_nullMap_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    assertThrows(IllegalArgumentException.class, () -> new FortificationPhase(player, null));
  }

  @Test
  public void constructor_validPlayerAndMap_movedIsFalse() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(player, map);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isMoved());
    EasyMock.verify(player, map);
  }

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

  @Test
  public void moveTroops_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(t, t, 1));
    EasyMock.verify(player, map, t);
  }

  @Test
  public void moveTroops_sourceNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(player, other, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, other, map, s, d);
  }

  @Test
  public void moveTroops_destinationNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(other);
    EasyMock.replay(player, other, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, other, map, s, d);
  }

  @Test
  public void moveTroops_nIsZero_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 0));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_nIsNegative_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, -1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_nEqualsTroopCount_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 2));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_noPathBetweenSourceAndDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_pathThroughEnemyTerritory_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_nIsOne_minValid_troopsTransferredAndMovedIsTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    s.removeTroops(1);
    d.addTroops(1);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertTrue(phase.isMoved());
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_nIsTroopCountMinusOne_maxValid_troopsTransferredAndMovedIsTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(3);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    s.removeTroops(2);
    d.addTroops(2);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 2);
    assertTrue(phase.isMoved());
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_calledAfterSuccessfulMove_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    s.removeTroops(1);
    d.addTroops(1);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertThrows(IllegalStateException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void moveTroops_calledAfterSkipPhase_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertThrows(IllegalStateException.class, () -> phase.moveTroops(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

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

  @Test
  public void isConnected_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.isConnected(t, t));
    EasyMock.verify(player, map, t);
  }

  @Test
  public void isConnected_directNeighborsBothOwned_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void isConnected_twoHopPathAllOwned_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void isConnected_threeHopPathAllOwned_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertTrue(phase.isConnected(s, d));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void isConnected_noPathBetweenTerritories_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isConnected(s, d));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void isConnected_pathThroughEnemyTerritory_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isConnected(s, d));
    EasyMock.verify(player, map, s, d);
  }

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

  @Test
  public void validateMove_sourceEqualsDestination_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory t = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, t);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(t, t, 1));
    EasyMock.verify(player, map, t);
  }

  @Test
  public void validateMove_sourceNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(player, other, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, other, map, s, d);
  }

  @Test
  public void validateMove_destinationNotOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(other);
    EasyMock.replay(player, other, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, other, map, s, d);
  }

  @Test
  public void validateMove_neitherOwnedByPlayer_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(other);
    EasyMock.replay(player, other, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, other, map, s, d);
  }

  @Test
  public void validateMove_nIsZero_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 0));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_nIsNegative_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, -1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_nEqualsTroopCount_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 2));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_movedAlreadyTrue_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertThrows(IllegalStateException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_noPath_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_pathThroughEnemyTerritory_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of());
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertThrows(IllegalArgumentException.class, () -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_nIsOne_minValid_noExceptionThrown() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertDoesNotThrow(() -> phase.validateMove(s, d, 1));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void validateMove_nIsTroopCountMinusOne_maxValid_noExceptionThrown() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(3);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertDoesNotThrow(() -> phase.validateMove(s, d, 2));
    EasyMock.verify(player, map, s, d);
  }

  @Test
  public void isComplete_freshPhase_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(player, map);
    FortificationPhase phase = new FortificationPhase(player, map);
    assertFalse(phase.isComplete());
    EasyMock.verify(player, map);
  }

  @Test
  public void skipPhase_freshPhase_movedBecomesTrue() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    EasyMock.replay(player, map);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.skipPhase();
    assertTrue(phase.isMoved());
    EasyMock.verify(player, map);
  }

  @Test
  public void skipPhase_calledAfterMoveTroops_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory d = EasyMock.createMock(Territory.class);
    EasyMock.expect(s.getOwner()).andReturn(player);
    EasyMock.expect(d.getOwner()).andReturn(player);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(map.findPath(s, d, player)).andReturn(List.of(s, d));
    s.removeTroops(1);
    d.addTroops(1);
    EasyMock.replay(player, map, s, d);
    FortificationPhase phase = new FortificationPhase(player, map);
    phase.moveTroops(s, d, 1);
    assertThrows(IllegalStateException.class, () -> phase.skipPhase());
    EasyMock.verify(player, map, s, d);
  }

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
}
