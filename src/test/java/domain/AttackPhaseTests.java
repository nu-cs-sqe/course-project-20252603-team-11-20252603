package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class AttackPhaseTests {

  @Test
  public void constructor_validArguments_conqueredCountInitializedToZero() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    EasyMock.replay(attacker, diceRoller, game);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);

    assertEquals(0, phase.getConqueredCount());
    EasyMock.verify(attacker, diceRoller, game);
  }

  @Test
  public void canAttack_sourceTroopCountOne_returnsFalse() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(s.getTroopCount()).andReturn(1);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertFalse(phase.canAttack(s, t));

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }
}
