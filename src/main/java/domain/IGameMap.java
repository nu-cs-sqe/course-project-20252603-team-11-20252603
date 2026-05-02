package domain;

import java.util.List;

public interface IGameMap {
    List<ITerritory> getTerritories();
    void addTerritory(ITerritory territory);
    void addConnection(ITerritory a, ITerritory b);
    List<ITerritory> getNeighbors(ITerritory territory);
    boolean areAdjacent(ITerritory a, ITerritory b);
}
