package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Integration (thread) test for Feature F7: "Ability for players to fortify by moving troops
// between two of their own territories connected through a player-owned path, once per turn."
//
// Unlike the per-class unit tests in this package, these tests wire real collaborators together
// with no mocks: FortificationPhase drives a real ConnectivityGraph, which runs a real BFS over a
// real GameMap, honoring ownership on real Player/Territory state. Fortify has no dice, so the
// tests need no seeding and assert exact outcomes (path validity, troop movement, once-per-turn).
public class F7Tests {

  private static final int SOURCE_TROOPS = 10;
  private static final int DEST_TROOPS = 1;
  private static final int INTERMEDIATE_TROOPS = 1;
  private static final int TROOPS_TO_MOVE = 4;
  private static final int SINGLE_TROOP = 1;
  private static final int EXPECTED_SOURCE_AFTER = SOURCE_TROOPS - TROOPS_TO_MOVE;
  private static final int EXPECTED_DEST_AFTER = DEST_TROOPS + TROOPS_TO_MOVE;
  private static final int MOVE_ALL_TROOPS = SOURCE_TROOPS;
  private static final int EXPECTED_PATH_LENGTH = 3;

  private GameMap map;
  private Player player;
  private Player enemy;
  // A player-owned chain: alpha -- bravo -- charlie.
  private Territory alpha;
  private Territory bravo;
  private Territory charlie;
  private FortificationPhase phase;

  @BeforeEach
  public void setUp() {
    map = new GameMap();
    player = new Player("Player");
    enemy = new Player("Enemy");

    alpha = new Territory("Alpha");
    bravo = new Territory("Bravo");
    charlie = new Territory("Charlie");
    map.addTerritory(alpha);
    map.addTerritory(bravo);
    map.addTerritory(charlie);
    map.addConnection(alpha, bravo);
    map.addConnection(bravo, charlie);

    ownBy(player, alpha, SOURCE_TROOPS);
    ownBy(player, bravo, INTERMEDIATE_TROOPS);
    ownBy(player, charlie, DEST_TROOPS);

    phase = new FortificationPhase(player, map);
  }

  private void ownBy(Player owner, Territory territory, int troops) {
    owner.addTerritory(territory);
    territory.setOwner(owner);
    territory.addTroops(troops);
  }

  @Test
  public void fortify_multiHopOwnedPath_movesTroopsAndCompletesPhase() {
    // the real ConnectivityGraph + GameMap BFS finds alpha -> bravo -> charlie
    List<Territory> path = phase.findPath(alpha, charlie);
    assertEquals(EXPECTED_PATH_LENGTH, path.size());
    assertEquals(alpha, path.get(0));
    assertEquals(charlie, path.get(path.size() - 1));

    phase.moveTroops(alpha, charlie, TROOPS_TO_MOVE);

    // troops moved across the real Territory objects, leaving the source garrisoned
    assertEquals(EXPECTED_SOURCE_AFTER, alpha.getTroopCount());
    assertEquals(EXPECTED_DEST_AFTER, charlie.getTroopCount());
    assertTrue(phase.isMoved());
    assertTrue(phase.isComplete());
  }

  @Test
  public void fortify_pathBlockedByEnemyTerritory_isRejectedViaConnectivityGraph() {
    // delta -- hostile -- echo, with the only route running through an enemy-owned territory
    Territory delta = new Territory("Delta");
    Territory hostile = new Territory("Hostile");
    Territory echo = new Territory("Echo");
    map.addTerritory(delta);
    map.addTerritory(hostile);
    map.addTerritory(echo);
    map.addConnection(delta, hostile);
    map.addConnection(hostile, echo);

    ownBy(player, delta, SOURCE_TROOPS);
    ownBy(enemy, hostile, INTERMEDIATE_TROOPS);
    ownBy(player, echo, DEST_TROOPS);

    // ownership-aware reachability is enforced by the real graph, not a stub
    assertFalse(phase.isConnected(delta, echo));
    assertTrue(phase.findPath(delta, echo).isEmpty());
    assertThrows(IllegalArgumentException.class, () -> phase.moveTroops(delta, echo, SINGLE_TROOP));
  }

  @Test
  public void fortify_secondMoveSameTurn_rejectedByOncePerTurnRule() {
    phase.moveTroops(alpha, charlie, TROOPS_TO_MOVE);

    assertThrows(IllegalStateException.class, () -> phase.moveTroops(charlie, bravo, SINGLE_TROOP));
  }

  @Test
  public void fortify_movingEntireGarrison_rejectedToKeepSourceDefended() {
    assertThrows(
        IllegalArgumentException.class, () -> phase.moveTroops(alpha, charlie, MOVE_ALL_TROOPS));
  }

  @Test
  public void fortify_sourceEqualsDestination_rejected() {
    assertThrows(
        IllegalArgumentException.class, () -> phase.moveTroops(alpha, alpha, SINGLE_TROOP));
  }

  @Test
  public void fortify_skipPhase_completesTurnAndBlocksLaterMove() {
    phase.skipPhase();

    assertTrue(phase.isComplete());
    assertThrows(
        IllegalStateException.class, () -> phase.moveTroops(alpha, charlie, TROOPS_TO_MOVE));
  }
}
