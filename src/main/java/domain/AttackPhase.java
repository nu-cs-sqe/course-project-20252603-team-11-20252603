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
