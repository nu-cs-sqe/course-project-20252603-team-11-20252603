package domain;

import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
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
}
