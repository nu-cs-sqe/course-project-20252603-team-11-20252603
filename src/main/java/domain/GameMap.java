package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameMap implements IGameMap {
    private final List<ITerritory> territories;
    private final Map<ITerritory, Set<ITerritory>> adjacency;

    public GameMap() {
        this.territories = new ArrayList<>();
        this.adjacency = new HashMap<>();
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
            adjacency.put(territory, new HashSet<>());
        }
    }

    @Override
    public void addConnection(ITerritory a, ITerritory b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Territories cannot be null.");
        }
        if (a == b) {
            throw new IllegalArgumentException("Cannot connect a territory to itself.");
        }
        if (!territories.contains(a) || !territories.contains(b)) {
            throw new IllegalArgumentException("Both territories must be in the map.");
        }
        adjacency.get(a).add(b);
        adjacency.get(b).add(a);
    }

    @Override
    public List<ITerritory> getNeighbors(ITerritory territory) {
        if (territory == null) {
            throw new IllegalArgumentException("Territory cannot be null.");
        }
        Set<ITerritory> neighbors = adjacency.get(territory);
        if (neighbors == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(neighbors));
    }

    @Override
    public boolean areAdjacent(ITerritory a, ITerritory b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Territories cannot be null.");
        }
        if (!territories.contains(a) || !territories.contains(b)) {
            throw new IllegalArgumentException("Both territories must be in the map.");
        }
        return adjacency.get(a).contains(b);
    }
}