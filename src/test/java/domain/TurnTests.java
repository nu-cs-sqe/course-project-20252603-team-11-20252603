package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class TurnTests {
  // Helpers
  private Turn buildTurn(Player player, Game game, Random random, ReinforcementPhase rp, AttackPhase ap, FortificationPhase fp) {
    return new Turn(player, game, random) {
      @Override protected ReinforcementPhase createReinforcementPhase(Player p) { return rp; }
      @Override protected AttackPhase createAttackPhase(Player p, Game g, Random r) { return ap; }
      @Override protected FortificationPhase createFortificationPhase(Player p, Game g) { return fp; }
    };
  }
  private void recordAdvanceToReinforcement(Player p) {
    EasyMock.expect(p.calculateReinforcements()).andReturn(5);
    p.setAvailableTroops(5);
  }

  private void recordAdvanceToAttack(Player p, ReinforcementPhase rp) {
    recordAdvanceToReinforcement(p);
    EasyMock.expect(rp.isComplete()).andReturn(true);
  }

  private void recordAdvanceToFortification(Player p, ReinforcementPhase rp,
                                            AttackPhase ap, int conqueredCount) {
    recordAdvanceToAttack(p, rp);
    EasyMock.expect(ap.isEnded()).andReturn(true);
    EasyMock.expect(ap.getConqueredCount()).andReturn(conqueredCount);
  }

  private void recordAdvanceToEnded(Player p, ReinforcementPhase rp,
                                    AttackPhase ap, FortificationPhase fp) {
    recordAdvanceToFortification(p, rp, ap, 0);
    EasyMock.expect(fp.isComplete()).andReturn(true);
  }

  // Start Turn tests
  @Test
  public void constructor_nullPlayer_throwsIllegalArgumentException() {
    Game game  = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    assertThrows(IllegalArgumentException.class, () -> new Turn(null, game, random));
  }

  @Test
  public void constructor_nullGame_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Random random = EasyMock.createMock(Random.class);
    assertThrows(IllegalArgumentException.class, () -> new Turn(player, null, random));
  }

  @Test
  public void constructor_nullRandom_throwsIllegalArgumentException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    assertThrows(IllegalArgumentException.class, () -> new Turn(player, game, null));
  }

  @Test
  public void constructor_validArgs_initialTurnState() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);

    Turn turn = new Turn(player, game, random);

    assertNull(turn.getPhase());
    assertFalse(turn.hasConqueredThisTurn());
    assertSame(player, turn.getCurrentPlayer());
    assertSame(game, turn.getGame());
    assertNull(turn.getReinforcementPhase());
    assertNull(turn.getAttackPhase());
    assertNull(turn.getFortificationPhase());
  }

  @Test
  public void startTurn_calledTwice_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertThrows(IllegalStateException.class, turn::startTurn);
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void startTurn_valid_setsPhaseAndReinforcements() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertEquals(TurnPhase.REINFORCEMENT, turn.getPhase());
    assertSame(rp, turn.getReinforcementPhase());
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void runReinforcementPhase_beforeStartTurn_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(player, game);

    Turn turn = new Turn(player, game, random);

    assertThrows(IllegalStateException.class, turn::runReinforcementPhase);
    EasyMock.verify(player, game);
  }

  @Test
  public void runReinforcementPhase_whenAttackPhase_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);

    recordAdvanceToAttack(player, rp);
    EasyMock.replay(player, game, rp, ap);

    Turn turn = buildTurn(player, game, random, rp, ap, null);
    turn.startTurn();
    turn.runReinforcementPhase();

    assertThrows(IllegalStateException.class, turn::runReinforcementPhase);
    EasyMock.verify(player, game, rp, ap);
  }

  @Test
  public void runReinforcementPhase_whenFortificationPhase_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 0);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertThrows(IllegalStateException.class, turn::runReinforcementPhase);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runReinforcementPhase_whenEnded_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToEnded(player, rp, ap, fp);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();

    assertThrows(IllegalStateException.class, turn::runReinforcementPhase);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runReinforcementPhase_notComplete_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.expect(rp.isComplete()).andReturn(false);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertThrows(IllegalStateException.class, turn::runReinforcementPhase);
    assertEquals(TurnPhase.REINFORCEMENT, turn.getPhase());
    assertNull(turn.getAttackPhase());
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void runReinforcementPhase_complete_transitionsToAttack() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);

    recordAdvanceToAttack(player, rp);
    EasyMock.replay(player, game, rp, ap);

    Turn turn = buildTurn(player, game, random, rp, ap, null);
    turn.startTurn();
    turn.runReinforcementPhase();

    assertEquals(TurnPhase.ATTACK, turn.getPhase());
    assertSame(ap, turn.getAttackPhase());
    EasyMock.verify(player, game, rp, ap);
  }

  @Test
  public void runAttackPhase_whenReinforcement_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertThrows(IllegalStateException.class, turn::runAttackPhase);
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void runAttackPhase_whenFortification_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 0);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertThrows(IllegalStateException.class, turn::runAttackPhase);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runAttackPhase_whenEnded_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToEnded(player, rp, ap, fp);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();

    assertThrows(IllegalStateException.class, turn::runAttackPhase);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runAttackPhase_notEnded_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);

    recordAdvanceToAttack(player, rp);
    EasyMock.expect(ap.isEnded()).andReturn(false);
    EasyMock.replay(player, game, rp, ap);

    Turn turn = buildTurn(player, game, random, rp, ap, null);
    turn.startTurn();
    turn.runReinforcementPhase();

    assertThrows(IllegalStateException.class, turn::runAttackPhase);
    assertEquals(TurnPhase.ATTACK, turn.getPhase());
    assertNull(turn.getFortificationPhase());
    EasyMock.verify(player, game, rp, ap);
  }

  @Test
  public void runAttackPhase_noConquests_transitionsAndConqueredFalse() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 0);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertEquals(TurnPhase.FORTIFICATION, turn.getPhase());
    assertFalse(turn.hasConqueredThisTurn());
    assertSame(fp, turn.getFortificationPhase());
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runAttackPhase_oneConquest_conqueredTrue() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 1);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertEquals(TurnPhase.FORTIFICATION, turn.getPhase());
    assertTrue(turn.hasConqueredThisTurn());
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runAttackPhase_manyConquests_conqueredTrue() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 7);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertEquals(TurnPhase.FORTIFICATION, turn.getPhase());
    assertTrue(turn.hasConqueredThisTurn());
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runFortificationPhase_whenReinforcement_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertThrows(IllegalStateException.class, turn::runFortificationPhase);
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void runFortificationPhase_whenAttack_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);

    recordAdvanceToAttack(player, rp);
    EasyMock.replay(player, game, rp, ap);

    Turn turn = buildTurn(player, game, random, rp, ap, null);
    turn.startTurn();
    turn.runReinforcementPhase();

    assertThrows(IllegalStateException.class, turn::runFortificationPhase);
    EasyMock.verify(player, game, rp, ap);
  }

  @Test
  public void runFortificationPhase_whenEnded_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToEnded(player, rp, ap, fp);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();

    assertThrows(IllegalStateException.class, turn::runFortificationPhase);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runFortificationPhase_notComplete_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 0);
    EasyMock.expect(fp.isComplete()).andReturn(false);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertThrows(IllegalStateException.class, turn::runFortificationPhase);
    assertEquals(TurnPhase.FORTIFICATION, turn.getPhase());
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void runFortificationPhase_complete_transitionsToEnded() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToEnded(player, rp, ap, fp);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();

    assertEquals(TurnPhase.ENDED, turn.getPhase());
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void endTurn_beforeStart_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(player, game);

    Turn turn = new Turn(player, game, random);

    assertThrows(IllegalStateException.class, turn::endTurn);
    EasyMock.verify(player, game);
  }

  @Test
  public void endTurn_whenReinforcement_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);

    recordAdvanceToReinforcement(player);
    EasyMock.replay(player, game, rp);

    Turn turn = buildTurn(player, game, random, rp, null, null);
    turn.startTurn();

    assertThrows(IllegalStateException.class, turn::endTurn);
    EasyMock.verify(player, game, rp);
  }

  @Test
  public void endTurn_whenAttack_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);

    recordAdvanceToAttack(player, rp);
    EasyMock.replay(player, game, rp, ap);

    Turn turn = buildTurn(player, game, random, rp, ap, null);
    turn.startTurn();
    turn.runReinforcementPhase();

    assertThrows(IllegalStateException.class, turn::endTurn);
    EasyMock.verify(player, game, rp, ap);
  }

  @Test
  public void endTurn_whenFortification_throwsIllegalStateException() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = EasyMock.createMock(Random.class);
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToFortification(player, rp, ap, 0);
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();

    assertThrows(IllegalStateException.class, turn::endTurn);
    EasyMock.verify(player, game, rp, ap, fp);
  }

  @Test
  public void endTurn_whenEnded_callsAdvanceToNextPlayer() {
    Player player = EasyMock.createMock(Player.class);
    Game game = EasyMock.createMock(Game.class);
    Random random = new Random();
    ReinforcementPhase rp = EasyMock.createMock(ReinforcementPhase.class);
    AttackPhase ap = EasyMock.createMock(AttackPhase.class);
    FortificationPhase fp = EasyMock.createMock(FortificationPhase.class);

    recordAdvanceToEnded(player, rp, ap, fp);
    game.advanceToNextPlayer();
    EasyMock.replay(player, game, rp, ap, fp);

    Turn turn = buildTurn(player, game, random, rp, ap, fp);
    turn.startTurn();
    turn.runReinforcementPhase();
    turn.runAttackPhase();
    turn.runFortificationPhase();
    turn.endTurn();

    EasyMock.verify(player, game, rp, ap, fp);
  }
}
