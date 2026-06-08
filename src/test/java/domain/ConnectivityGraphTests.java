package domain;

import static org.junit.jupiter.api.Assertions.*;

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
}
