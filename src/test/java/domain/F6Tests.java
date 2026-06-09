package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Integration (thread) test for Feature F6: "Ability for players to initiate attacks on enemy
// territories, with dice-rolling mechanics to decide attack outcomes."
//
// Unlike the per-class unit tests in this package, these tests wire real collaborators together
// with no mocks: AttackPhase drives the real DiceRoller/BattleResult, mutating real
// Territory/Player state, validating adjacency through a real GameMap, and awarding a card through
// a real Game + DeckManager.
public class F6Tests {

  private static final int SAFETY_CAP = 1000;

  private GameMap map;
  private Player attacker;
  private Player defender;
  private Territory source;
  private Territory target;
  private Game game;
  private DiceRoller diceRoller;
  private AttackPhase phase;

  @BeforeEach
  public void setUp() {
    map = new GameMap();
    source = new Territory("Source");
    target = new Territory("Target");
    map.addTerritory(source);
    map.addTerritory(target);
    map.addConnection(source, target);

    attacker = new Player("Attacker");
    defender = new Player("Defender");

    attacker.addTerritory(source);
    source.setOwner(attacker);
    source.addTroops(20);

    defender.addTerritory(target);
    target.setOwner(defender);
    target.addTroops(1);

    List<Player> players = new ArrayList<>();
    players.add(attacker);
    players.add(defender);

    // a real deck-backed DeckManager
    Deque<RiskCard> drawPile = new ArrayDeque<>();
    drawPile.add(new RiskCard(RiskCardType.INFANTRY, target));
    DeckManager deckManager = drawPile::pop;

    Random gameRandom = new Random(1);
    game = new Game(players, map, new ArrayList<>(drawPile), gameRandom, deckManager);

    diceRoller = new DiceRoller(new Random(7));
    phase = new AttackPhase(attacker, diceRoller, game);
  }

  @Test
  public void attack_eligibleSourceAndAdjacentEnemy_canAttackAndDeclareSucceed() {
    assertTrue(phase.canAttack(source, target));
    // declareAttack validates ownership, dice bounds, troop count, and adjacency via GameMap
    phase.declareAttack(source, target, 1);
  }

  @Test
  public void attack_resolveUntilConquest_transfersOwnershipAndAwardsCard() {
    int safety = 0;
    while (phase.getConqueredCount() == 0) {
      phase.resolveBattle(source, target, 1);
      if (++safety > SAFETY_CAP) {
        fail("Attack never resolved to a conquest within the safety cap.");
      }
    }

    int troopsMovedIn = 3;
    phase.moveInTroops(source, target, troopsMovedIn);
    phase.endPhase();

    // ownership transferred across the real Player/Territory objects
    assertEquals(attacker, target.getOwner());
    assertTrue(attacker.getTerritories().contains(target));
    assertFalse(defender.getTerritories().contains(target));
    assertEquals(troopsMovedIn, target.getTroopCount());
    // source retained at least one army
    assertTrue(source.getTroopCount() >= 1);
    // a card was awarded through Game + DeckManager because a territory was conquered
    assertEquals(1, attacker.getCards().size());
    assertTrue(phase.isEnded());
  }

  @Test
  public void attack_defenderSurvivesSingleRound_totalTroopsDropByExactlyOneAndNoConquest() {
    // two defenders means a single 1-die exchange cannot capture the territory
    target.addTroops(1);
    int before = source.getTroopCount() + target.getTroopCount();

    phase.resolveBattle(source, target, 1);

    int after = source.getTroopCount() + target.getTroopCount();
    assertEquals(1, before - after);
    assertEquals(0, phase.getConqueredCount());
  }

  @Test
  public void attack_nonAdjacentTarget_declareAttackRejectedViaGameMap() {
    Territory remote = new Territory("Remote");
    map.addTerritory(remote);
    remote.setOwner(defender);
    remote.addTroops(1);

    // adjacency is enforced by the real GameMap, not a stub
    assertThrows(IllegalArgumentException.class, () -> phase.declareAttack(source, remote, 1));
  }

  @Test
  public void attack_sourceWithSingleArmy_cannotAttack() {
    Territory weak = new Territory("Weak");
    map.addTerritory(weak);
    map.addConnection(weak, target);
    attacker.addTerritory(weak);
    weak.setOwner(attacker);
    weak.addTroops(1);

    assertFalse(phase.canAttack(weak, target));
  }
}
