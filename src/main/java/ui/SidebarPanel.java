package ui;

import domain.Player;
import domain.TurnPhase;
import i18n.Messages;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class SidebarPanel extends VBox {

  private static final double SIDEBAR_WIDTH = 200.0;
  private static final double PADDING = 12.0;
  private static final double SPACING = 8.0;
  private static final double WINNER_FONT_SIZE = 16.0;
  private static final double PLAYER_FONT_SIZE = 14.0;
  private static final List<Locale> LOCALES =
      Arrays.asList(Locale.ENGLISH, new Locale("es"));
  private static final List<String> LOCALE_LABELS = Arrays.asList("English", "Español");

  private final Label languageLabel;
  private final ComboBox<String> languagePicker;
  private final Label playerLabel;
  private final Label phaseLabel;
  private final Label troopsLabel;
  private final Label territoriesLabel;
  private final Label cardsLabel;
  private final Label instructionLabel;
  private final Button actionButton;
  private final Label winnerLabel;

  private Consumer<Locale> onLocaleChanged;

  public SidebarPanel() {
    setPrefWidth(SIDEBAR_WIDTH);
    setPadding(new Insets(PADDING));
    setSpacing(SPACING);
    setStyle("-fx-background-color: #f0f0f0;");

    languageLabel = new Label();
    languagePicker = new ComboBox<>();
    languagePicker.getItems().addAll(LOCALE_LABELS);
    languagePicker.setValue(LOCALE_LABELS.get(0));
    languagePicker.setMaxWidth(Double.MAX_VALUE);
    languagePicker.setOnAction(e -> {
      int idx = languagePicker.getItems().indexOf(languagePicker.getValue());
      if (idx >= 0 && onLocaleChanged != null) {
        onLocaleChanged.accept(LOCALES.get(idx));
      }
    });

    playerLabel = new Label();
    playerLabel.setFont(Font.font(null, FontWeight.BOLD, PLAYER_FONT_SIZE));

    phaseLabel = new Label();
    troopsLabel = new Label();
    territoriesLabel = new Label();
    cardsLabel = new Label();

    instructionLabel = new Label();
    instructionLabel.setWrapText(true);
    instructionLabel.setMaxWidth(SIDEBAR_WIDTH - 2 * PADDING);

    actionButton = new Button();
    actionButton.setMaxWidth(Double.MAX_VALUE);

    winnerLabel = new Label();
    winnerLabel.setFont(Font.font(null, FontWeight.BOLD, WINNER_FONT_SIZE));
    winnerLabel.setWrapText(true);
    winnerLabel.setVisible(false);

    getChildren().addAll(
        languageLabel,
        languagePicker,
        new Separator(),
        playerLabel,
        phaseLabel,
        new Separator(),
        troopsLabel,
        territoriesLabel,
        cardsLabel,
        new Separator(),
        instructionLabel,
        actionButton,
        winnerLabel
    );
  }

  public void setOnLocaleChanged(Consumer<Locale> callback) {
    this.onLocaleChanged = callback;
  }

  public void refresh(GameController ctrl) {
    languageLabel.setText(Messages.get("ui.button.language"));

    if (ctrl.isGameOver()) {
      ctrl.getWinner().ifPresent(w ->
          winnerLabel.setText(Messages.get("ui.winner").replace("{0}", w.getName())));
      winnerLabel.setVisible(true);
      actionButton.setDisable(true);
      instructionLabel.setText("");
      phaseLabel.setText("");
      troopsLabel.setVisible(false);
      return;
    }

    winnerLabel.setVisible(false);
    Player player = ctrl.getCurrentPlayer();
    TurnPhase phase = ctrl.getCurrentPhase();

    if (player != null) {
      playerLabel.setText(Messages.get("ui.label.player") + ": " + player.getName());
      territoriesLabel.setText(
          Messages.get("ui.label.territories") + ": " + player.getTerritoryCount());
      cardsLabel.setText(
          Messages.get("ui.label.cards") + ": " + player.getCardCount());
    }

    if (phase == TurnPhase.REINFORCEMENT) {
      phaseLabel.setText(
          Messages.get("ui.label.phase") + ": " + Messages.get("ui.phase.reinforcement"));
      troopsLabel.setText(
          Messages.get("ui.label.troopsRemaining") + ": " + ctrl.getRemainingTroops());
      troopsLabel.setVisible(true);
      instructionLabel.setText(Messages.get("ui.instruction.reinforcement"));
      actionButton.setText(Messages.get("ui.button.donePlacing"));
      actionButton.setDisable(ctrl.getRemainingTroops() > 0);
      actionButton.setOnAction(e -> ctrl.endReinforcementPhase());
    } else if (phase == TurnPhase.ATTACK) {
      phaseLabel.setText(
          Messages.get("ui.label.phase") + ": " + Messages.get("ui.phase.attack"));
      troopsLabel.setVisible(false);
      instructionLabel.setText(Messages.get("ui.instruction.attack"));
      actionButton.setText(Messages.get("ui.button.endAttack"));
      actionButton.setDisable(false);
      actionButton.setOnAction(e -> ctrl.endAttackPhase());
    } else if (phase == TurnPhase.FORTIFICATION) {
      phaseLabel.setText(
          Messages.get("ui.label.phase") + ": " + Messages.get("ui.phase.fortification"));
      troopsLabel.setVisible(false);
      instructionLabel.setText(Messages.get("ui.instruction.fortification"));
      if (ctrl.isFortificationMoved()) {
        actionButton.setText(Messages.get("ui.button.doneMoving"));
        actionButton.setOnAction(e -> ctrl.endFortificationPhase());
      } else {
        actionButton.setText(Messages.get("ui.button.skip"));
        actionButton.setOnAction(e -> ctrl.skipFortification());
      }
      actionButton.setDisable(false);
    }
  }
}
