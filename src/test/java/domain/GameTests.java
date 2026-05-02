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
    public void constructor_emptyPlayersList_throwsIllegalArgumentException() {
        IGameMap map = EasyMock.createMock(IGameMap.class);
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(new ArrayList<>(), map));
        EasyMock.verify(map);
    }
}
