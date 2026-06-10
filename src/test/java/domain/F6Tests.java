package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
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

  private static final int GAME_SEED = 1;
  private static final int DICE_SEED = 7;
  private static final int ATTACKER_ARMIES = 20;
  private static final int SINGLE_ARMY = 1;
  private static final int ATTACK_DICE = 1;
  private static final int TROOPS_MOVED_IN = 3;
  private static final int MIN_REMAINING_ARMIES = 1;
  private static final int EXPECTED_CARDS_AWARDED = 1;
  private static final int EXPECTED_SINGLE_ROUND_LOSS = 1;
  private static final int NO_CONQUEST = 0;
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
    source.addTroops(ATTACKER_ARMIES);

    defender.addTerritory(target);
    target.setOwner(defender);
    target.addTroops(SINGLE_ARMY);

    List<Player> players = new ArrayList<>();
    players.add(attacker);
    players.add(defender);

    // a real DeckManager seeded with one card to award on a successful conquest
    Random gameRandom = new Random(GAME_SEED);
    List<RiskCard> initialDrawPile = new ArrayList<>();
    initialDrawPile.add(new RiskCard(RiskCardType.INFANTRY, target));
    DeckManager deckManager = new DeckManager(gameRandom, initialDrawPile);

    game = new Game(players, map, deckManager, gameRandom);

    diceRoller = new DiceRoller(new Random(DICE_SEED));
    phase = new AttackPhase(attacker, diceRoller, game);
  }

  @Test
  public void attack_eligibleSourceAndAdjacentEnemy_canAttackAndDeclareSucceed() {
    assertTrue(phase.canAttack(source, target));
    // declareAttack validates ownership, dice bounds, troop count, and adjacency via GameMap
    phase.declareAttack(source, target, ATTACK_DICE);
  }

  @Test
  public void attack_resolveUntilConquest_transfersOwnershipAndAwardsCard() {
    int safety = 0;
    while (phase.getConqueredCount() == NO_CONQUEST) {
      phase.resolveBattle(source, target, ATTACK_DICE);
      if (++safety > SAFETY_CAP) {
        fail("Attack never resolved to a conquest within the safety cap.");
      }
    }

    phase.moveInTroops(source, target, TROOPS_MOVED_IN);
    phase.endPhase();

    // ownership transferred across the real Player/Territory objects
    assertEquals(attacker, target.getOwner());
    assertTrue(attacker.getTerritories().contains(target));
    assertFalse(defender.getTerritories().contains(target));
    assertEquals(TROOPS_MOVED_IN, target.getTroopCount());
    // source retained at least one army
    assertTrue(source.getTroopCount() >= MIN_REMAINING_ARMIES);
    // a card was awarded through Game + DeckManager because a territory was conquered
    assertEquals(EXPECTED_CARDS_AWARDED, attacker.getCards().size());
    assertTrue(phase.isEnded());
  }

  @Test
  public void attack_defenderSurvivesSingleRound_totalTroopsDropByExactlyOneAndNoConquest() {
    // two defenders means a single 1-die exchange cannot capture the territory
    target.addTroops(SINGLE_ARMY);
    int before = source.getTroopCount() + target.getTroopCount();

    phase.resolveBattle(source, target, ATTACK_DICE);

    int after = source.getTroopCount() + target.getTroopCount();
    assertEquals(EXPECTED_SINGLE_ROUND_LOSS, before - after);
    assertEquals(NO_CONQUEST, phase.getConqueredCount());
  }

  @Test
  public void attack_nonAdjacentTarget_declareAttackRejectedViaGameMap() {
    Territory remote = new Territory("Remote");
    map.addTerritory(remote);
    remote.setOwner(defender);
    remote.addTroops(SINGLE_ARMY);

    // adjacency is enforced by the real GameMap, not a stub
    assertThrows(
        IllegalArgumentException.class, () -> phase.declareAttack(source, remote, ATTACK_DICE));
  }

  @Test
  public void attack_sourceWithSingleArmy_cannotAttack() {
    Territory weak = new Territory("Weak");
    map.addTerritory(weak);
    map.addConnection(weak, target);
    attacker.addTerritory(weak);
    weak.setOwner(attacker);
    weak.addTroops(SINGLE_ARMY);

    assertFalse(phase.canAttack(weak, target));
  }
}
