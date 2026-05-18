package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class BattleResultTests {

  @Test
  public void getAttackerLosses_oneVsOne_attackerWins_attackerLosesZero() {
    BattleResult result = new BattleResult(List.of(3), List.of(2), false);
    assertEquals(0, result.getAttackerLosses());
    assertEquals(1, result.getDefenderLosses());
  }

  @Test
  public void getAttackerLosses_oneVsOne_tie_attackerLosesOne() {
    BattleResult result = new BattleResult(List.of(3), List.of(3), false);
    assertEquals(1, result.getAttackerLosses());
    assertEquals(0, result.getDefenderLosses());
  }

  @Test
  public void getAttackerLosses_oneVsOne_defenderWins_attackerLosesOne() {
    BattleResult result = new BattleResult(List.of(2), List.of(3), false);
    assertEquals(1, result.getAttackerLosses());
    assertEquals(0, result.getDefenderLosses());
  }

  @Test
  public void getAttackerLosses_threeVsTwo_attackerWinsBoth_attackerLosesZero() {
    BattleResult result = new BattleResult(List.of(6, 5, 4), List.of(3, 1), false);
    assertEquals(0, result.getAttackerLosses());
    assertEquals(2, result.getDefenderLosses());
  }

  @Test
  public void getAttackerLosses_threeVsTwo_splitResult_eachLosesOne() {
    BattleResult result = new BattleResult(List.of(6, 1, 1), List.of(5, 5), false);
    assertEquals(1, result.getAttackerLosses());
    assertEquals(1, result.getDefenderLosses());
  }
}
