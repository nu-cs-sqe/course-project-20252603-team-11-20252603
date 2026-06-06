package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

  private void recordActivePlayerTurnSetup(
      Game game, Player currentPlayer, Player otherPlayer, Turn turn, Random random) {
    recordActivePlayerTurnSetup(game, currentPlayer, otherPlayer, turn, random, List.of());
  }

  private void recordActivePlayerTurnSetup(
      Game game,
      Player currentPlayer,
      Player otherPlayer,
      Turn turn,
      Random random,
      List<RiskCard> currentPlayerCards) {
    EasyMock.expect(game.getCurrentPlayerIndex()).andReturn(0);
    EasyMock.expect(game.getPlayers()).andReturn(List.of(currentPlayer, otherPlayer)).anyTimes();
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(currentPlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(currentPlayer.getCards()).andReturn(currentPlayerCards);
    EasyMock.expect(currentPlayer.calculateReinforcements()).andReturn(3);
    currentPlayer.setAvailableTroops(3);
    EasyMock.expect(currentPlayer.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(otherPlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(otherPlayer.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
  }

  private List<RiskCard> makeCards(int count) {
    List<RiskCard> cards = new java.util.ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(EasyMock.createMock(RiskCard.class));
    }
    return cards;
  }

  private void recordSkippedEliminatedPlayerTurnSetup(
      Game game,
      Player eliminatedPlayer,
      Player activePlayer,
      Turn turn,
      Random random) {
    EasyMock.expect(game.getCurrentPlayerIndex()).andReturn(0);
    EasyMock.expect(game.getPlayers())
        .andReturn(List.of(eliminatedPlayer, activePlayer))
        .anyTimes();
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(eliminatedPlayer.isEliminated()).andReturn(true).anyTimes();
    EasyMock.expect(activePlayer.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(activePlayer.getCards()).andReturn(List.of());
    EasyMock.expect(activePlayer.calculateReinforcements()).andReturn(3);
    activePlayer.setAvailableTroops(3);
    EasyMock.expect(activePlayer.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(eliminatedPlayer.getTerritoryCount()).andReturn(0).anyTimes();
    recordTurnDelegation(turn);
    game.setGameState(GameState.GAME_OVER);
    EasyMock.expectLastCall().once();
    game.setWinner(activePlayer);
    EasyMock.expectLastCall().once();
  }

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

  @Test
  public void runNextTurn_currentPlayerActive_createsTurnForCurrentPlayer() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    recordActivePlayerTurnSetup(game, player0, player1, turn, random);
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
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    recordSkippedEliminatedPlayerTurnSetup(game, player0, player1, turn, random);
    EasyMock.replay(game, player0, player1, turn, random);

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
    EasyMock.verify(game, player0, player1, turn, random);
  }

  @Test
  public void runNextTurn_twoConsecutiveEliminated_skipsToThirdActivePlayer() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Player player2 = EasyMock.createMock(Player.class);
    Player player3 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(game.getCurrentPlayerIndex()).andReturn(0);
    EasyMock.expect(game.getPlayers())
        .andReturn(List.of(player0, player1, player2, player3))
        .anyTimes();
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(player0.isEliminated()).andReturn(true).anyTimes();
    EasyMock.expect(player1.isEliminated()).andReturn(true).anyTimes();
    EasyMock.expect(player2.isEliminated()).andReturn(true).anyTimes();
    EasyMock.expect(player3.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player3.getCards()).andReturn(List.of());
    EasyMock.expect(player3.calculateReinforcements()).andReturn(3);
    player3.setAvailableTroops(3);
    EasyMock.expect(player0.getTerritoryCount()).andReturn(0).anyTimes();
    EasyMock.expect(player1.getTerritoryCount()).andReturn(0).anyTimes();
    EasyMock.expect(player2.getTerritoryCount()).andReturn(0).anyTimes();
    EasyMock.expect(player3.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
    game.setGameState(GameState.GAME_OVER);
    EasyMock.expectLastCall().once();
    game.setWinner(player3);
    EasyMock.expectLastCall().once();

    EasyMock.replay(game, player0, player1, player2, player3, turn, random);

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
    EasyMock.verify(game, player0, player1, player2, player3, turn, random);
  }

  @Test
  public void runNextTurn_fourCards_doesNotRunCardTradePhase() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);
    List<RiskCard> fourCards = makeCards(4);
    fourCards.forEach(EasyMock::replay);

    recordActivePlayerTurnSetup(game, player0, player1, turn, random, fourCards);
    EasyMock.replay(game, player0, player1, turn, random);

    final boolean[] cardTradeCalled = {false};
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }

          @Override
          protected CardTradePhase createCardTradePhase(Player player) {
            cardTradeCalled[0] = true;
            return EasyMock.createMock(CardTradePhase.class);
          }
        };

    gameLoop.runNextTurn();

    assertFalse(cardTradeCalled[0]);
    EasyMock.verify(game, player0, player1, turn, random);
    fourCards.forEach(EasyMock::verify);
  }

  @Test
  public void runNextTurn_fiveCards_runsCardTradePhaseBeforeReinforcement() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);
    CardTradePhase cardTradePhase = EasyMock.createMock(CardTradePhase.class);
    List<RiskCard> fiveCards = makeCards(5);
    fiveCards.forEach(EasyMock::replay);

    recordActivePlayerTurnSetup(game, player0, player1, turn, random, fiveCards);
    cardTradePhase.execute();
    EasyMock.replay(game, player0, player1, turn, random, cardTradePhase);

    final boolean[] cardTradeCalled = {false};
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            assertTrue(cardTradeCalled[0]);
            return turn;
          }

          @Override
          protected CardTradePhase createCardTradePhase(Player player) {
            assertSame(player0, player);
            cardTradeCalled[0] = true;
            return cardTradePhase;
          }
        };

    gameLoop.runNextTurn();

    assertTrue(cardTradeCalled[0]);
    EasyMock.verify(game, player0, player1, turn, random, cardTradePhase);
    fiveCards.forEach(EasyMock::verify);
  }

  @Test
  public void runNextTurn_sixCards_runsCardTradePhaseBeforeReinforcement() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);
    CardTradePhase cardTradePhase = EasyMock.createMock(CardTradePhase.class);
    List<RiskCard> sixCards = makeCards(6);
    sixCards.forEach(EasyMock::replay);

    recordActivePlayerTurnSetup(game, player0, player1, turn, random, sixCards);
    cardTradePhase.execute();
    EasyMock.replay(game, player0, player1, turn, random, cardTradePhase);

    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }

          @Override
          protected CardTradePhase createCardTradePhase(Player player) {
            assertSame(player0, player);
            return cardTradePhase;
          }
        };

    gameLoop.runNextTurn();

    EasyMock.verify(game, player0, player1, turn, random, cardTradePhase);
    sixCards.forEach(EasyMock::verify);
  }

  @Test
  public void runNextTurn_calculateReinforcements_passedToReinforcementPhase() {
    Game game = EasyMock.createMock(Game.class);
    Player player0 = EasyMock.createMock(Player.class);
    Player player1 = EasyMock.createMock(Player.class);
    Turn turn = EasyMock.createMock(Turn.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase reinforcementPhase = EasyMock.createMock(ReinforcementPhase.class);

    EasyMock.expect(game.getCurrentPlayerIndex()).andReturn(0);
    EasyMock.expect(game.getPlayers()).andReturn(List.of(player0, player1)).anyTimes();
    EasyMock.expect(game.getRandom()).andReturn(random);
    EasyMock.expect(player0.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player0.getCards()).andReturn(List.of());
    EasyMock.expect(player0.calculateReinforcements()).andReturn(7);
    player0.setAvailableTroops(7);
    EasyMock.expect(player0.getTerritoryCount()).andReturn(1).anyTimes();
    EasyMock.expect(player1.isEliminated()).andReturn(false).anyTimes();
    EasyMock.expect(player1.getTerritoryCount()).andReturn(1).anyTimes();
    recordTurnDelegation(turn);
    EasyMock.replay(game, player0, player1, turn, random, reinforcementPhase);

    final int[] troopsToPlace = {-1};
    GameLoop gameLoop =
        new GameLoop(game) {
          @Override
          protected ReinforcementPhase createReinforcementPhase(
              Player player, int reinforcements) {
            assertSame(player0, player);
            troopsToPlace[0] = reinforcements;
            return reinforcementPhase;
          }

          @Override
          protected Turn createTurn(Player currentPlayer, Game g, Random r) {
            return turn;
          }
        };

    gameLoop.runNextTurn();

    assertEquals(7, troopsToPlace[0]);
    EasyMock.verify(game, player0, player1, turn, random, reinforcementPhase);
  }
}
