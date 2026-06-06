package ui;

import domain.AtlaMapData;
import domain.GameMap;
import domain.Territory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MapView extends Pane {

  private static final double MAP_WIDTH = 1100.0;
  private static final double MAP_HEIGHT = 750.0;
  private static final double HEX_RADIUS = 32.0;
  private static final double EDGE_WIDTH = 1.0;
  private static final Color EDGE_COLOR = Color.DARKGRAY;
  private static final Color COLOR_MOON = Color.LIGHTGRAY;
  private static final Color COLOR_BA_SING_SE = Color.LIGHTGREEN;
  private static final Color COLOR_FIRE = Color.SALMON;
  private static final Color COLOR_OMASHU = Color.TAN;
  private static final Color COLOR_OCEAN = Color.LIGHTSKYBLUE;
  private static final String BACKGROUND_CSS = "-fx-background-color: aliceblue;";

  private static final Map<String, double[]> POSITIONS = buildPositions();
  private static final Map<String, String> MSG_KEYS = buildMsgKeys();
  private static final Map<String, Color> COLORS = buildColors();

  public MapView() {
    setPrefSize(MAP_WIDTH, MAP_HEIGHT);
    setStyle(BACKGROUND_CSS);
    GameMap gameMap = new AtlaMapData().buildMap();
    getChildren().addAll(buildEdges(gameMap), buildNodes(gameMap));
  }

  private static Group buildEdges(GameMap gameMap) {
    Group group = new Group();
    Set<String> drawn = new HashSet<>();
    for (Territory ta : gameMap.getTerritories()) {
      double[] posA = POSITIONS.get(ta.getName());
      for (Territory tb : gameMap.getNeighbors(ta)) {
        String ek = edgeKey(ta.getName(), tb.getName());
        if (drawn.add(ek)) {
          double[] posB = POSITIONS.get(tb.getName());
          Line line = new Line(posA[0], posA[1], posB[0], posB[1]);
          line.setStroke(EDGE_COLOR);
          line.setStrokeWidth(EDGE_WIDTH);
          group.getChildren().add(line);
        }
      }
    }
    return group;
  }

  private static Group buildNodes(GameMap gameMap) {
    Group group = new Group();
    for (Territory territory : gameMap.getTerritories()) {
      String name = territory.getName();
      double[] pos = POSITIONS.get(name);
      String key = MSG_KEYS.get(name);
      Color color = COLORS.get(name);
      TerritoryNode node = new TerritoryNode(
          territory, color, pos[0], pos[1], HEX_RADIUS, key);
      group.getChildren().add(node);
    }
    return group;
  }

  private static String edgeKey(String nameA, String nameB) {
    return nameA.compareTo(nameB) < 0 ? nameA + "|" + nameB : nameB + "|" + nameA;
  }

  private static Map<String, double[]> buildPositions() {
    Map<String, double[]> pm = new HashMap<>();
    pm.put("Northern Tundra",       new double[]{110.0,  75.0});
    pm.put("Frost Hills",           new double[]{200.0,  75.0});
    pm.put("Moon Tribe",            new double[]{155.0, 145.0});
    pm.put("Bin-Er",                new double[]{275.0, 115.0});
    pm.put("Yue Bay",               new double[]{355.0,  80.0});
    pm.put("Wulong",                new double[]{305.0, 170.0});
    pm.put("Western Air Temple",    new double[]{190.0, 215.0});
    pm.put("Zigan",                 new double[]{445.0, 115.0});
    pm.put("Northern Air Temple",   new double[]{540.0,  85.0});
    pm.put("Northern Mountains",    new double[]{625.0, 145.0});
    pm.put("Taihua",                new double[]{550.0, 190.0});
    pm.put("Ba Sing Se City",       new double[]{645.0, 230.0});
    pm.put("Ba Sing Se Province",   new double[]{715.0, 290.0});
    pm.put("Continental Corridor",  new double[]{620.0, 315.0});
    pm.put("Taku",                  new double[]{540.0, 330.0});
    pm.put("Green Farmlands",       new double[]{585.0, 400.0});
    pm.put("Charmeleon Province",   new double[]{685.0, 385.0});
    pm.put("Sun Isles",             new double[]{860.0, 260.0});
    pm.put("Burning Gates",         new double[]{935.0, 320.0});
    pm.put("Caldera City",          new double[]{860.0, 355.0});
    pm.put("Black Cliffs",          new double[]{940.0, 420.0});
    pm.put("Keonso",                new double[]{860.0, 445.0});
    pm.put("Fire Archipelago",      new double[]{940.0, 505.0});
    pm.put("Whale Tail Isle",       new double[]{860.0, 540.0});
    pm.put("Hei Bei",               new double[]{415.0, 265.0});
    pm.put("Great Divide",          new double[]{470.0, 320.0});
    pm.put("Serpent Pass",          new double[]{525.0, 385.0});
    pm.put("Full Moon Bay",         new double[]{450.0, 405.0});
    pm.put("Omashu",                new double[]{385.0, 450.0});
    pm.put("Western Si Wong Desert", new double[]{305.0, 440.0});
    pm.put("Eastern Si Wong Desert", new double[]{455.0, 500.0});
    pm.put("Heart Farmlands",       new double[]{375.0, 500.0});
    pm.put("Chin",                  new double[]{485.0, 550.0});
    pm.put("Gao Ling",              new double[]{390.0, 565.0});
    pm.put("Eastern Air Temple",    new double[]{565.0, 560.0});
    pm.put("Seafoam Isles",         new double[]{495.0, 620.0});
    pm.put("Hakoda Island",         new double[]{410.0, 660.0});
    pm.put("Shimsom Isle",          new double[]{635.0, 630.0});
    pm.put("Southern Air Temple",   new double[]{705.0, 565.0});
    pm.put("Ocean Tribe",           new double[]{300.0, 665.0});
    pm.put("Southern Tundra",       new double[]{190.0, 640.0});
    pm.put("Icy Plains",            new double[]{100.0, 670.0});
    return pm;
  }

  private static Map<String, String> buildMsgKeys() {
    Map<String, String> mk = new HashMap<>();
    mk.put("Northern Tundra",       "territory.northernTundra");
    mk.put("Frost Hills",           "territory.frostHills");
    mk.put("Moon Tribe",            "territory.moonTribe");
    mk.put("Bin-Er",                "territory.binEr");
    mk.put("Yue Bay",               "territory.yueBay");
    mk.put("Wulong",                "territory.wulong");
    mk.put("Western Air Temple",    "territory.westernAirTemple");
    mk.put("Zigan",                 "territory.zigan");
    mk.put("Northern Air Temple",   "territory.northernAirTemple");
    mk.put("Northern Mountains",    "territory.northernMountains");
    mk.put("Taihua",                "territory.taihua");
    mk.put("Ba Sing Se City",       "territory.baSingSeCity");
    mk.put("Ba Sing Se Province",   "territory.baSingSeProvince");
    mk.put("Continental Corridor",  "territory.continentalCorridor");
    mk.put("Taku",                  "territory.taku");
    mk.put("Green Farmlands",       "territory.greenFarmlands");
    mk.put("Charmeleon Province",   "territory.charmeleonProvince");
    mk.put("Sun Isles",             "territory.sunIsles");
    mk.put("Burning Gates",         "territory.burningGates");
    mk.put("Caldera City",          "territory.calderaCity");
    mk.put("Black Cliffs",          "territory.blackCliffs");
    mk.put("Keonso",                "territory.keonso");
    mk.put("Fire Archipelago",      "territory.fireArchipelago");
    mk.put("Whale Tail Isle",       "territory.whaleTailIsle");
    mk.put("Hei Bei",               "territory.heiBei");
    mk.put("Great Divide",          "territory.greatDivide");
    mk.put("Serpent Pass",          "territory.serpentPass");
    mk.put("Full Moon Bay",         "territory.fullMoonBay");
    mk.put("Omashu",                "territory.omashu");
    mk.put("Western Si Wong Desert", "territory.westernSiWongDesert");
    mk.put("Eastern Si Wong Desert", "territory.easternSiWongDesert");
    mk.put("Heart Farmlands",       "territory.heartFarmlands");
    mk.put("Chin",                  "territory.chin");
    mk.put("Gao Ling",              "territory.gaoLing");
    mk.put("Eastern Air Temple",    "territory.easternAirTemple");
    mk.put("Seafoam Isles",         "territory.seafoamIsles");
    mk.put("Hakoda Island",         "territory.hakodaIsland");
    mk.put("Shimsom Isle",          "territory.shimsomIsle");
    mk.put("Southern Air Temple",   "territory.southernAirTemple");
    mk.put("Ocean Tribe",           "territory.oceanTribe");
    mk.put("Southern Tundra",       "territory.southernTundra");
    mk.put("Icy Plains",            "territory.icyPlains");
    return mk;
  }

  private static Map<String, Color> buildColors() {
    Map<String, Color> cm = new HashMap<>();
    cm.put("Northern Tundra",       COLOR_MOON);
    cm.put("Frost Hills",           COLOR_MOON);
    cm.put("Moon Tribe",            COLOR_MOON);
    cm.put("Bin-Er",                COLOR_MOON);
    cm.put("Yue Bay",               COLOR_MOON);
    cm.put("Wulong",                COLOR_MOON);
    cm.put("Western Air Temple",    COLOR_MOON);
    cm.put("Zigan",                 COLOR_BA_SING_SE);
    cm.put("Northern Air Temple",   COLOR_BA_SING_SE);
    cm.put("Northern Mountains",    COLOR_BA_SING_SE);
    cm.put("Taihua",                COLOR_BA_SING_SE);
    cm.put("Ba Sing Se City",       COLOR_BA_SING_SE);
    cm.put("Ba Sing Se Province",   COLOR_BA_SING_SE);
    cm.put("Continental Corridor",  COLOR_BA_SING_SE);
    cm.put("Taku",                  COLOR_BA_SING_SE);
    cm.put("Green Farmlands",       COLOR_BA_SING_SE);
    cm.put("Charmeleon Province",   COLOR_BA_SING_SE);
    cm.put("Sun Isles",             COLOR_FIRE);
    cm.put("Burning Gates",         COLOR_FIRE);
    cm.put("Caldera City",          COLOR_FIRE);
    cm.put("Black Cliffs",          COLOR_FIRE);
    cm.put("Keonso",                COLOR_FIRE);
    cm.put("Fire Archipelago",      COLOR_FIRE);
    cm.put("Whale Tail Isle",       COLOR_FIRE);
    cm.put("Hei Bei",               COLOR_OMASHU);
    cm.put("Great Divide",          COLOR_OMASHU);
    cm.put("Serpent Pass",          COLOR_OMASHU);
    cm.put("Full Moon Bay",         COLOR_OMASHU);
    cm.put("Omashu",                COLOR_OMASHU);
    cm.put("Western Si Wong Desert", COLOR_OMASHU);
    cm.put("Eastern Si Wong Desert", COLOR_OMASHU);
    cm.put("Heart Farmlands",       COLOR_OMASHU);
    cm.put("Chin",                  COLOR_OMASHU);
    cm.put("Gao Ling",              COLOR_OMASHU);
    cm.put("Eastern Air Temple",    COLOR_OCEAN);
    cm.put("Seafoam Isles",         COLOR_OCEAN);
    cm.put("Hakoda Island",         COLOR_OCEAN);
    cm.put("Shimsom Isle",          COLOR_OCEAN);
    cm.put("Southern Air Temple",   COLOR_OCEAN);
    cm.put("Ocean Tribe",           COLOR_OCEAN);
    cm.put("Southern Tundra",       COLOR_OCEAN);
    cm.put("Icy Plains",            COLOR_OCEAN);
    return cm;
  }
}
