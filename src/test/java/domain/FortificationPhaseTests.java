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
