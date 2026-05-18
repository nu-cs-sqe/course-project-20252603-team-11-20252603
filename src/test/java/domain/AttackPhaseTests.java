package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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

  @Test
  public void declareAttack_nEqualsZero_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    EasyMock.replay(attacker, diceRoller, game);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(null, null, 0));

    EasyMock.verify(attacker, diceRoller, game);
  }

  @Test
  public void declareAttack_nEqualsOne_sourceTroopCountTwo_noException() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(true);
    EasyMock.replay(attacker, enemy, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.declareAttack(s, t, 1);

    EasyMock.verify(attacker, enemy, diceRoller, game, map, s, t);
  }

  @Test
  public void declareAttack_nEqualsThree_sourceTroopCountFour_noException() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    EasyMock.expect(s.getTroopCount()).andReturn(4);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(true);
    EasyMock.replay(attacker, enemy, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.declareAttack(s, t, 3);

    EasyMock.verify(attacker, enemy, diceRoller, game, map, s, t);
  }

  @Test
  public void declareAttack_nEqualsFour_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    EasyMock.replay(attacker, diceRoller, game);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(null, null, 4));

    EasyMock.verify(attacker, diceRoller, game);
  }

  @Test
  public void declareAttack_nExceedsTroopCountMinusOne_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.replay(attacker, enemy, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(s, t, 2));

    EasyMock.verify(attacker, enemy, diceRoller, game, s, t);
  }

  @Test
  public void declareAttack_sourceNotOwnedByAttacker_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(enemy);
    EasyMock.replay(attacker, enemy, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(s, t, 1));

    EasyMock.verify(attacker, enemy, diceRoller, game, s, t);
  }

  @Test
  public void declareAttack_territoriesNotAdjacent_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    GameMap map = EasyMock.createMock(GameMap.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    EasyMock.expect(s.getTroopCount()).andReturn(2);
    EasyMock.expect(game.getMap()).andReturn(map);
    EasyMock.expect(map.areAdjacent(s, t)).andReturn(false);
    EasyMock.replay(attacker, enemy, diceRoller, game, map, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(s, t, 1));

    EasyMock.verify(attacker, enemy, diceRoller, game, map, s, t);
  }

  @Test
  public void declareAttack_targetOwnedByAttacker_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.expect(s.getOwner()).andReturn(attacker);
    EasyMock.expect(t.getOwner()).andReturn(attacker);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    assertThrows(IllegalArgumentException.class,
        () -> phase.declareAttack(s, t, 1));

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void resolveBattle_defenderHasOneTroop_rollsOneDie() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(3);
    List<Integer> defendDice = List.of(4);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(3), List.of(4), false));
    s.removeTroops(1);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void resolveBattle_defenderHasTwoTroops_rollsTwoDice() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(3);
    List<Integer> defendDice = List.of(5, 4);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(2);
    EasyMock.expect(diceRoller.rollDefender(2)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(3), List.of(5, 4), false));
    s.removeTroops(1);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void resolveBattle_noConquest_lossesAppliedAndConqueredCountUnchanged() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(5);
    List<Integer> defendDice = List.of(3, 2);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(3);
    EasyMock.expect(diceRoller.rollDefender(2)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(5), List.of(3, 2), false));
    t.removeTroops(1);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);

    assertEquals(0, phase.getConqueredCount());
    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void resolveBattle_conquest_conqueredCountIncrementedAndRemoveTroopsNotCalled() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6), List.of(1), false));
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);

    assertEquals(1, phase.getConqueredCount());
    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void moveInTroops_nBelowLastAttackDice_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6, 5);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(2)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6, 5), List.of(1), false));
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 2);
    assertThrows(IllegalArgumentException.class,
        () -> phase.moveInTroops(s, t, 1));

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void moveInTroops_nEqualsLastAttackDice_transfersOwnershipAndTroops() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6, 5);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(2)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6, 5), List.of(1), false));
    EasyMock.expect(s.getTroopCount()).andReturn(5);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    enemy.removeTerritory(t);
    attacker.addTerritory(t);
    t.conquer(attacker, 2);
    s.removeTroops(2);
    EasyMock.replay(attacker, enemy, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 2);
    phase.moveInTroops(s, t, 2);

    EasyMock.verify(attacker, enemy, diceRoller, game, s, t);
  }

  @Test
  public void moveInTroops_nEqualsSourceTroopCountMinusOne_transfersOwnershipAndTroops() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6), List.of(1), false));
    EasyMock.expect(s.getTroopCount()).andReturn(5);
    EasyMock.expect(t.getOwner()).andReturn(enemy);
    enemy.removeTerritory(t);
    attacker.addTerritory(t);
    t.conquer(attacker, 4);
    s.removeTroops(4);
    EasyMock.replay(attacker, enemy, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);
    phase.moveInTroops(s, t, 4);

    EasyMock.verify(attacker, enemy, diceRoller, game, s, t);
  }

  @Test
  public void moveInTroops_nEqualsTroopCount_throwsIllegalArgumentException() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6), List.of(1), false));
    EasyMock.expect(s.getTroopCount()).andReturn(3);
    EasyMock.replay(attacker, diceRoller, game, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);
    assertThrows(IllegalArgumentException.class,
        () -> phase.moveInTroops(s, t, 3));

    EasyMock.verify(attacker, diceRoller, game, s, t);
  }

  @Test
  public void awardCardIfEarned_noConquest_noCardDrawn() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    EasyMock.replay(attacker, diceRoller, game);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.awardCardIfEarned();

    EasyMock.verify(attacker, diceRoller, game);
  }

  @Test
  public void awardCardIfEarned_twoConquests_drawsExactlyOneCard() {
    Player attacker = EasyMock.createMock(Player.class);
    Player enemy = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    RiskCard card = EasyMock.createMock(RiskCard.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    List<Integer> attackDice1 = List.of(6);
    List<Integer> defendDice1 = List.of(1);
    List<Integer> attackDice2 = List.of(5);
    List<Integer> defendDice2 = List.of(2);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice1);
    EasyMock.expect(t1.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice1);
    EasyMock.expect(diceRoller.compare(attackDice1, defendDice1))
        .andReturn(new BattleResult(List.of(6), List.of(1), false));
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice2);
    EasyMock.expect(t2.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice2);
    EasyMock.expect(diceRoller.compare(attackDice2, defendDice2))
        .andReturn(new BattleResult(List.of(5), List.of(2), false));
    EasyMock.expect(game.drawCard()).andReturn(card);
    attacker.addCard(card);
    EasyMock.replay(attacker, enemy, diceRoller, game, card, s, t1, t2);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t1, 1);
    phase.resolveBattle(s, t2, 1);
    phase.awardCardIfEarned();

    EasyMock.verify(attacker, enemy, diceRoller, game, card, s, t1, t2);
  }

  @Test
  public void awardCardIfEarned_oneConquest_drawsOneCardAndAddsToAttacker() {
    Player attacker = EasyMock.createMock(Player.class);
    DiceRoller diceRoller = EasyMock.createMock(DiceRoller.class);
    Game game = EasyMock.createMock(Game.class);
    RiskCard card = EasyMock.createMock(RiskCard.class);
    Territory s = EasyMock.createMock(Territory.class);
    Territory t = EasyMock.createMock(Territory.class);

    List<Integer> attackDice = List.of(6);
    List<Integer> defendDice = List.of(1);
    EasyMock.expect(diceRoller.rollAttacker(1)).andReturn(attackDice);
    EasyMock.expect(t.getTroopCount()).andReturn(1);
    EasyMock.expect(diceRoller.rollDefender(1)).andReturn(defendDice);
    EasyMock.expect(diceRoller.compare(attackDice, defendDice))
        .andReturn(new BattleResult(List.of(6), List.of(1), false));
    EasyMock.expect(game.drawCard()).andReturn(card);
    attacker.addCard(card);
    EasyMock.replay(attacker, diceRoller, game, card, s, t);

    AttackPhase phase = new AttackPhase(attacker, diceRoller, game);
    phase.resolveBattle(s, t, 1);
    phase.awardCardIfEarned();

    EasyMock.verify(attacker, diceRoller, game, card, s, t);
  }
}
