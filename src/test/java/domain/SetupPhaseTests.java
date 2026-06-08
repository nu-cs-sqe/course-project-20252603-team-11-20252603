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
  public void chooseFirstPlayer_twoPlayers_lowerBoundPlayerReturned() {
    Game game = EasyMock.createMock(Game.class);
    Player player = EasyMock.createMock(Player.class);
    EasyMock.expect(game.getPlayerCount()).andReturn(2);
    game.chooseFirstPlayer();
    EasyMock.expect(game.getCurrentActivePlayer()).andReturn(player);
    EasyMock.replay(game, player);

    SetupPhase setup = new SetupPhase(game);
    Player result = setup.chooseFirstPlayer();

    assertSame(player, result);
    EasyMock.verify(game, player);
  }

  @Test
  public void distributeStartingTroops_delegatesToGame() {
    Game game = EasyMock.createMock(Game.class);
    EasyMock.expect(game.getPlayerCount()).andReturn(2);
    game.distributeStartingTroops();
    EasyMock.replay(game);

    SetupPhase setup = new SetupPhase(game);
    setup.distributeStartingTroops();

    EasyMock.verify(game);
  }

  @Test
  public void assignTerritories_delegatesToGame() {
    Game game = EasyMock.createMock(Game.class);
    EasyMock.expect(game.getPlayerCount()).andReturn(2);
    game.assignTerritories();
    EasyMock.replay(game);

    SetupPhase setup = new SetupPhase(game);
    setup.assignTerritories();

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
