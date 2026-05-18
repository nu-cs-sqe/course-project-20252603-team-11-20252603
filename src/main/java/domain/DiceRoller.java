package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRoller {
  private static final int MAX_ATTACKER_DICE = 3;
  private static final int MAX_DEFENDER_DICE = 2;
  private static final int FACES_PER_DIE = 6;

  private final Random random;

  public DiceRoller(Random random) {
    this.random = random;
  }

  public List<Integer> rollAttacker(int n) {
    if (n < 1 || n > MAX_ATTACKER_DICE) {
      throw new IllegalArgumentException("Attacker must roll between 1 and 3 dice.");
    }
    return rollDice(n);
  }

  public List<Integer> rollDefender(int n) {
    if (n < 1 || n > MAX_DEFENDER_DICE) {
      throw new IllegalArgumentException("Defender must roll between 1 and 2 dice.");
    }
    return rollDice(n);
  }

  public void sortDescending(List<Integer> dice) {
  }

  private List<Integer> rollDice(int n) {
    List<Integer> dice = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      dice.add(random.nextInt(FACES_PER_DIE) + 1);
    }
    return dice;
  }
}
