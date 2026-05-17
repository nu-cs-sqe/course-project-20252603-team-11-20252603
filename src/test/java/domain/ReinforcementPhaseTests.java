package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        int troopsToPlace = 3;

        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player);

        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);
        int tryToPlaceTroops = 1;

        assertFalse(reinforcements.validatePlacement(tryToPlaceTroops, territory));
        EasyMock.verify(player);
    }

    @Test
    public void validatePlacement_placeFourWithThreeLeft_validTerritory_returnFalse() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        territories.add(territory);
        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player);

        int troops = 4;

        assertFalse(reinforcements.validatePlacement(troops, territory));
        EasyMock.verify(player);
    }

    @Test
    public void validatePlacement_placeOneWithThreeLeft_validTerritory_returnTrue() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        territories.add(territory);
        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player);

        int troops = 1;

        assertTrue(reinforcements.validatePlacement(troops, territory));
        EasyMock.verify(player);
    }

    @Test
    public void validatePlacement_placeZero_validTerritory_returnFalse() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        territories.add(territory);
        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player);

        int troops = 0;

        assertFalse(reinforcements.validatePlacement(troops, territory));
    }

    @Test
    public void placeTroops_placeOneValidTroop_validTerritory_void() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        territories.add(territory);

        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        EasyMock.expect(player.getTerritories()).andReturn(territories);
        territory.addTroops(1);
        EasyMock.expectLastCall().once();

        EasyMock.replay(player, territory);

        int tryToPlaceTroops = 1;
        reinforcements.placeTroops(tryToPlaceTroops, territory);

        assertEquals(2, reinforcements.getRemaining());
        EasyMock.verify(player,territory);
    }

    @Test
    public void placeTroops_placeMaxValidTroop_validTerritory_void() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        territories.add(territory);

        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        EasyMock.expect(player.getTerritories()).andReturn(territories);
        territory.addTroops(3);
        EasyMock.expectLastCall().once();

        EasyMock.replay(player, territory);

        int tryToPlaceTroops = 3;
        reinforcements.placeTroops(tryToPlaceTroops, territory);

        assertEquals(0, reinforcements.getRemaining());
        EasyMock.verify(player,territory);
    }

    @Test
    public void placeTroops_placeMaxPlusOneTroops_validTerritory_void() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();
        territories.add(territory);

        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player, territory);

        int tryToPlaceTroops = 4;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reinforcements.placeTroops(tryToPlaceTroops, territory)
        );

        assertEquals("Invalid troop placement", exception.getMessage());
        EasyMock.verify(player,territory);
    }

    @Test
    public void placeTroops_invalidTerritory_void() {
        Player player = EasyMock.createMock(Player.class);
        Territory territory = EasyMock.createMock(Territory.class);

        List<Territory> territories = new ArrayList<>();

        int troopsToPlace = 3;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        EasyMock.expect(player.getTerritories()).andReturn(territories);
        EasyMock.replay(player, territory);

        int tryToPlaceTroops = 1;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reinforcements.placeTroops(tryToPlaceTroops, territory)
        );

        assertEquals("Invalid troop placement", exception.getMessage());
        EasyMock.verify(player,territory);
    }

    @Test
    public void isComplete_oneTroopLeft_returnsFalse() {
        Player player = EasyMock.createMock(Player.class);
        int troopsToPlace = 1;
        ReinforcementPhase reinforcements = new ReinforcementPhase(player, troopsToPlace);

        assertFalse(reinforcements.isComplete());
    }

}
