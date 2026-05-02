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

    public int getPlayerCount() { return players.size(); }
    public IGameMap getMap() { return map; }
}
