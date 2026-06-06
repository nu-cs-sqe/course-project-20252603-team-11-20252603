package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameLoopTests {

  @Test
  public void constructor_nullGame_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new GameLoop(null));
  }

  @Test
  public void constructor_validGame_returnsInjectedGame() {
    Game game = EasyMock.createMock(Game.class);
    GameLoop gameLoop = new GameLoop(game);
    assertSame(game, gameLoop.getGame());
  }
}
