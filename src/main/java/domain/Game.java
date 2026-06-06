package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Game {
  private static final int BASE_STARTING_ARMIES = 50;
  private static final int ARMIES_REDUCTION_PER_PLAYER = 5;
  private static final int MIN_NUMBER_OF_PLAYERS = 2;
  private static final int MAX_NUMBER_OF_PLAYERS = 6;
  private final List<Player> players;
  private final GameMap map;
  private final List<RiskCard> deck;
  private final Random random;
  private int currentPlayerIndex = -1;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Random is shared so the test harness can seed it for deterministic shuffling. "
          + "GameMap is the shared aggregate root for the territory graph; cloning it would "
          + "create orphan territories that drift from real game state."
  )
  public Game(List<Player> players, GameMap map, List<RiskCard> deck, Random random) {
    validatePlayers(players);
    validateMap(map);
    this.players = new ArrayList<>(players);
    this.map = map;
    this.deck = new ArrayList<>(deck);
    this.random = random;
  }

  private static void validatePlayers(List<Player> players) {
    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null.");
    }
    if (players.size() < MIN_NUMBER_OF_PLAYERS || players.size() > MAX_NUMBER_OF_PLAYERS) {
      throw new IllegalArgumentException("Game requires between 2 and 6 players.");
    }
    if (players.stream().anyMatch(p -> p == null)) {
      throw new IllegalArgumentException("Players list cannot contain null.");
    }
  }

  private static void validateMap(GameMap map) {
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null.");
    }
  }

  private static int calculateStartingArmies(int playerCount) {
    return BASE_STARTING_ARMIES - (ARMIES_REDUCTION_PER_PLAYER * playerCount);
  }

  public void assignTerritories() {
    List<Territory> territories = new ArrayList<>(map.getTerritories());
    Collections.shuffle(territories, random);
    for (int i = 0; i < territories.size(); i++) {
      Player player = players.get(i % players.size());
      Territory territory = territories.get(i);
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

  public void advanceToNextPlayer() {
    if (currentPlayerIndex < 0) {
      throw new IllegalStateException("Game not started; call chooseFirstPlayer() first.");
    }
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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

  public int getPlayerCount() {
    return players.size();
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "GameMap is the shared aggregate root for the territory graph; "
          + "callers need the live reference to read game state."
  )
  public GameMap getMap() {
    return map;
  }

  public int getDeckSize() {
    return deck.size();
  }

  public List<RiskCard> getDeck() {
    return Collections.unmodifiableList(deck);
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}
