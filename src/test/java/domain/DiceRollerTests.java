package domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class DiceRollerTests {

  @Test
  public void rollAttacker_zeroDice_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    assertThrows(IllegalArgumentException.class, () -> diceRoller.rollAttacker(0));
    EasyMock.verify(random);
  }
}
