package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    // ! Constructor tests
    @Test
    public void constructor_nullPlayers_throwsIllegalArgumentException() {
        IGameMap map = EasyMock.createMock(IGameMap.class);
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(null, map));
        EasyMock.verify(map);
    }

    @Test
    public void constructor_twoPlayers_gameConstructedWithPlayersAndMap() {
        IGameMap map = EasyMock.createMock(IGameMap.class);
        Player p1 = EasyMock.createMock(Player.class);
        Player p2 = EasyMock.createMock(Player.class);
        EasyMock.replay(map, p1, p2);

        Game game = new Game(List.of(p1, p2), map);

        assertEquals(2, game.getPlayerCount());
        assertEquals(map, game.getMap());
        EasyMock.verify(map, p1, p2);
    }

    @Test
    public void constructor_onePlayer_throwsIllegalArgumentException() {
        IGameMap map = EasyMock.createMock(IGameMap.class);
        Player p1 = EasyMock.createMock(Player.class);
        EasyMock.replay(map, p1);
        assertThrows(IllegalArgumentException.class, () -> new Game(List.of(p1), map));
        EasyMock.verify(map, p1);
    }

    @Test
    public void constructor_emptyPlayersList_throwsIllegalArgumentException() {
        IGameMap map = EasyMock.createMock(IGameMap.class);
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(new ArrayList<>(), map));
        EasyMock.verify(map);
    }
}
