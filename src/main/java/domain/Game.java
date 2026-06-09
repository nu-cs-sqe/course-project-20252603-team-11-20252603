package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Game {
  private static final int BASE_STARTING_ARMIES = 50;
  private static final int ARMIES_REDUCTION_PER_PLAYER = 5;
  private static final int MIN_NUMBER_OF_PLAYERS = 2;
  private static final int MAX_NUMBER_OF_PLAYERS = 6;
  private final List<Player> players;
  private final GameMap map;
  private final DeckManager deckManager;
  private final Random random;
  private GameState gameState = GameState.SETUP;
  private Optional<Player> winner = Optional.empty();
  private int currentPlayerIndex = -1;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification = "Random is shared so the test harness can seed it for deterministic "
          + "shuffling. GameMap is the shared aggregate root for the territory graph; cloning "
          + "it would create orphan territories that drift from real game state. Class is "
          + "non-final because EasyMock subclasses it to mock in tests."
  )
  public Game(List<Player> players, GameMap map, DeckManager deckManager, Random random) {
    validatePlayers(players);
    validateMap(map);
    if (deckManager == null) {
      throw new IllegalArgumentException("DeckManager cannot be null.");
    }
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null.");
    }
    this.players = players;
    this.map = map;
    this.random = random;
    this.deckManager = deckManager;
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
    int next = (currentPlayerIndex + 1) % players.size();
    while (players.get(next).isEliminated()) {
      next = (next + 1) % players.size();
    }
    currentPlayerIndex = next;
  }

  public Player getCurrentActivePlayer() {
    if (currentPlayerIndex < 0) {
      throw new IllegalStateException("Game not started; call chooseFirstPlayer() first.");
    }
    Player current = players.get(currentPlayerIndex);
    if (!current.isEliminated()) {
      return current;
    }
    int next = (currentPlayerIndex + 1) % players.size();
    while (players.get(next).isEliminated()) {
      next = (next + 1) % players.size();
    }
    return players.get(next);
  }

  public void startGame() {
    shuffleDeck();
    assignTerritories();
    distributeStartingTroops();
    chooseFirstPlayer();
  }

  public void shuffleDeck() {
    deckManager.shuffle();
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
    return deckManager.getDrawPileSize();
  }

  public List<RiskCard> getDeck() {
    return deckManager.getDrawPile();
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Optional<Player> getWinner() {
    return winner;
  }

  public RiskCard drawCard() {
    return deckManager.draw();
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Random is shared so the test harness can seed it for deterministic behavior."
  )
  public Random getRandom() {
    return random;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public void setWinner(Player winner) {
    this.winner = Optional.ofNullable(winner);
  }
}
