package domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtlaMapData {

  private enum Faction {
    MOON_TRIBE,
    BA_SING_SE_KINGDOM,
    FIRE_NATION,
    OMASHU_KINGDOM,
    OCEAN_TRIBE
  }

  private static final Map<Faction, List<String>> FACTION_TERRITORIES =
      buildFactionTerritories();

  private static final String[][] INTRA_EDGES = {
    {"Northern Tundra", "Frost Hills"},
    {"Northern Tundra", "Moon Tribe"},
    {"Frost Hills", "Bin-Er"},
    {"Moon Tribe", "Bin-Er"},
    {"Bin-Er", "Yue Bay"},
    {"Yue Bay", "Wulong"},
    {"Wulong", "Western Air Temple"},
    {"Zigan", "Northern Air Temple"},
    {"Zigan", "Northern Mountains"},
    {"Northern Air Temple", "Northern Mountains"},
    {"Northern Mountains", "Taihua"},
    {"Taihua", "Ba Sing Se City"},
    {"Ba Sing Se City", "Ba Sing Se Province"},
    {"Ba Sing Se Province", "Continental Corridor"},
    {"Continental Corridor", "Taku"},
    {"Taku", "Green Farmlands"},
    {"Green Farmlands", "Charmeleon Province"},
    {"Charmeleon Province", "Ba Sing Se Province"},
    {"Sun Isles", "Burning Gates"},
    {"Sun Isles", "Caldera City"},
    {"Burning Gates", "Caldera City"},
    {"Caldera City", "Black Cliffs"},
    {"Black Cliffs", "Keonso"},
    {"Keonso", "Fire Archipelago"},
    {"Fire Archipelago", "Whale Tail Isle"},
    {"Hei Bei", "Great Divide"},
    {"Great Divide", "Serpent Pass"},
    {"Serpent Pass", "Full Moon Bay"},
    {"Full Moon Bay", "Omashu"},
    {"Omashu", "Western Si Wong Desert"},
    {"Omashu", "Eastern Si Wong Desert"},
    {"Western Si Wong Desert", "Heart Farmlands"},
    {"Eastern Si Wong Desert", "Heart Farmlands"},
    {"Heart Farmlands", "Chin"},
    {"Chin", "Gao Ling"},
    {"Gao Ling", "Hei Bei"},
    {"Eastern Air Temple", "Seafoam Isles"},
    {"Seafoam Isles", "Hakoda Island"},
    {"Hakoda Island", "Shimsom Isle"},
    {"Shimsom Isle", "Southern Air Temple"},
    {"Southern Air Temple", "Ocean Tribe"},
    {"Ocean Tribe", "Southern Tundra"},
    {"Southern Tundra", "Icy Plains"},
    {"Icy Plains", "Eastern Air Temple"},
  };

  private static final String[][] CROSS_EDGES = {
    {"Whale Tail Isle", "Shimsom Isle"},
    {"Bin-Er", "Zigan"},
    {"Yue Bay", "Zigan"},
    {"Gao Ling", "Seafoam Isles"},
    {"Hakoda Island", "Chin"},
  };

  private static Map<Faction, List<String>> buildFactionTerritories() {
    Map<Faction, List<String>> fm = new EnumMap<>(Faction.class);
    fm.put(Faction.MOON_TRIBE, Arrays.asList(
        "Northern Tundra", "Frost Hills", "Moon Tribe", "Bin-Er",
        "Yue Bay", "Wulong", "Western Air Temple"));
    fm.put(Faction.BA_SING_SE_KINGDOM, Arrays.asList(
        "Zigan", "Northern Air Temple", "Northern Mountains", "Taihua",
        "Ba Sing Se City", "Ba Sing Se Province", "Continental Corridor",
        "Taku", "Green Farmlands", "Charmeleon Province"));
    fm.put(Faction.FIRE_NATION, Arrays.asList(
        "Sun Isles", "Burning Gates", "Caldera City", "Black Cliffs",
        "Keonso", "Fire Archipelago", "Whale Tail Isle"));
    fm.put(Faction.OMASHU_KINGDOM, Arrays.asList(
        "Hei Bei", "Great Divide", "Serpent Pass", "Full Moon Bay",
        "Omashu", "Western Si Wong Desert", "Eastern Si Wong Desert",
        "Heart Farmlands", "Chin", "Gao Ling"));
    fm.put(Faction.OCEAN_TRIBE, Arrays.asList(
        "Eastern Air Temple", "Seafoam Isles", "Hakoda Island", "Shimsom Isle",
        "Southern Air Temple", "Ocean Tribe", "Southern Tundra", "Icy Plains"));
    return Collections.unmodifiableMap(fm);
  }

  public GameMap buildMap() {
    GameMap gameMap = new GameMap();
    Map<String, Territory> byName = new HashMap<>();
    for (List<String> names : FACTION_TERRITORIES.values()) {
      for (String name : names) {
        Territory territory = new Territory(name);
        byName.put(name, territory);
        gameMap.addTerritory(territory);
      }
    }
    for (String[] edge : INTRA_EDGES) {
      addEdge(gameMap, byName, edge);
    }
    for (String[] edge : CROSS_EDGES) {
      addEdge(gameMap, byName, edge);
    }
    return gameMap;
  }

  private static void addEdge(
      GameMap gameMap, Map<String, Territory> byName, String[] edge) {
    gameMap.addConnection(byName.get(edge[0]), byName.get(edge[1]));
  }
}
