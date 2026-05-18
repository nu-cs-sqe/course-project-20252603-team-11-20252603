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
    return phase;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Game getGame() {
    return game;
  }

  public boolean hasConqueredThisTurn() {
    return conqueredThisTurn;
  }

  public ReinforcementPhase getReinforcementPhase() {
    return reinforcementPhase;
  }

  public AttackPhase getAttackPhase() {
    return attackPhase;
  }

  public FortificationPhase getFortificationPhase() {
    return fortificationPhase;
  }

  public void startTurn() {
    if (phase != null) {
      throw new IllegalStateException("Turn already started");
    }
    currentPlayer.setAvailableTroops(currentPlayer.calculateReinforcements());
    reinforcementPhase = createReinforcementPhase(currentPlayer);
    phase = TurnPhase.REINFORCEMENT;
  }

  public void runReinforcementPhase() {
    if (phase != TurnPhase.REINFORCEMENT) {
      throw new IllegalStateException("Not in REINFORCEMENT phase.");
    }
    if (!reinforcementPhase.isComplete()) {
      throw new IllegalStateException("Reinforcement phase not complete.");
    }
    attackPhase = createAttackPhase(currentPlayer, game, random);
    phase = TurnPhase.ATTACK;
  }

  public void runAttackPhase() {
    if (phase != TurnPhase.ATTACK) {
      throw new IllegalStateException("Not in ATTACK phase.");
    }
    if (!attackPhase.isEnded()) {
      throw new IllegalStateException("Attack phase not ended.");
    }
    conqueredThisTurn = attackPhase.getConqueredCount() > 0;
    fortificationPhase = createFortificationPhase(currentPlayer, game);
    phase = TurnPhase.FORTIFICATION;
  }

  protected ReinforcementPhase createReinforcementPhase(Player p) {
    return new ReinforcementPhase(p);
  }

  protected AttackPhase createAttackPhase(Player p, Game g, Random r) {
    return new AttackPhase(p, g, r);
  }

  protected FortificationPhase createFortificationPhase(Player p, Game g) {
    return new FortificationPhase(p, g);
  }
}
