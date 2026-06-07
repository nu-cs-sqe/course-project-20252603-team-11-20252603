package domain;

import java.util.List;
import java.util.stream.Collectors;

public class GameLoop {
  private final Game game;

  public GameLoop(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
  }

  public boolean checkWinCondition() {
    List<Player> activePlayers =
        game.getPlayers().stream()
            .filter(p -> !p.isEliminated() && p.getTerritoryCount() > 0)
            .collect(Collectors.toList());

    if (activePlayers.size() == 1) {
      game.setGameState(GameState.GAME_OVER);
      game.setWinner(activePlayers.get(0));
      return true;
    }
    return false;
  }

  public void runNextTurn() {
    Player currentPlayer = game.getCurrentActivePlayer();
    CardTradePhase.runIfRequired(currentPlayer, CardTradePhase.PRE_TURN_THRESHOLD);

    int reinforcements = currentPlayer.calculateReinforcements();
    currentPlayer.setAvailableTroops(reinforcements);

    Turn turn = createTurn(currentPlayer, game, game.getRandom());
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();
    turn.endTurn();

    turn.getEliminatedDefender()
        .ifPresent(
            defender -> {
              currentPlayer.inheritCardsFrom(defender);
              CardTradePhase.runIfRequired(
                  currentPlayer, CardTradePhase.POST_ELIMINATION_THRESHOLD);
            });

    checkWinCondition();
  }

  public void start() {
    while (!checkWinCondition()) {
      runNextTurn();
    }
  }

  protected Turn createTurn(Player currentPlayer, Game game, java.util.Random random) {
    return new Turn(currentPlayer, game, random);
  }
}
