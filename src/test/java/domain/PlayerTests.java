package domain;

import org.junit.jupiter.api.Test;

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
}
