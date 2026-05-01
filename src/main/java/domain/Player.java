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
        throw new IllegalArgumentException("Territory cannot be null.");
    }
}
