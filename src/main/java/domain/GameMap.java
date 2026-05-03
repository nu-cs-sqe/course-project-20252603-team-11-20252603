package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap implements IGameMap {
    private final List<ITerritory> territories;

    public GameMap() {
        this.territories = new ArrayList<>();
    }

    @Override
    public List<ITerritory> getTerritories() {
        return Collections.unmodifiableList(territories);
    }

    @Override
    public void addTerritory(ITerritory territory) {
        if (territory == null) {
            throw new IllegalArgumentException("Territory cannot be null.");
        }
        if (!territories.contains(territory)) {
            territories.add(territory);
        }
    }

    @Override
    public void addConnection(ITerritory a, ITerritory b) {
        throw new IllegalArgumentException("Territories cannot be null.");
    }

    @Override
    public List<ITerritory> getNeighbors(ITerritory territory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean areAdjacent(ITerritory a, ITerritory b) {
        throw new UnsupportedOperationException();
    }
}