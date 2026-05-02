package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    // ! Helpers
    private List<Player> makePlayers(int count) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            players.add(EasyMock.createMock(Player.class));
        }
        return players;
    }

    private IGameMap makeMap() {
        return EasyMock.createMock(IGameMap.class);
    }

    private void replayAll(List<Player> players, IGameMap map) {
        players.forEach(EasyMock::replay);
        EasyMock.replay(map);
    }

    private void verifyAll(List<Player> players, IGameMap map) {
        players.forEach(EasyMock::verify);
        EasyMock.verify(map);
    }

    // ! Constructor tests
    @Test
    public void constructor_nullPlayers_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(null, map));
        EasyMock.verify(map);
    }

    @Test
    public void constructor_emptyPlayersList_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(new ArrayList<>(), map));
        EasyMock.verify(map);
    }

    @Test
    public void constructor_onePlayer_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(1);
        replayAll(players, map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        verifyAll(players, map);
    }

    @Test
    public void constructor_twoPlayers_gameConstructedWithPlayersAndMap() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(2, game.getPlayerCount());
        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }

    @Test
    public void constructor_sixPlayers_gameConstructedWithPlayersAndMap() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(6);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(6, game.getPlayerCount());
        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }

    @Test
    public void constructor_sevenPlayers_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(7);
        replayAll(players, map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        verifyAll(players, map);
    }

    @Test
    public void constructor_playersListContainsNull_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(1);
        players.add(null);
        players.forEach(p -> { if (p != null) EasyMock.replay(p); });
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        players.forEach(p -> { if (p != null) EasyMock.verify(p); });
        EasyMock.verify(map);
    }

    @Test
    public void constructor_nullMap_throwsIllegalArgumentException() {
        List<Player> players = makePlayers(2);
        players.forEach(EasyMock::replay);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, null));
        players.forEach(EasyMock::verify);
    }

    // ! assignTerritories tests
    @Test
    public void assignTerritories_noTerritories_noTerritoriesAddedToPlayers() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        EasyMock.expect(map.getTerritories()).andReturn(new ArrayList<>());
        replayAll(players, map);

        Game game = new Game(players, map);
        game.assignTerritories();

        verifyAll(players, map);
    }

    @Test
    public void constructor_validPlayersAndMap_mapStoredCorrectly() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }
}
