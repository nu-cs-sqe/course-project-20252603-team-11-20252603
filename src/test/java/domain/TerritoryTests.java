package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TerritoryTests {

    @Test
    public void addTroops_negativeTroops_fail(){
        Player p1  = EasyMock.createMock(Player.class);
        Territory t1 = new Territory(p1, 5);

        int input = -1;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            t1.addTroops(input);
        });

        String expectedMessage = "Input must be non-negative integer";
        assertEquals(expectedMessage, exception.getMessage());
    }


}
