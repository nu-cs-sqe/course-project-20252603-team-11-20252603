package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckManager {

  private final List<RiskCard> drawPile;
  private final List<RiskCard> discardPile;
  private final Random random;

  public DeckManager(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null.");
    }
    this.random = random;
    this.drawPile = new ArrayList<>();
    this.discardPile = new ArrayList<>();
  }

  public int size() {
    return drawPile.size() + discardPile.size();
  }

  public int getDrawPileSize() {
    return drawPile.size();
  }

  public int getDiscardPileSize() {
    return discardPile.size();
  }
}
