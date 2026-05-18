package domain;

public class AttackPhase {
  private static final int MIN_ATTACK_DICE = 1;
  private static final int MAX_ATTACK_DICE = 3;
  private static final int MIN_TROOPS_TO_ATTACK = 2;

  private final Player attacker;
  private final DiceRoller diceRoller;
  private final Game game;
  private int conqueredCount = 0;

  public AttackPhase(Player attacker, DiceRoller diceRoller, Game game) {
    this.attacker = attacker;
    this.diceRoller = diceRoller;
    this.game = game;
  }

  public int getConqueredCount() {
    return conqueredCount;
  }

  public void declareAttack(Territory s, Territory t, int n) {
    if (n < MIN_ATTACK_DICE || n > MAX_ATTACK_DICE) {
      throw new IllegalArgumentException("Attacker must roll between 1 and 3 dice.");
    }
    if (!s.getOwner().equals(attacker)) {
      throw new IllegalArgumentException("Source territory must be owned by the attacker.");
    }
    if (t.getOwner().equals(attacker)) {
      throw new IllegalArgumentException("Target territory must not be owned by the attacker.");
    }
    if (s.getTroopCount() <= n) {
      throw new IllegalArgumentException("Source must have more armies than dice rolled.");
    }
    if (!game.getMap().areAdjacent(s, t)) {
      throw new IllegalArgumentException("Source and target territories must be adjacent.");
    }
  }

  public boolean canAttack(Territory s, Territory t) {
    if (!s.getOwner().equals(attacker)) {
      return false;
    }
    if (s.getTroopCount() < MIN_TROOPS_TO_ATTACK) {
      return false;
    }
    if (!game.getMap().areAdjacent(s, t)) {
      return false;
    }
    return !t.getOwner().equals(attacker);
  }
}
