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

  @Test
  public void canAttack_allConditionsValid_returnsTrue() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(true);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    EasyMock.replay(attacker, enemy, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertTrue(phase.canAttack(s, t));

    EasyMock.verify(attacker, enemy, diceRoller, game, map, s, t);
  }

  @Test
  public void canAttack_sourceNotOwnedByAttacker_returnsFalse() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(enemy);
    EasyMock.replay(attacker, enemy, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertFalse(phase.canAttack(s, t));

    EasyMock.verify(attacker, enemy, diceRoller, game, s, t);
  }

  @Test
  public void canAttack_territoriesNotAdjacent_returnsFalse() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(false);
    EasyMock.replay(attacker, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertFalse(phase.canAttack(s, t));

    EasyMock.verify(attacker, diceRoller, game, map, s, t);
  }

  @Test
  public void canAttack_targetOwnedByAttacker_returnsFalse() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(true);
    EasyMock.expect(t.getOwner()).andReturn(attacker);
    EasyMock.replay(attacker, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertFalse(phase.canAttack(s, t));

    EasyMock.verify(attacker, diceRoller, game, map, s, t);
  }
}
