package domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameLoop {
  private static final int PRE_TURN_CARD_TRADE_THRESHOLD = 5;
  private static final int POST_ELIMINATION_CARD_TRADE_THRESHOLD = 6;

  private final Game game;

  public GameLoop(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
  }

  public Game getGame() {
    return game;
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
    Player currentPlayer = resolveCurrentPlayer();
    runCardTradeIfNeeded(currentPlayer, PRE_TURN_CARD_TRADE_THRESHOLD);

    int reinforcements = currentPlayer.calculateReinforcements();
    currentPlayer.setAvailableTroops(reinforcements);
    createReinforcementPhase(currentPlayer, reinforcements);

    Turn turn = createTurn(currentPlayer, game, game.getRandom());
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();
    turn.endTurn();

    handleElimination(turn, currentPlayer);
    checkWinCondition();
  }

  protected Player resolveCurrentPlayer() {
    List<Player> players = game.getPlayers();
    int index = game.getCurrentPlayerIndex();
    Player current = players.get(index);
    if (!current.isEliminated()) {
      return current;
    }
    int next = (index + 1) % players.size();
    while (players.get(next).isEliminated()) {
      next = (next + 1) % players.size();
    }
    return players.get(next);
  }

  protected void runCardTradeIfNeeded(Player player, int threshold) {
    if (player.getCards().size() >= threshold) {
      CardTradePhase cardTradePhase = createCardTradePhase(player);
      cardTradePhase.execute();
    }
  }

  protected void handleElimination(Turn turn, Player attacker) {
    Optional<Player> eliminatedDefender = turn.getEliminatedDefender();
    if (eliminatedDefender.isEmpty()) {
      return;
    }
    Player defender = eliminatedDefender.get();
    inheritCards(defender, attacker);
    runCardTradeIfNeeded(attacker, POST_ELIMINATION_CARD_TRADE_THRESHOLD);
  }

  protected void inheritCards(Player defender, Player attacker) {
    for (RiskCard card : defender.getCards()) {
      attacker.addCard(card);
    }
  }

  protected Turn createTurn(Player currentPlayer, Game game, java.util.Random random) {
    return new Turn(currentPlayer, game, random);
  }

  protected CardTradePhase createCardTradePhase(Player player) {
    return new CardTradePhase(player);
  }

  protected ReinforcementPhase createReinforcementPhase(Player player, int reinforcements) {
    return new ReinforcementPhase(player, reinforcements);
  }
}
