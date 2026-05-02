package domain;

import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    // ! Constructor tests
    @Test
    public void constructor_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null));
    }

    @Test
    public void constructor_emptyName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Player(""));
    }

    @Test
    public void constructor_validName_initialStateIsCorrect() {
        Player player = new Player("Alice");
        assertEquals("Alice", player.getName());
        assertTrue(player.getTerritories().isEmpty());
        assertTrue(player.getCards().isEmpty());
        assertEquals(0, player.getAvailableTroops());
        assertEquals(0, player.getTerritoryCount());
    }

    // ! addTerritory tests
    @Test
    public void addTerritory_nullTerritory_throwsIllegalArgumentException() {
        Player player = new Player("Alice");
        assertThrows(IllegalArgumentException.class, () -> player.addTerritory(null));
    }

    @Test
    public void addTerritory_emptyList_sizeBecomesOne() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);
        player.addTerritory(t1);

        assertEquals(1, player.getTerritoryCount());

        assertTrue(player.getTerritories().contains(t1));
        EasyMock.verify(t1);
    }

    @Test
    public void addTerritory_oneExisting_sizeBecomesTwo() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);

        player.addTerritory(t1);
        assertEquals(1, player.getTerritoryCount());
        assertTrue(player.getTerritories().contains(t1));

        player.addTerritory(t2);
        assertEquals(2, player.getTerritoryCount());
        assertTrue(player.getTerritories().contains(t2));

        EasyMock.verify(t1, t2);
    }

    @Test
    public void addTerritory_duplicateTerritory_sizeRemainsOne() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);

        player.addTerritory(t1);
        player.addTerritory(t1);

        assertEquals(1, player.getTerritoryCount());
        EasyMock.verify(t1);
    }

    // removeTerritory tests
    @Test
    public void removeTerritory_nullTerritory_throwsIllegalArgumentException() {
        Player player = new Player("Alice");
        assertThrows(IllegalArgumentException.class, () -> player.removeTerritory(null));
    }

    @Test
    public void removeTerritory_singleItemList_listBecomesEmpty() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);

        player.addTerritory(t1);
        player.removeTerritory(t1);

        assertEquals(0, player.getTerritoryCount());
        assertFalse(player.getTerritories().contains(t1));

        EasyMock.verify(t1);
    }

    @Test
    public void removeTerritory_multiItemList_onlyTargetRemoved() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);

        player.addTerritory(t1);
        player.addTerritory(t2);
        player.removeTerritory(t1);

        assertEquals(1, player.getTerritoryCount());
        assertFalse(player.getTerritories().contains(t1));
        assertTrue(player.getTerritories().contains(t2));

        EasyMock.verify(t1, t2);
    }

    @Test
    public void removeTerritory_territoryNotInList_noOpSizeUnchanged() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);
        ITerritory t2 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1, t2);

        player.addTerritory(t1);
        player.removeTerritory(t2);

        assertEquals(1, player.getTerritoryCount());

        EasyMock.verify(t1, t2);
    }

    @Test
    public void removeTerritory_emptyList_noOpNoException() {
        Player player = new Player("Alice");
        ITerritory t1 = EasyMock.createMock(ITerritory.class);

        EasyMock.replay(t1);

        assertDoesNotThrow(() -> player.removeTerritory(t1));
        assertEquals(0, player.getTerritoryCount());

        EasyMock.verify(t1);
    }

    // setAvailableTroops tests
    @Test
    public void setAvailableTroops_negativeAmount_throwsIllegalArgumentException() {
        Player player = new Player("Alice");
        assertThrows(IllegalArgumentException.class, () -> player.setAvailableTroops(-1));
    }

    @Test
    public void setAvailableTroops_zero_setsToZero() {
        Player player = new Player("Alice");
        player.setAvailableTroops(0);
        assertEquals(0, player.getAvailableTroops());
    }

    @Test
    public void setAvailableTroops_positiveAmount_setsCorrectly() {
        Player player = new Player("Alice");
        player.setAvailableTroops(5);
        assertEquals(5, player.getAvailableTroops());
    }

}
