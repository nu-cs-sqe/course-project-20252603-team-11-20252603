package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.stream.Collectors;

public class GameLoop {
  private static final int INITIAL_TRADE_BONUS = 4;
  private static final int TRADE_BONUS_INCREMENT = 2;

  private final Game game;
  private final TradeBonus tradeBonus;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification = "Game is the shared aggregate root intentionally stored by reference; "
          + "class is non-final because tests subclass it to override createTurn()."
  )
  public GameLoop(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
    this.tradeBonus = new TradeBonus(INITIAL_TRADE_BONUS, TRADE_BONUS_INCREMENT);
  }

  public boolean checkWinCondition() {
    List<Player> activePlayers =
        game.getPlayers().stream()
            .filter(p -> !p.isEliminated() && p.getTerritoryCount() > 0)
            .collect(Collectors.toList());

    if (activePlayers.size() == 1) {
      game.setGameState(GameState.FINISHED);
      game.setWinner(activePlayers.get(0));
      return true;
    }
    return false;
  }

  public void runNextTurn() {
    Player currentPlayer = game.getCurrentActivePlayer();
    if (currentPlayer.getCards().size() >= CardTradePhase.PRE_TURN_THRESHOLD) {
      createCardTradePhase(currentPlayer, true).run();
    }

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
              if (currentPlayer.getCards().size() >= CardTradePhase.POST_ELIMINATION_THRESHOLD) {
                createCardTradePhase(currentPlayer, true).run();
              }
            });

    checkWinCondition();
  }

  protected CardTradePhase createCardTradePhase(Player player, boolean mandatory) {
    return new CardTradePhase(player, tradeBonus, mandatory, new CardTradeValidator());
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
