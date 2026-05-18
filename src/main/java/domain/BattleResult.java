package domain;

import java.util.List;

public class BattleResult {
  private final int attackerLosses;
  private final int defenderLosses;
  private final boolean conquered;

  public BattleResult(List<Integer> attackerDice, List<Integer> defenderDice, boolean conquered) {
    this.conquered = conquered;
    int comparisons = Math.min(attackerDice.size(), defenderDice.size());
    int attackLoss = 0;
    int defendLoss = 0;
    for (int i = 0; i < comparisons; i++) {
      if (attackerDice.get(i) > defenderDice.get(i)) {
        defendLoss++;
      } else {
        attackLoss++;
      }
    }
    this.attackerLosses = attackLoss;
    this.defenderLosses = defendLoss;
  }

  public int getAttackerLosses() {
    return attackerLosses;
  }

  public int getDefenderLosses() {
    return defenderLosses;
  }

  public boolean isConquered() {
    return conquered;
  }
}
