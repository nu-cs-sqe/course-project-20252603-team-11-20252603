package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RiskCardTests {

    @Test
    public void constructor_nullRiskCardType_throwsIllegalArgumentException() {
        Player p = EasyMock.createMock(Player.class);
        Territory t = new Territory("TestTerritory", p, 1);
        assertThrows(IllegalArgumentException.class, () -> new RiskCard(null, t));
    }

    @Test
    public void constructor_nullTerritory_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new RiskCard(RiskCardType.INFANTRY, null));
    }

}
