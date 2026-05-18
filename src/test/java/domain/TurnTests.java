package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class TurnTests {
  // Helpers
  private Turn buildTurn(Player player, Game game, Random random, ReinforcementPhase rp, AttackPhase ap, FortificationPhase fp) {
    return new Turn(player, game, random) {
      @Override protected ReinforcementPhase createReinforcementPhase(Player p) { return rp; }
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
}
