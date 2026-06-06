package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DeckManager {
  private static final int WILDCARD_COUNT = 2;
  private static final RiskCardType[] TERRITORY_TYPES = {
      RiskCardType.INFANTRY,
      RiskCardType.CAVALRY,
      RiskCardType.ARTILLERY
  };

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

  public void buildDeck(List<Territory> territories) {
    if (territories == null) {
      throw new IllegalArgumentException("Territories list cannot be null.");
    }

    drawPile.clear();
    discardPile.clear();

    int i = 0;
    for (Territory territory : territories) {
      if (territory == null) {
        throw new IllegalArgumentException("Territory in list cannot be null.");
      }

      RiskCardType type = TERRITORY_TYPES[i % TERRITORY_TYPES.length];
      drawPile.add(new RiskCard(type, territory));
      i++;
    }

    for (int w = 0; w < WILDCARD_COUNT; w++) {
      drawPile.add(new RiskCard(RiskCardType.WILDCARD, null));
    }
  }

  public void returnCards(List<RiskCard> cards) {
    if (cards == null) {
      throw new IllegalArgumentException("Cards list cannot be null.");
    }
    discardPile.addAll(cards);
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

  public List<RiskCard> getDrawPile() {
    return Collections.unmodifiableList(drawPile);
  }
}
