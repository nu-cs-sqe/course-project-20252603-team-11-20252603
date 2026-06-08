package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class SetupPhaseTests {

  @Test
  public void constructor_nullGame_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new SetupPhase(null));
  }

  @Test
  public void constructor_twoPlayers_playerCountSetToTwo() {
    Game game = EasyMock.createMock(Game.class);
    EasyMock.expect(game.getPlayerCount()).andReturn(2);
    EasyMock.replay(game);

    SetupPhase setup = new SetupPhase(game);

    assertEquals(2, setup.getPlayerCount());
    EasyMock.verify(game);
  }

  @Test
  public void constructor_sixPlayers_playerCountSetToSix() {
    Game game = EasyMock.createMock(Game.class);
    EasyMock.expect(game.getPlayerCount()).andReturn(6);
    EasyMock.replay(game);

    SetupPhase setup = new SetupPhase(game);

    assertEquals(6, setup.getPlayerCount());
    EasyMock.verify(game);
  }
}
