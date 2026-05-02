package domain;

import java.util.List;

public class Game {
    private final List<Player> players;
    private final IGameMap map;

    public Game(List<Player> players, IGameMap map) {
        if (players == null) {
            throw new IllegalArgumentException("Players list cannot be null.");
        }
        this.players = players;
        this.map = map;
    }
}
