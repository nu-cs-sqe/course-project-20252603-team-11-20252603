package domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class TurnTests {
  @Test
  public void constructor_nullPlayer_throwsIllegalArgumentException() {
    Game game  = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    assertThrows(IllegalArgumentException.class, () -> new Turn(null, game, random));
  }

  @Test
  public void constructor_nullGame_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Random random = EasyMock.createMock(Random.class);
    assertThrows(IllegalArgumentException.class, () -> new Turn(player, null, random));
  }
}
