package domain;

import java.util.List;

public class AttackPhase {
  private static final int MIN_ATTACK_DICE = 1;
  private static final int MAX_ATTACK_DICE = 3;
  private static final int MAX_DEFENDER_DICE = 2;
  private static final int MIN_TROOPS_TO_ATTACK = 2;

  private final Player attacker;
  private final DiceRoller diceRoller;
  private final Game game;
  private int conqueredCount = 0;
  private int lastAttackDice = 0;

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

  public void moveInTroops(Territory s, Territory t, int n) {
    if (n < lastAttackDice) {
      throw new IllegalArgumentException(
          "Must move at least as many armies as attacking dice rolled.");
    }
    if (n >= s.getTroopCount()) {
      throw new IllegalArgumentException("Source must retain at least 1 army.");
    }
  }

  public void resolveBattle(Territory s, Territory t, int n) {
    lastAttackDice = n;
    int defenderTroops = t.getTroopCount();
    int defenderDice = Math.min(MAX_DEFENDER_DICE, defenderTroops);
    List<Integer> attackerRoll = diceRoller.rollAttacker(n);
    List<Integer> defenderRoll = diceRoller.rollDefender(defenderDice);
    BattleResult result = diceRoller.compare(attackerRoll, defenderRoll);
    if (result.getAttackerLosses() > 0) {
      s.removeTroops(result.getAttackerLosses());
    }
    if (result.getDefenderLosses() >= defenderTroops) {
      conqueredCount++;
    } else if (result.getDefenderLosses() > 0) {
      t.removeTroops(result.getDefenderLosses());
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
