package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    private final List<Player> players;
    private final IGameMap map;
    private final List<IRiskCard> deck;
    private final Random random;
    private int currentPlayerIndex = -1;

    public Game(List<Player> players, IGameMap map, List<IRiskCard> deck, Random random) {
        validatePlayers(players);
        validateMap(map);
        this.players = players;
        this.map = map;
        this.deck = deck;
        this.random = random;
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
        List<ITerritory> territories = new ArrayList<>(map.getTerritories());
        Collections.shuffle(territories, random);
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

    public void startGame() {
        shuffleDeck();
        assignTerritories();
        distributeStartingTroops();
        chooseFirstPlayer();
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void chooseFirstPlayer() {
        currentPlayerIndex = random.nextInt(players.size());
    }

    public int getPlayerCount() { return players.size(); }
    public IGameMap getMap() { return map; }
    public int getDeckSize() { return deck.size(); }
    public List<IRiskCard> getDeck() { return Collections.unmodifiableList(deck); }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
}
