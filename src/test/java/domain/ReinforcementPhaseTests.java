package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ReinforcementPhaseTests {

    @Test
    public void validatePlacement_territoryNotOwned_returnFalse() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);
        List<Territory> territories = new ArrayList<>();

        EasyMock.expect(player.getTerritories()).andStubReturn(territories);
        EasyMock.replay(player);

        ReinforcementPhase reinforcements = new ReinforcementPhase(player);
        int troops = 1;

        assertFalse(reinforcements.validatePlacement(troops, territory));
        EasyMock.verify(player);
    }

}
