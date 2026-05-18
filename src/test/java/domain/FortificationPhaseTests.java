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
    assertThrows(IllegalArgumentException.class, () -> new FortificationPhase(null, map));
    EasyMock.verify(map);
  }
}
