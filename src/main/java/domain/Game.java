package domain;

import java.util.List;

public class Game {
    private final List<Player> players;
    private final IGameMap map;

    public Game(List<Player> players, IGameMap map) {
        validatePlayers(players);
        validateMap(map);
        this.players = players;
        this.map = map;
    }

    private static void validatePlayers(List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("Players list cannot be null.");
        }
        if (players.size() < 2 || players.size() > 6) {
            throw new IllegalArgumentException("Game requires between 2 and 6 players.");
        }
        if (players.stream().anyMatch(p -> p == null)) {
            throw new IllegalArgumentException("Players list cannot contain null.");
        }
    }

    private static void validateMap(IGameMap map) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null.");
        }
    }

    public void assignTerritories() {
        List<ITerritory> territories = map.getTerritories();
        for (int i = 0; i < territories.size(); i++) {
            Player player = players.get(i % players.size());
            ITerritory territory = territories.get(i);
            player.addTerritory(territory);
            territory.setOwner(player);
            territory.addTroops(1);
        }
    }

    public void distributeStartingTroops() {
        int startingArmies = calculateStartingArmies(players.size());
        for (Player player : players) {
            player.setAvailableTroops(startingArmies - player.getTerritoryCount());
        }
    }

    private static int calculateStartingArmies(int playerCount) {
        return 50 - (5 * playerCount);
    }

    public int getPlayerCount() { return players.size(); }
    public IGameMap getMap() { return map; }
}
