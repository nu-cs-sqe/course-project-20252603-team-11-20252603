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

  @Test
  public void rollAttacker_threeDice_returnsListOfSizeThreeWithCorrectValues() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(2).times(3);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollAttacker(3);
    assertEquals(3, result.size());
    assertEquals(List.of(3, 3, 3), result);
    EasyMock.verify(random);
  }

  @Test
  public void rollAttacker_fourDice_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    assertThrows(IllegalArgumentException.class, () -> diceRoller.rollAttacker(4));
    EasyMock.verify(random);
  }

  @Test
  public void rollAttacker_randomProducesMinimum_dieValueIsOne() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(0);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollAttacker(1);
    assertEquals(1, result.get(0));
    EasyMock.verify(random);
  }

  @Test
  public void rollAttacker_randomProducesMaximum_dieValueIsSix() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(5);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollAttacker(1);
    assertEquals(6, result.get(0));
    EasyMock.verify(random);
  }

  @Test
  public void rollDefender_zeroDice_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    assertThrows(IllegalArgumentException.class, () -> diceRoller.rollDefender(0));
    EasyMock.verify(random);
  }

  @Test
  public void rollDefender_oneDie_returnsListOfSizeOneWithCorrectValue() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(2);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollDefender(1);
    assertEquals(1, result.size());
    assertEquals(3, result.get(0));
    EasyMock.verify(random);
  }

  @Test
  public void rollDefender_twoDice_returnsListOfSizeTwoWithCorrectValues() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(2).times(2);
    EasyMock.replay(random);
    DiceRoller diceRoller = new DiceRoller(random);
    List<Integer> result = diceRoller.rollDefender(2);
    assertEquals(2, result.size());
    assertEquals(List.of(3, 3), result);
    EasyMock.verify(random);
  }
}
