package domain;

import static org.junit.jupiter.api.Assertions.*;

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
