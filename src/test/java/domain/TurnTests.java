package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class TurnTests {
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
}
