package domain;

import java.util.List;

public class Game {
    private final List<Player> players;
    private final IGameMap map;

    public Game(List<Player> players, IGameMap map) {
        if (players == null) {
            throw new IllegalArgumentException("Players list cannot be null.");
        }
        if (players.size() < 2) {
            throw new IllegalArgumentException("Game requires at least 2 players.");
        }
        this.players = players;
        this.map = map;
    }

    public int getPlayerCount() { return players.size(); }
    public IGameMap getMap() { return map; }
}
