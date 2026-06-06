package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
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

  @Test
  public void checkWinCondition_twoActivePlayers_returnsFalse() {
    Game game = EasyMock.createMock(Game.class);
    Player player1 = EasyMock.createMock(Player.class);
    Player player2 = EasyMock.createMock(Player.class);

    EasyMock.expect(game.getPlayers()).andReturn(List.of(player1, player2));
    EasyMock.expect(player1.isEliminated()).andReturn(false);
    EasyMock.expect(player2.isEliminated()).andReturn(false);
    EasyMock.expect(player1.getTerritoryCount()).andReturn(5);
    EasyMock.expect(player2.getTerritoryCount()).andReturn(3);

    EasyMock.replay(game, player1, player2);

    GameLoop gameLoop = new GameLoop(game);
    assertFalse(gameLoop.checkWinCondition());

    EasyMock.verify(game, player1, player2);
  }
}
