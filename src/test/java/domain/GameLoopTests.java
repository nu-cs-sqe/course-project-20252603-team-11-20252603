package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameLoopTests {

  private void recordTurnDelegation(Turn turn) {
    EasyMock.expect(turn.getEliminatedDefender()).andReturn(Optional.empty());
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();
    turn.endTurn();
  }

  private void recordStandardTurnSetup(
      Game game, Player currentPlayer, Player otherPlayer, Turn turn, Random random) {
    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(currentPlayer);
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(currentPlayer.getCards()).andReturn(List.of());
    EasyMock.expect(currentPlayer.calculateReinforcements()).andReturn(3);
    currentPlayer.setAvailableTroops(3);
    EasyMock.expect(game.getPlayers())
        .andReturn(List.of(currentPlayer, otherPlayer))
        .anyTimes();
    EasyMock.expect(currentPlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(currentPlayer.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(otherPlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(otherPlayer.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
  }

  @Test
  public void constructor_nullGame_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new GameLoop(null));
  }

  @Test
  public void constructor_validGame_succeeds() {
    Game game = EasyMock.createMock(Game.class);
    EasyMock.replay(game);
    assertNotNull(new GameLoop(game));
    EasyMock.verify(game);
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

    game.setGameState(GameState.FINISHED);
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

  @Test
  public void runNextTurn_currentPlayerActive_createsTurnForCurrentPlayer() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    recordStandardTurnSetup(game, player0, player1, turn, random);
    EasyMock.replay(game, player0, player1, turn, random);

    final Player[] turnPlayer = new Player[1];
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            turnPlayer[0] = currentPlayer;
            assertSame(game, g);
            assertSame(random, r);
            return turn;
          }
        };

    gameLoop.runNextTurn();

    assertSame(player0, turnPlayer[0]);
    EasyMock.verify(game, player0, player1, turn, random);
  }

  @Test
  public void runNextTurn_currentPlayerEliminated_skipsToNextActivePlayer() {
    Game game = EasyMock.createMock(Game.class);
    Player player1 = EasyMock.createMock(Player.class);
    Player player2 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(player1);
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(player1.getCards()).andReturn(List.of());
    EasyMock.expect(player1.calculateReinforcements()).andReturn(3);
    player1.setAvailableTroops(3);
    EasyMock.expect(game.getPlayers()).andReturn(List.of(player1, player2)).anyTimes();
    EasyMock.expect(player1.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player1.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(player2.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player2.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
    EasyMock.replay(game, player1, player2, turn, random);

    final Player[] turnPlayer = new Player[1];
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            turnPlayer[0] = currentPlayer;
            return turn;
          }
        };

    gameLoop.runNextTurn();

    assertSame(player1, turnPlayer[0]);
    EasyMock.verify(game, player1, player2, turn, random);
  }

  @Test
  public void runNextTurn_twoConsecutiveEliminated_skipsToThirdActivePlayer() {
    Game game = EasyMock.createMock(Game.class);
    Player player3 = EasyMock.createMock(Player.class);
    Player other = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(player3);
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(player3.getCards()).andReturn(List.of());
    EasyMock.expect(player3.calculateReinforcements()).andReturn(3);
    player3.setAvailableTroops(3);
    EasyMock.expect(game.getPlayers()).andReturn(List.of(player3, other)).anyTimes();
    EasyMock.expect(player3.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player3.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(other.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(other.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
    EasyMock.replay(game, player3, other, turn, random);

    final Player[] turnPlayer = new Player[1];
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            turnPlayer[0] = currentPlayer;
            return turn;
          }
        };

    gameLoop.runNextTurn();

    assertSame(player3, turnPlayer[0]);
    EasyMock.verify(game, player3, other, turn, random);
  }

  @Test
  public void runNextTurn_calculateReinforcements_calledBeforeTurnStarts() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(player0);
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(player0.getCards()).andReturn(List.of());
    EasyMock.expect(player0.calculateReinforcements()).andReturn(7);
    player0.setAvailableTroops(7);
    EasyMock.expect(game.getPlayers())
        .andReturn(List.of(player0, player1))
        .anyTimes();
    EasyMock.expect(player0.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player0.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(player1.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player1.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
    EasyMock.replay(game, player0, player1, turn, random);

    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }
        };

    gameLoop.runNextTurn();

    EasyMock.verify(game, player0, player1, turn, random);
  }

  @Test
  public void runNextTurn_delegatesFullTurnLifecycle() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    recordStandardTurnSetup(game, player0, player1, turn, random);
    EasyMock.replay(game, player0, player1, turn, random);

    final int[] createTurnCalls = {0};
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            createTurnCalls[0]++;
            assertSame(player0, currentPlayer);
            assertSame(game, g);
            assertSame(random, r);
            return turn;
          }
        };

    gameLoop.runNextTurn();

    assertEquals(1, createTurnCalls[0]);
    EasyMock.verify(game, player0, player1, turn, random);
  }

  @Test
  public void runNextTurn_calledTwice_createsDistinctTurnInstances() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn1 = EasyMock.createMock(Turn.class);
    Turn turn2 = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(player0).times(2);
    EasyMock.expect(game.getRandom()).andReturn(random).times(2);
    EasyMock.expect(player0.getCards()).andReturn(List.of()).times(2);
    EasyMock.expect(player0.calculateReinforcements()).andReturn(3).times(2);
    player0.setAvailableTroops(3);
    EasyMock.expectLastCall().times(2);
    EasyMock.expect(game.getPlayers()).andReturn(List.of(player0, player1)).anyTimes();
    EasyMock.expect(player0.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player0.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(player1.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player1.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn1);
    recordTurnDelegation(turn2);
    EasyMock.replay(game, player0, player1, turn1, turn2, random);

    final int[] createTurnCalls = {0};
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            createTurnCalls[0]++;
            return createTurnCalls[0] == 1 ? turn1 : turn2;
          }
        };

    gameLoop.runNextTurn();
    gameLoop.runNextTurn();

    assertEquals(2, createTurnCalls[0]);
    EasyMock.verify(game, player0, player1, turn1, turn2, random);
  }

  @Test
  public void runNextTurn_noElimination_checkWinConditionReturnsFalse() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    recordStandardTurnSetup(game, player0, player1, turn, random);
    EasyMock.replay(game, player0, player1, turn, random);

    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }
        };

    gameLoop.runNextTurn();

    EasyMock.verify(game, player0, player1, turn, random);
  }

  @Test
  public void runNextTurn_defenderEliminated_inheritsCardsFromDefender() {
    Game game = EasyMock.createMock(Game.class);
    Player attacker = EasyMock.createMock(Player.class);
    Player otherPlayer = EasyMock.createMock(Player.class);
    Player defender = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(attacker);
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(attacker.getCards()).andReturn(List.of());
    EasyMock.expect(attacker.getCards()).andReturn(List.of());
    EasyMock.expect(attacker.calculateReinforcements()).andReturn(3);
    attacker.setAvailableTroops(3);
    EasyMock.expect(game.getPlayers())
        .andReturn(List.of(attacker, otherPlayer, defender))
        .anyTimes();
    EasyMock.expect(attacker.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(attacker.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(otherPlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(otherPlayer.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(defender.isEliminated()).andReturn(true).anyTimes();
    EasyMock.expect(defender.getTerritoryCount()).andReturn(0).anyTimes();
    EasyMock.expect(turn.getEliminatedDefender()).andReturn(Optional.of(defender));
    attacker.inheritCardsFrom(defender);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();
    turn.endTurn();
    EasyMock.replay(game, attacker, otherPlayer, defender, turn, random);

    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }
        };

    gameLoop.runNextTurn();

    EasyMock.verify(game, attacker, otherPlayer, defender, turn, random);
  }
}
