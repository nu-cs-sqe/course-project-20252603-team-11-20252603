package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRoller {
  private static final int MAX_ATTACKER_DICE = 3;

  private final Random random;

  public DiceRoller(Random random) {
    this.random = random;
  }

  public List<Integer> rollAttacker(int n) {
    if (n < 1 || n > MAX_ATTACKER_DICE) {
      throw new IllegalArgumentException("Attacker must roll between 1 and 3 dice.");
    }
    return new ArrayList<>();
  }
}
