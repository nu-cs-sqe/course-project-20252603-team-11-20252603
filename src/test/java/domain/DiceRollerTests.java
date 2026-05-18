package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
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

  @Test
  public void rollAttacker_oneDie_returnsListOfSizeOneWithCorrectValue() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(2);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollAttacker(1);
    assertEquals(1, result.size());
    assertEquals(3, result.get(0));
    EasyMock.verify(random);
  }
}
