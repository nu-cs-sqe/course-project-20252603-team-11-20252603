package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapTests {
    // ! Constructor tests
    @Test
    public void constructor_emptyMap_initialStateIsCorrect() {
        GameMap map = new GameMap();
        assertTrue(map.getTerritories().isEmpty());
    }

    // ! addTerritory tests
    @Test
    public void addTerritory_nullTerritory_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        assertThrows(IllegalArgumentException.class, () -> map.addTerritory(null));
    }

    @Test
    public void addTerritory_emptyMap_sizeBecomesOne() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);
        map.addTerritory(t1);

        assertEquals(1, map.getTerritories().size());
        assertTrue(map.getTerritories().contains(t1));
        EasyMock.verify(t1);
    }

    @Test
    public void addTerritory_oneExisting_sizeBecomesTwo() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);

        map.addTerritory(t1);
        assertEquals(1, map.getTerritories().size());
        assertTrue(map.getTerritories().contains(t1));

        map.addTerritory(t2);
        assertEquals(2, map.getTerritories().size());
        assertTrue(map.getTerritories().contains(t2));

        EasyMock.verify(t1, t2);
    }

    @Test
    public void addTerritory_duplicateTerritory_sizeRemainsOne() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);

        map.addTerritory(t1);
        map.addTerritory(t1);

        assertEquals(1, map.getTerritories().size());
        EasyMock.verify(t1);
    }
}