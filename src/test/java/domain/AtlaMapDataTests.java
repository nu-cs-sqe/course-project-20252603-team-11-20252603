package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtlaMapDataTests {

  private GameMap gameMap;

  @BeforeEach
  public void setUp() {
    gameMap = new AtlaMapData().buildMap();
  }

  @Test
  public void buildMap_totalTerritoryCount_returns42() {
    assertEquals(42, gameMap.getTerritories().size());
  }

  @Test
  public void buildMap_moonTribeFactionCount_returns7() {
    List<String> names = Arrays.asList(
        "Northern Tundra", "Frost Hills", "Moon Tribe", "Bin-Er",
        "Yue Bay", "Wulong", "Western Air Temple");
    assertEquals(7, countByNames(names));
  }

  @Test
  public void buildMap_baSingSeFactionCount_returns10() {
    List<String> names = Arrays.asList(
        "Zigan", "Northern Air Temple", "Northern Mountains", "Taihua",
        "Ba Sing Se City", "Ba Sing Se Province", "Continental Corridor",
        "Taku", "Green Farmlands", "Charmeleon Province");
    assertEquals(10, countByNames(names));
  }

  @Test
  public void buildMap_fireNationFactionCount_returns7() {
    List<String> names = Arrays.asList(
        "Sun Isles", "Burning Gates", "Caldera City", "Black Cliffs",
        "Keonso", "Fire Archipelago", "Whale Tail Isle");
    assertEquals(7, countByNames(names));
  }

  @Test
  public void buildMap_omashuKingdomFactionCount_returns10() {
    List<String> names = Arrays.asList(
        "Hei Bei", "Great Divide", "Serpent Pass", "Full Moon Bay",
        "Omashu", "Western Si Wong Desert", "Eastern Si Wong Desert",
        "Heart Farmlands", "Chin", "Gao Ling");
    assertEquals(10, countByNames(names));
  }

  @Test
  public void buildMap_oceanTribeFactionCount_returns8() {
    List<String> names = Arrays.asList(
        "Eastern Air Temple", "Seafoam Isles", "Hakoda Island", "Shimsom Isle",
        "Southern Air Temple", "Ocean Tribe", "Southern Tundra", "Icy Plains");
    assertEquals(8, countByNames(names));
  }

  @Test
  public void buildMap_allTerritoriesUnowned_getOwnerReturnsNull() {
    for (Territory territory : gameMap.getTerritories()) {
      assertNull(territory.getOwner(), "Expected unowned: " + territory.getName());
    }
  }

  @Test
  public void buildMap_allTerritoriesZeroTroops_getTroopCountReturnsZero() {
    for (Territory territory : gameMap.getTerritories()) {
      assertEquals(0, territory.getTroopCount(),
          "Expected 0 troops: " + territory.getName());
    }
  }

  // --- helpers ---

  private long countByNames(List<String> names) {
    return gameMap.getTerritories().stream()
        .filter(t -> names.contains(t.getName()))
        .count();
  }

  private Territory findByName(String name) {
    return gameMap.getTerritories().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Territory not found: " + name));
  }

  private void assertBothDirections(String nameA, String nameB) {
    Territory ta = findByName(nameA);
    Territory tb = findByName(nameB);
    assertTrue(gameMap.areAdjacent(ta, tb), nameA + " -> " + nameB);
    assertTrue(gameMap.areAdjacent(tb, ta), nameB + " -> " + nameA);
  }
}
