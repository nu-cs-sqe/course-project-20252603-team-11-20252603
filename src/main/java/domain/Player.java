package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
  private final String name;
  private final List<Territory> territories;
  private final List<RiskCard> cards;
  private int availableTroops;
  private boolean isEliminated;

  public Player(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }
    this.name = name;
    this.territories = new ArrayList<>();
    this.cards = new ArrayList<>();
    this.availableTroops = 0;
  }

  public String getName() {
    return name;
  }

  public List<Territory> getTerritories() {
    return Collections.unmodifiableList(territories);
  }

  public List<RiskCard> getCards() {
    return Collections.unmodifiableList(cards);
  }

  public int getAvailableTroops() {
    return availableTroops;
  }

  public void setAvailableTroops(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Available troops cannot be negative");
    }
    this.availableTroops = amount;
  }

  public int getTerritoryCount() {
    return territories.size();
  }

  public void addTerritory(Territory territory) {
    if (territory == null) {
      throw new IllegalArgumentException("Territory cannot be null.");
    }
    if (!territories.contains(territory)) {
      territories.add(territory);
    }
  }

  public void removeTerritory(Territory territory) {
    if (territory == null) {
      throw new IllegalArgumentException("Territory cannot be null.");
    }
    territories.remove(territory);
  }

  public void addCard(RiskCard card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null.");
    }
    cards.add(card);
  }

  public boolean isEliminated() {
    return isEliminated;
  }

  public int calculateReinforcements() {
    return Math.max(3, territories.size() / 3);
  }
}
