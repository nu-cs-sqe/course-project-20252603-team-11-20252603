package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String APP_TITLE = "ATLA Risk";
  private static final double SCENE_WIDTH = 1100.0;
  private static final double SCENE_HEIGHT = 750.0;

  @Override
  public void start(Stage primaryStage) {
    ScrollPane scroll = new ScrollPane(new MapView());
    scroll.setPannable(true);
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(new Scene(scroll, SCENE_WIDTH, SCENE_HEIGHT));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
