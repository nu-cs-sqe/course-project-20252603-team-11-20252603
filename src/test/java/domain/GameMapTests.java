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

    // ! addConnection tests
    @Test
    public void addConnection_nullA_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t);
        map.addTerritory(t);

        assertThrows(IllegalArgumentException.class, () -> map.addConnection(null, t));
        EasyMock.verify(t);
    }

    @Test
    public void addConnection_nullB_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t);
        map.addTerritory(t);

        assertThrows(IllegalArgumentException.class, () -> map.addConnection(t, null));
        EasyMock.verify(t);
    }

    @Test
    public void addConnection_selfConnection_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);
        map.addTerritory(t1);

        assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t1));
        EasyMock.verify(t1);
    }

    @Test
    public void addConnection_aNotInMap_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t2);

        assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t2));
        EasyMock.verify(t1, t2);
    }

    @Test
    public void addConnection_bNotInMap_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);

        assertThrows(IllegalArgumentException.class, () -> map.addConnection(t1, t2));
        EasyMock.verify(t1, t2);
    }

    @Test
    public void addConnection_validPair_doesNotThrow() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);
        map.addTerritory(t2);

        assertDoesNotThrow(() -> map.addConnection(t1, t2));
        EasyMock.verify(t1, t2);
    }

    @Test
    public void addConnection_duplicatePair_isNoOp() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);
        map.addTerritory(t2);

        map.addConnection(t1, t2);
        assertDoesNotThrow(() -> map.addConnection(t1, t2));
        assertEquals(1, map.getNeighbors(t1).size());

        EasyMock.verify(t1, t2);
    }

    @Test
    public void addConnection_duplicateReversedPair_isNoOp() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);
        map.addTerritory(t2);

        map.addConnection(t1, t2);
        assertDoesNotThrow(() -> map.addConnection(t2, t1));
        assertEquals(1, map.getNeighbors(t1).size());

        EasyMock.verify(t1, t2);
    }

    // ! getNeighbors tests
    @Test
    public void getNeighbors_nullTerritory_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        assertThrows(IllegalArgumentException.class, () -> map.getNeighbors(null));
    }

    @Test
    public void getNeighbors_territoryNotInMap_returnsEmptyList() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);

        assertTrue(map.getNeighbors(t1).isEmpty());
        EasyMock.verify(t1);
    }

    @Test
    public void getNeighbors_territoryWithNoConnections_returnsEmptyList() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);
        map.addTerritory(t1);

        assertTrue(map.getNeighbors(t1).isEmpty());
        EasyMock.verify(t1);
    }

    @Test
    public void getNeighbors_oneNeighbor_returnsSingletonList() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addConnection(t1, t2);

        assertEquals(1, map.getNeighbors(t1).size());
        assertTrue(map.getNeighbors(t1).contains(t2));
        EasyMock.verify(t1, t2);
    }

    @Test
    public void getNeighbors_multipleNeighbors_returnsAllNeighbors() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);
        ITerritory t3 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2, t3);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addTerritory(t3);
        map.addConnection(t1, t2);
        map.addConnection(t1, t3);

        assertEquals(2, map.getNeighbors(t1).size());
        assertTrue(map.getNeighbors(t1).contains(t2));
        assertTrue(map.getNeighbors(t1).contains(t3));
        EasyMock.verify(t1, t2, t3);
    }

    // ! areAdjacent tests
    @Test
    public void areAdjacent_nullA_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t);
        map.addTerritory(t);

        assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(null, t));
        EasyMock.verify(t);
    }

    @Test
    public void areAdjacent_nullB_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t);
        map.addTerritory(t);

        assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t, null));
        EasyMock.verify(t);
    }

    @Test
    public void areAdjacent_aNotInMap_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t2);

        assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t1, t2));
        EasyMock.verify(t1, t2);
    }

    @Test
    public void areAdjacent_bNotInMap_throwsIllegalArgumentException() {
        GameMap map = new GameMap();
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);
        map.addTerritory(t1);

        assertThrows(IllegalArgumentException.class, () -> map.areAdjacent(t1, t2));
        EasyMock.verify(t1, t2);
    }
}