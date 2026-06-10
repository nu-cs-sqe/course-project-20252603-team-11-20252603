package ui;

import domain.AtlaMapData;
import domain.DeckManager;
import domain.Game;
import domain.GameMap;
import domain.Player;
import domain.SetupPhase;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String APP_TITLE = "ATLA Risk";
  private static final String PLAYER_ONE_NAME = "Player 1";
  private static final String PLAYER_TWO_NAME = "Player 2";
  private static final double SCENE_WIDTH = 1300.0;
  private static final double SCENE_HEIGHT = 750.0;

  @Override
  public void start(Stage primaryStage) {
    GameMap map = new AtlaMapData().buildMap();
    Random random = new Random();
    DeckManager deckManager = new DeckManager(random);
    deckManager.buildDeck(map.getTerritories());
    deckManager.shuffle();

    List<Player> players = Arrays.asList(new Player(PLAYER_ONE_NAME), new Player(PLAYER_TWO_NAME));
    Game game = new Game(players, map, deckManager, random);
    new SetupPhase(game).run();

    GameController ctrl = new GameController(game);
    SidebarPanel sidebar = new SidebarPanel();
    MapView mapView = new MapView(ctrl);

    ctrl.setOnStateChanged(
        () -> {
          mapView.refresh();
          sidebar.refresh(ctrl);
        });

    mapView.refresh();

    sidebar.setOnLocaleChanged(
        locale -> {
          i18n.Messages.setLocale(locale);
          mapView.refreshLocale();
          sidebar.refresh(ctrl);
        });

    sidebar.refresh(ctrl);

    ScrollPane scroll = new ScrollPane(mapView);
    scroll.setPannable(true);

    BorderPane root = new BorderPane();
    root.setCenter(scroll);
    root.setRight(sidebar);

    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
