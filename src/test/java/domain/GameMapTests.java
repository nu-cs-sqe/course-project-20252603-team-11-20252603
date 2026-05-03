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
}