package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

  @Test
  public void checkWinCondition_oneActivePlayer_setsGameOverAndWinner() {
    Game game = EasyMock.createMock(Game.class);
    Player winner = EasyMock.createMock(Player.class);
    Player eliminated = EasyMock.createMock(Player.class);

    EasyMock.expect(game.getPlayers()).andReturn(List.of(winner, eliminated));
    EasyMock.expect(winner.isEliminated()).andReturn(false);
    EasyMock.expect(eliminated.isEliminated()).andReturn(true);
    EasyMock.expect(winner.getTerritoryCount()).andReturn(42);

    game.setGameState(GameState.GAME_OVER);
    EasyMock.expectLastCall().once();
    game.setWinner(winner);
    EasyMock.expectLastCall().once();

    EasyMock.replay(game, winner, eliminated);

    GameLoop gameLoop = new GameLoop(game);
    assertTrue(gameLoop.checkWinCondition());

    EasyMock.verify(game, winner, eliminated);
  }

  @Test
  public void checkWinCondition_threeActivePlayers_returnsFalse() {
    Game game = EasyMock.createMock(Game.class);
    Player player1 = EasyMock.createMock(Player.class);
    Player player2 = EasyMock.createMock(Player.class);
    Player player3 = EasyMock.createMock(Player.class);

    EasyMock.expect(game.getPlayers()).andReturn(List.of(player1, player2, player3));
    EasyMock.expect(player1.isEliminated()).andReturn(false);
    EasyMock.expect(player2.isEliminated()).andReturn(false);
    EasyMock.expect(player3.isEliminated()).andReturn(false);
    EasyMock.expect(player1.getTerritoryCount()).andReturn(5);
    EasyMock.expect(player2.getTerritoryCount()).andReturn(3);
    EasyMock.expect(player3.getTerritoryCount()).andReturn(2);

    EasyMock.replay(game, player1, player2, player3);

    GameLoop gameLoop = new GameLoop(game);
    assertFalse(gameLoop.checkWinCondition());

    EasyMock.verify(game, player1, player2, player3);
  }
}
