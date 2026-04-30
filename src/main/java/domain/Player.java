package domain;

public class Player {
    public Player(String name) {
        throw new IllegalArgumentException("Player name cannot be null or empty");
    }
}
