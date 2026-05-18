package domain;

import java.util.Random;

public class Turn {
  private final Player currentPlayer;
  private final Game game;
  private final Random random;

  private TurnPhase phase;
  private boolean conqueredThisTurn;

  private ReinforcementPhase reinforcementPhase;
  private AttackPhase attackPhase;
  private FortificationPhase fortificationPhase;

  public Turn(Player currentPlayer, Game game, Random random) {
    if (currentPlayer == null) {
      throw new IllegalArgumentException("currentPlayer cannot be null.");
    }
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    if (random == null) {
      throw new IllegalArgumentException("random cannot be null.");
    }
    this.currentPlayer = currentPlayer;
    this.game = game;
    this.random = random;
    this.phase = null;
    this.conqueredThisTurn = false;
  }

  public TurnPhase getPhase() {
    return null;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Game getGame() {
    return game;
  }

  public boolean hasConqueredThisTurn() {
    return false;
  }

  public ReinforcementPhase getReinforcementPhase() {
    return null;
  }

  public AttackPhase getAttackPhase() {
    return null;
  }

  public FortificationPhase getFortificationPhase() {
    return null;
  }

  public void startTurn() {
    if (phase != null) {
      throw new IllegalStateException("Turn already started");
    }
    currentPlayer.setAvailableTroops(currentPlayer.calculateReinforcements());
    reinforcementPhase = createReinforcementPhase(currentPlayer);
    phase = TurnPhase.REINFORCEMENT;
  }



  protected ReinforcementPhase createReinforcementPhase(Player p) {
    return new ReinforcementPhase(p);
  }
}
