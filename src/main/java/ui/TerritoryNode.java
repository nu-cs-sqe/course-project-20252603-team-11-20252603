package ui;

import domain.Territory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import i18n.Messages;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public final class TerritoryNode extends Group {

  private static final double STROKE_WIDTH = 1.5;
  private static final double SELECTED_STROKE_WIDTH = 3.0;
  private static final double OWNER_RADIUS = 8.0;
  private static final double OWNER_OFFSET = 0.55;
  private static final double LABEL_DX = -8.0;
  private static final double LABEL_DY = 5.0;
  private static final int HEX_SIDES = 6;
  private static final Color STROKE_COLOR = Color.BLACK;
  private static final Color SELECTED_STROKE_COLOR = Color.YELLOW;
  private static final Color OWNER_FILL = Color.WHITE;

  private final Territory territory;
  private final String msgKey;
  private final Polygon shape;
  private final Text troopLabel;
  private final Circle ownerMarker;
  private final Tooltip tooltip;

  private Runnable clickHandler = () -> {};

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification =
          "Territory is the shared aggregate domain object; TerritoryNode needs the "
              + "live reference to read troop count and owner for display refresh.")
  public TerritoryNode(
      Territory territory, Color baseColor, double cx, double cy, double radius, String msgKey) {
    this.territory = territory;
    this.msgKey = msgKey;

    this.shape = buildHexagon(cx, cy, radius);
    shape.setFill(baseColor);
    shape.setStroke(STROKE_COLOR);
    shape.setStrokeWidth(STROKE_WIDTH);
    shape.setCursor(Cursor.HAND);

    this.tooltip = new Tooltip(Messages.get(msgKey));
    Tooltip.install(shape, tooltip);

    shape.setOnMouseEntered(e -> shape.setFill(baseColor.brighter()));
    shape.setOnMouseExited(e -> shape.setFill(baseColor));
    shape.setOnMouseClicked(e -> clickHandler.run());

    this.troopLabel =
        new Text(cx + LABEL_DX, cy + LABEL_DY, String.valueOf(territory.getTroopCount()));
    this.ownerMarker =
        buildOwnerMarker(cx + radius * OWNER_OFFSET, cy - radius * OWNER_OFFSET, territory);

    getChildren().addAll(shape, troopLabel, ownerMarker);
  }

  public void setOnTerritoryClicked(Runnable handler) {
    this.clickHandler = handler;
  }

  public void refresh() {
    troopLabel.setText(String.valueOf(territory.getTroopCount()));
  }

  public void refreshOwner(Color color) {
    ownerMarker.setFill(color != null ? color : OWNER_FILL);
    ownerMarker.setVisible(color != null);
  }

  public void refreshLocale() {
    tooltip.setText(Messages.get(msgKey));
  }

  public void setSelected(boolean selected) {
    shape.setStrokeWidth(selected ? SELECTED_STROKE_WIDTH : STROKE_WIDTH);
    shape.setStroke(selected ? SELECTED_STROKE_COLOR : STROKE_COLOR);
  }

  private static Circle buildOwnerMarker(double mx, double my, Territory territory) {
    Circle marker = new Circle(mx, my, OWNER_RADIUS);
    marker.setFill(OWNER_FILL);
    marker.setStroke(STROKE_COLOR);
    marker.setVisible(territory.getOwner() != null);
    return marker;
  }

  private static Polygon buildHexagon(double cx, double cy, double radius) {
    Polygon hex = new Polygon();
    for (int i = 0; i < HEX_SIDES; i++) {
      double angle = Math.PI / 3.0 * i;
      hex.getPoints().add(cx + radius * Math.cos(angle));
      hex.getPoints().add(cy + radius * Math.sin(angle));
    }
    return hex;
  }
}
