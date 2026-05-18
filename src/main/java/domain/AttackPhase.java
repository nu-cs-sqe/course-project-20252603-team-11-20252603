package domain;

public class AttackPhase {
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

  public boolean canAttack(Territory s, Territory t) {
    if (!s.getOwner().equals(attacker)) {
      return false;
    }
    return s.getTroopCount() >= 2;
  }
}
