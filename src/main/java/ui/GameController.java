package ui;

import domain.AttackPhase;
import domain.CardTradePhase;
import domain.CardTradeValidator;
import domain.FortificationPhase;
import domain.Game;
import domain.GameMap;
import domain.GameState;
import domain.Player;
import domain.ReinforcementPhase;
import domain.RiskCard;
import domain.Territory;
import domain.TradeBonus;
import domain.Turn;
import domain.TurnPhase;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import i18n.Messages;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public final class GameController {

  private static final int INITIAL_TRADE_BONUS = 4;
  private static final int TRADE_BONUS_INCREMENT = 2;

  private final Game game;
  private final TradeBonus tradeBonus;
  private Turn currentTurn;
  private Territory selectedTerritory;
  private Runnable onStateChanged;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Game is the shared aggregate root intentionally stored by reference.")
  public GameController(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null");
    }
    this.game = game;
    this.tradeBonus = new TradeBonus(INITIAL_TRADE_BONUS, TRADE_BONUS_INCREMENT);
    autoPlaceStartingTroops();
    startNextTurn();
  }

  public void setOnStateChanged(Runnable callback) {
    this.onStateChanged = callback;
  }

  public void handleTerritoryClick(Territory territory) {
    if (isGameOver()) {
      return;
    }
    TurnPhase phase = currentTurn.getPhase();
    if (phase == TurnPhase.REINFORCEMENT) {
      handleReinforcementClick(territory);
    } else if (phase == TurnPhase.ATTACK) {
      handleAttackClick(territory);
    } else if (phase == TurnPhase.FORTIFICATION) {
      handleFortificationClick(territory);
    }
  }

  public void endReinforcementPhase() {
    if (currentTurn.getPhase() != TurnPhase.REINFORCEMENT) {
      return;
    }
    if (!currentTurn.getReinforcementPhase().isComplete()) {
      return;
    }
    currentTurn.runReinforcementPhase();
    selectedTerritory = null;
    notifyStateChanged();
  }

  public void endAttackPhase() {
    if (currentTurn.getPhase() != TurnPhase.ATTACK) {
      return;
    }
    currentTurn.getAttackPhase().endPhase();
    currentTurn.runAttackPhase();
    selectedTerritory = null;
    notifyStateChanged();
  }

  public void skipFortification() {
    if (currentTurn.getPhase() != TurnPhase.FORTIFICATION) {
      return;
    }
    currentTurn.getFortificationPhase().skipPhase();
    currentTurn.runFortificationPhase();
    endTurn();
  }

  public void endFortificationPhase() {
    if (currentTurn.getPhase() != TurnPhase.FORTIFICATION) {
      return;
    }
    if (!currentTurn.getFortificationPhase().isComplete()) {
      return;
    }
    currentTurn.runFortificationPhase();
    endTurn();
  }

  public TurnPhase getCurrentPhase() {
    return currentTurn != null ? currentTurn.getPhase() : null;
  }

  public Player getCurrentPlayer() {
    return currentTurn != null ? currentTurn.getCurrentPlayer() : null;
  }

  public int getRemainingTroops() {
    if (currentTurn == null || currentTurn.getPhase() != TurnPhase.REINFORCEMENT) {
      return 0;
    }
    ReinforcementPhase rp = currentTurn.getReinforcementPhase();
    return rp != null ? rp.getRemaining() : 0;
  }

  public boolean isFortificationMoved() {
    if (currentTurn == null || currentTurn.getPhase() != TurnPhase.FORTIFICATION) {
      return false;
    }
    FortificationPhase fp = currentTurn.getFortificationPhase();
    return fp != null && fp.isMoved();
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Territory is the shared aggregate domain object; callers need the live "
          + "reference to highlight selection state on the map.")
  public Territory getSelectedTerritory() {
    return selectedTerritory;
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "GameMap is the shared aggregate root; MapView needs the live reference "
          + "to iterate the same territory objects wired to game state.")
  public GameMap getGameMap() {
    return game.getMap();
  }

  public List<Player> getPlayers() {
    return game.getPlayers();
  }

  public boolean isGameOver() {
    return game.getGameState() == GameState.FINISHED;
  }

  public Optional<Player> getWinner() {
    return game.getWinner();
  }

  // --- Private helpers ---

  private void autoPlaceStartingTroops() {
    for (Player player : game.getPlayers()) {
      List<Territory> owned = new ArrayList<>(player.getTerritories());
      int troops = player.getAvailableTroops();
      for (int i = 0; i < troops; i++) {
        owned.get(i % owned.size()).addTroops(1);
      }
      player.setAvailableTroops(0);
    }
  }

  private void startNextTurn() {
    Player currentPlayer = game.getCurrentActivePlayer();
    if (currentPlayer.getCards().size() >= CardTradePhase.PRE_TURN_THRESHOLD) {
      runMandatoryCardTradeLoop(currentPlayer);
    }
    currentTurn = new Turn(currentPlayer, game, game.getRandom());
    currentTurn.startTurn();
    selectedTerritory = null;
    notifyStateChanged();
  }

  private void endTurn() {
    currentTurn.endTurn();
    currentTurn.getEliminatedDefender().ifPresent(defender -> {
      currentTurn.getCurrentPlayer().inheritCardsFrom(defender);
      // TODO: drive post-elimination trading via UI when >= POST_ELIMINATION_THRESHOLD
    });
    if (!checkAndHandleWinCondition()) {
      startNextTurn();
    }
  }

  private boolean checkAndHandleWinCondition() {
    List<Player> active = game.getPlayers().stream()
        .filter(p -> !p.isEliminated() && p.getTerritoryCount() > 0)
        .collect(Collectors.toList());
    if (active.size() == 1) {
      game.setGameState(GameState.FINISHED);
      game.setWinner(active.get(0));
      notifyStateChanged();
      return true;
    }
    return false;
  }

  private void handleReinforcementClick(Territory territory) {
    if (territory.getOwner() == null) {
      return;
    }
    if (!territory.getOwner().equals(currentTurn.getCurrentPlayer())) {
      return;
    }
    int remaining = currentTurn.getReinforcementPhase().getRemaining();
    Optional<Integer> n = showTroopDialog(remaining);
    if (!n.isPresent()) {
      return;
    }
    try {
      currentTurn.getReinforcementPhase().placeTroops(n.get(), territory);
    } catch (IllegalArgumentException e) {
      showError(e.getMessage());
    }
    notifyStateChanged();
  }

  private void handleAttackClick(Territory territory) {
    Player currentPlayer = currentTurn.getCurrentPlayer();

    if (selectedTerritory == null) {
      if (territory.getOwner() != null
          && territory.getOwner().equals(currentPlayer)
          && territory.getTroopCount() >= 2) {
        selectedTerritory = territory;
        notifyStateChanged();
      }
      return;
    }

    if (territory.equals(selectedTerritory)) {
      selectedTerritory = null;
      notifyStateChanged();
      return;
    }

    Territory src = selectedTerritory;
    Territory dst = territory;
    selectedTerritory = null;

    AttackPhase attackPhase = currentTurn.getAttackPhase();
    if (!attackPhase.canAttack(src, dst)) {
      notifyStateChanged();
      return;
    }

    int maxDice = Math.min(3, src.getTroopCount() - 1);
    Optional<Integer> diceOpt = showDiceDialog(maxDice);
    if (!diceOpt.isPresent()) {
      notifyStateChanged();
      return;
    }
    int dice = diceOpt.get();

    Player previousOwner = dst.getOwner();
    int conqueredBefore = attackPhase.getConqueredCount();
    try {
      attackPhase.declareAttack(src, dst, dice);
      attackPhase.resolveBattle(src, dst, dice);
    } catch (IllegalArgumentException e) {
      showError(e.getMessage());
      notifyStateChanged();
      return;
    }

    if (attackPhase.getConqueredCount() > conqueredBefore) {
      int maxMove = src.getTroopCount() - 1;
      int moveIn = showMoveInDialog(dice, maxMove).orElse(dice);
      try {
        attackPhase.moveInTroops(src, dst, moveIn);
      } catch (IllegalArgumentException ignored) {
        attackPhase.moveInTroops(src, dst, dice);
      }
      if (previousOwner != null && previousOwner.getTerritoryCount() == 0) {
        previousOwner.setEliminated(true);
      }
    }

    notifyStateChanged();
  }

  private void handleFortificationClick(Territory territory) {
    Player currentPlayer = currentTurn.getCurrentPlayer();

    if (selectedTerritory == null) {
      if (territory.getOwner() != null
          && territory.getOwner().equals(currentPlayer)
          && territory.getTroopCount() >= 2) {
        selectedTerritory = territory;
        notifyStateChanged();
      }
      return;
    }

    if (territory.equals(selectedTerritory)) {
      selectedTerritory = null;
      notifyStateChanged();
      return;
    }

    Territory src = selectedTerritory;
    Territory dst = territory;
    selectedTerritory = null;

    if (dst.getOwner() == null || !dst.getOwner().equals(currentPlayer)) {
      notifyStateChanged();
      return;
    }

    FortificationPhase fortPhase = currentTurn.getFortificationPhase();
    if (!fortPhase.isConnected(src, dst)) {
      notifyStateChanged();
      return;
    }

    int maxMove = src.getTroopCount() - 1;
    Optional<Integer> n = showTroopDialog(maxMove);
    if (!n.isPresent()) {
      notifyStateChanged();
      return;
    }

    try {
      fortPhase.moveTroops(src, dst, n.get());
    } catch (IllegalArgumentException | IllegalStateException e) {
      showError(e.getMessage());
    }
    notifyStateChanged();
  }

  private Optional<Integer> showTroopDialog(int max) {
    TextInputDialog dialog = new TextInputDialog("1");
    dialog.setTitle(Messages.get("ui.dialog.troops"));
    dialog.setHeaderText(null);
    dialog.setContentText(Messages.get("ui.dialog.troops") + " (1-" + max + ")");
    Optional<String> result = dialog.showAndWait();
    if (!result.isPresent()) {
      return Optional.empty();
    }
    try {
      int n = Integer.parseInt(result.get().trim());
      if (n >= 1 && n <= max) {
        return Optional.of(n);
      }
      return Optional.empty();
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private Optional<Integer> showDiceDialog(int max) {
    List<Integer> options = new ArrayList<>();
    for (int i = 1; i <= max; i++) {
      options.add(i);
    }
    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(max, options);
    dialog.setTitle(Messages.get("ui.phase.attack"));
    dialog.setHeaderText(null);
    dialog.setContentText(
        Messages.get("ui.dialog.dice").replace("{0}", String.valueOf(max)));
    return dialog.showAndWait();
  }

  private Optional<Integer> showMoveInDialog(int min, int max) {
    TextInputDialog dialog = new TextInputDialog(String.valueOf(min));
    String prompt = Messages.get("ui.dialog.moveIn").replace("{0}", String.valueOf(min));
    dialog.setTitle(prompt);
    dialog.setHeaderText(null);
    dialog.setContentText(prompt + " (max " + max + ")");
    Optional<String> result = dialog.showAndWait();
    if (!result.isPresent()) {
      return Optional.of(min);
    }
    try {
      int n = Integer.parseInt(result.get().trim());
      if (n >= min && n <= max) {
        return Optional.of(n);
      }
      return Optional.of(min);
    } catch (NumberFormatException e) {
      return Optional.of(min);
    }
  }

  private void runMandatoryCardTradeLoop(Player player) {
    CardTradePhase phase = new CardTradePhase(
        player, tradeBonus, true, new CardTradeValidator());
    while (!phase.isComplete()) {
      List<RiskCard> picks = pickTradeCards(player);
      if (!phase.validateSet(picks)) {
        showTradeInvalidAlert();
        continue;
      }
      for (RiskCard card : picks) {
        player.removeCard(card);
      }
      phase.run();
      phase = new CardTradePhase(player, tradeBonus, true, new CardTradeValidator());
    }
  }

  private List<RiskCard> pickTradeCards(Player player) {
    List<RiskCard> picks = new ArrayList<>();
    List<RiskCard> remaining = new ArrayList<>(player.getCards());
    for (int pick = 0; pick < 3; pick++) {
      picks.add(showCardPickDialog(remaining));
      remaining.remove(picks.get(pick));
    }
    return picks;
  }

  private RiskCard showCardPickDialog(List<RiskCard> choices) {
    RiskCard result = null;
    while (result == null) {
      ChoiceDialog<RiskCard> dialog = new ChoiceDialog<>(choices.get(0), choices);
      dialog.setTitle(Messages.get("ui.dialog.cardTrade.title"));
      dialog.setHeaderText(Messages.get("ui.dialog.cardTrade.header"));
      result = dialog.showAndWait().orElse(null);
    }
    return result;
  }

  private void showTradeInvalidAlert() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(Messages.get("ui.dialog.cardTrade.title"));
    alert.setHeaderText(null);
    alert.setContentText(Messages.get("ui.dialog.cardTrade.invalid"));
    alert.showAndWait();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Invalid Action");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void notifyStateChanged() {
    if (onStateChanged != null) {
      onStateChanged.run();
    }
  }
}
