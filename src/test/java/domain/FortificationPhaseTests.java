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
