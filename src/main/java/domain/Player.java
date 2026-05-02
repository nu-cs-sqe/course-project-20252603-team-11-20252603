package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private final List<ITerritory> territories;
    private final List<IRiskCard> cards;
    private int availableTroops;

    public Player(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        this.name = name;
        this.territories = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.availableTroops = 0;
    }

    public String getName() { return name; }
    public List<ITerritory> getTerritories() { return Collections.unmodifiableList(territories); }
    public List<IRiskCard> getCards() { return Collections.unmodifiableList(cards); }
    public int getAvailableTroops() { return availableTroops; }
    public int getTerritoryCount() { return territories.size(); }

    public void addTerritory(ITerritory territory) {
        if (territory == null) {
            throw new IllegalArgumentException("Territory cannot be null.");
        }
        if (!territories.contains(territory)) territories.add(territory);
    }

    public void removeTerritory(ITerritory territory) {
        if (territory == null) {
            throw new IllegalArgumentException("Territory cannot be null.");
        }
        territories.remove(territory);
    }

    public void setAvailableTroops(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Available troops cannot be negative");
        }
        this.availableTroops = amount;
    }

    public void addCard(IRiskCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null.");
        }
        cards.add(card);
    }

    public void placeTroops(ITerritory territory, int amount) {
        if (territory == null) {
            throw new IllegalArgumentException("Territory cannot be null.");
        }
        if (!territories.contains(territory)) {
            throw new IllegalArgumentException("Territory not owned by player.");
        }

    }
}
