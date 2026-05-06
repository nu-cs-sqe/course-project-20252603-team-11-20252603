package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TerritoryTests {

    @Test
    public void constructor_nullName_throwsIllegalArgumentException() {
        Player p1 = EasyMock.createMock(Player.class);
        assertThrows(IllegalArgumentException.class, () -> new Territory(null, p1, 5));
    }

    @Test
    public void constructor_emptyName_throwsIllegalArgumentException() {
        Player p1 = EasyMock.createMock(Player.class);
        assertThrows(IllegalArgumentException.class, () -> new Territory("", p1, 5));
    }

    @Test
    public void addTroops_negativeTroops_fail(){
        Player p1  = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 5);

        int input = -1;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.addTroops(input);
        });

        String expectedMessage = "Input must be non-negative integer";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void addTroops_zeroTroops_fail(){
        Player p1  = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 0);

        int input = 0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.addTroops(input);
        });

        String expectedMessage = "Input must be non-negative integer";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void addTroops_oneTroop_success() {
        Player p1 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 1);

        int input = 1;
        int expected = 2;

        t1.addTroops(input);

        assertEquals(expected, t1.getTroopCount());
    }

    @Test
    public void removeTroops_oneTroop_removeOneTroop_fail() {
        Player p1 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 1);

        int input = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.removeTroops(input);
        });

        String expectedMessage = "Territories must have at least 1 troop";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void removeTroops_oneTroop_removeTwoTroop_fail() {
        Player p1 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 1);

        int input = 2;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.removeTroops(input);
        });

        String expectedMessage = "Territories must have at least 1 troop";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void removeTroops_FiveTroops_removeFourTroops_success() {
        Player p1 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 5);

        int input = 4;
        t1.removeTroops(input);

        assertEquals(1, t1.getTroopCount());
    }

    @Test
    public void conquer_P2ConquersP1_movesZeroTroops_fail() {
        Player p1 = EasyMock.createMock(Player.class);
        Player p2 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.conquer(p2, 0);
        });

        String expectedMessage = "Conquered Territories must have at least 1 troop";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void conquer_P2ConquersP1_movesOneTroops_success() {
        Player p1 = EasyMock.createMock(Player.class);
        Player p2 = EasyMock.createMock(Player.class);
        Territory t1 = new Territory("TestTerritory", p1, 5);

        t1.conquer(p2, 1);

        int expectedTroops = 1;
        assertEquals(p2, t1.getOwner());
        assertEquals(expectedTroops, t1.getTroopCount());
    }

}
