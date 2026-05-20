package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameTests {

  // ! Helpers
  private List<Player> makePlayers(int count) {
    List<Player> players = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      players.add(EasyMock.createMock(Player.class));
    }
    return players;
  }

  private GameMap makeMap() {
    return EasyMock.createMock(GameMap.class);
  }

  private void replayAll(List<Player> players, GameMap map) {
    players.forEach(EasyMock::replay);
    EasyMock.replay(map);
  }

  private void verifyAll(List<Player> players, GameMap map) {
    players.forEach(EasyMock::verify);
    EasyMock.verify(map);
  }

  private List<Territory> makeTerritories(int count) {
    List<Territory> territories = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      territories.add(EasyMock.createMock(Territory.class));
    }
    return territories;
  }

  private void expectTerritoryAssignment(Territory territory, Player owner) {
    territory.setOwner(owner);
    territory.addTroops(1);
  }

  private void expectIdentityShuffle(Random random, int size) {
    for (int i = size; i > 1; i--) {
      EasyMock.expect(random.nextInt(i)).andReturn(i - 1);
    }
  }

  private List<RiskCard> makeCards(int count) {
    List<RiskCard> cards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(EasyMock.createMock(RiskCard.class));
    }
    return cards;
  }

  @Test
  public void constructor_nullPlayers_throwsIllegalArgumentException() {
    GameMap map = makeMap();
    EasyMock.replay(map);
    assertThrows(
        IllegalArgumentException.class, () -> new Game(null, map, new ArrayList<>(), new Random()));
    EasyMock.verify(map);
  }

  @Test
  public void constructor_emptyPlayersList_throwsIllegalArgumentException() {
    GameMap map = makeMap();
    EasyMock.replay(map);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Game(new ArrayList<>(), map, new ArrayList<>(), new Random()));
    EasyMock.verify(map);
  }

  @Test
  public void constructor_onePlayer_throwsIllegalArgumentException() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(1);
    replayAll(players, map);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Game(players, map, new ArrayList<>(), new Random()));
    verifyAll(players, map);
  }

  @Test
  public void constructor_twoPlayers_gameConstructedWithPlayersAndMap() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());

    assertEquals(2, game.getPlayerCount());
    assertEquals(map, game.getMap());
    verifyAll(players, map);
  }

  @Test
  public void constructor_sixPlayers_gameConstructedWithPlayersAndMap() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(6);
    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());

    assertEquals(6, game.getPlayerCount());
    assertEquals(map, game.getMap());
    verifyAll(players, map);
  }

  @Test
  public void constructor_sevenPlayers_throwsIllegalArgumentException() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(7);
    replayAll(players, map);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Game(players, map, new ArrayList<>(), new Random()));
    verifyAll(players, map);
  }

  @Test
  public void constructor_playersListContainsNull_throwsIllegalArgumentException() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(1);
    players.add(null);
    players.forEach(
        p -> {
          if (p != null) {
            EasyMock.replay(p);
          }
        });
    EasyMock.replay(map);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Game(players, map, new ArrayList<>(), new Random()));
    players.forEach(
        p -> {
          if (p != null) {
            EasyMock.verify(p);
          }
        });
    EasyMock.verify(map);
  }

  @Test
  public void constructor_nullMap_throwsIllegalArgumentException() {
    List<Player> players = makePlayers(2);
    players.forEach(EasyMock::replay);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Game(players, null, new ArrayList<>(), new Random()));
    players.forEach(EasyMock::verify);
  }

  @Test
  public void assignTerritories_oneTerritory_assignedToFirstPlayerWithOneTroop() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Territory territory = EasyMock.createMock(Territory.class);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(List.of(territory));
    players.get(0).addTerritory(territory);
    expectTerritoryAssignment(territory, players.get(0));

    replayAll(players, map);
    EasyMock.replay(territory, random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    EasyMock.verify(territory, random);
  }

  @Test
  public void assignTerritories_fourTerritoriesTwoPlayers_eachPlayerGetsTwoTerritories() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<Territory> territories = makeTerritories(4);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(territories);
    expectIdentityShuffle(random, 4);
    players.get(0).addTerritory(territories.get(0));
    players.get(0).addTerritory(territories.get(2));
    players.get(1).addTerritory(territories.get(1));
    players.get(1).addTerritory(territories.get(3));
    expectTerritoryAssignment(territories.get(0), players.get(0));
    expectTerritoryAssignment(territories.get(1), players.get(1));
    expectTerritoryAssignment(territories.get(2), players.get(0));
    expectTerritoryAssignment(territories.get(3), players.get(1));

    replayAll(players, map);
    territories.forEach(EasyMock::replay);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    territories.forEach(EasyMock::verify);
    EasyMock.verify(random);
  }

  @Test
  public void assignTerritories_threeTerritoriesTwoPlayers_allAssignedUnevenly() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<Territory> territories = makeTerritories(3);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(territories);
    expectIdentityShuffle(random, 3);
    players.get(0).addTerritory(territories.get(0));
    players.get(0).addTerritory(territories.get(2));
    players.get(1).addTerritory(territories.get(1));
    expectTerritoryAssignment(territories.get(0), players.get(0));
    expectTerritoryAssignment(territories.get(1), players.get(1));
    expectTerritoryAssignment(territories.get(2), players.get(0));

    replayAll(players, map);
    territories.forEach(EasyMock::replay);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    territories.forEach(EasyMock::verify);
    EasyMock.verify(random);
  }

  @Test
  public void
      assignTerritories_twoTerritories_randomProducesSwap_assignmentReflectsShuffledOrder() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<Territory> territories = makeTerritories(2);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(territories);
    EasyMock.expect(random.nextInt(2)).andReturn(0); // causes swap: [t1, t0]
    players.get(0).addTerritory(territories.get(1)); // t1 goes to player[0]
    players.get(1).addTerritory(territories.get(0)); // t0 goes to player[1]
    expectTerritoryAssignment(territories.get(1), players.get(0));
    expectTerritoryAssignment(territories.get(0), players.get(1));

    replayAll(players, map);
    territories.forEach(EasyMock::replay);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    territories.forEach(EasyMock::verify);
    EasyMock.verify(random);
  }

  @Test
  public void
      assignTerritories_twoTerritories_randomProducesNoSwap_assignmentReflectsOriginalOrder() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<Territory> territories = makeTerritories(2);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(territories);
    EasyMock.expect(random.nextInt(2)).andReturn(1); // no swap: [t0, t1]
    players.get(0).addTerritory(territories.get(0)); // t0 goes to player[0]
    players.get(1).addTerritory(territories.get(1)); // t1 goes to player[1]
    expectTerritoryAssignment(territories.get(0), players.get(0));
    expectTerritoryAssignment(territories.get(1), players.get(1));

    replayAll(players, map);
    territories.forEach(EasyMock::replay);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    territories.forEach(EasyMock::verify);
    EasyMock.verify(random);
  }

  @Test
  public void assignTerritories_noTerritories_noTerritoriesAddedToPlayers() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);
    // shuffle of size 0 makes no nextInt calls
    EasyMock.expect(map.getTerritories()).andReturn(new ArrayList<>());
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.assignTerritories();

    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void distributeStartingTroops_twoPlayers_availableTroopsSetTo40MinusTerritoryCount() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);

    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(3);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(2);
    players.get(0).setAvailableTroops(37);
    players.get(1).setAvailableTroops(38);

    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.distributeStartingTroops();

    verifyAll(players, map);
  }

  @Test
  public void distributeStartingTroops_threePlayers_availableTroopsSetTo35MinusTerritoryCount() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(3);

    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(5);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(4);
    EasyMock.expect(players.get(2).getTerritoryCount()).andReturn(5);
    players.get(0).setAvailableTroops(30);
    players.get(1).setAvailableTroops(31);
    players.get(2).setAvailableTroops(30);

    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.distributeStartingTroops();

    verifyAll(players, map);
  }

  @Test
  public void distributeStartingTroops_fourPlayers_availableTroopsSetTo30MinusTerritoryCount() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(4);

    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(3);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(3);
    EasyMock.expect(players.get(2).getTerritoryCount()).andReturn(3);
    EasyMock.expect(players.get(3).getTerritoryCount()).andReturn(3);
    players.get(0).setAvailableTroops(27);
    players.get(1).setAvailableTroops(27);
    players.get(2).setAvailableTroops(27);
    players.get(3).setAvailableTroops(27);

    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.distributeStartingTroops();

    verifyAll(players, map);
  }

  @Test
  public void distributeStartingTroops_fivePlayers_availableTroopsSetTo25MinusTerritoryCount() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(5);

    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(2).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(3).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(4).getTerritoryCount()).andReturn(2);
    for (Player p : players) {
      p.setAvailableTroops(23);
    }

    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.distributeStartingTroops();

    verifyAll(players, map);
  }

  @Test
  public void distributeStartingTroops_sixPlayers_availableTroopsSetTo20MinusTerritoryCount() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(6);

    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(2).getTerritoryCount()).andReturn(2);
    EasyMock.expect(players.get(3).getTerritoryCount()).andReturn(1);
    EasyMock.expect(players.get(4).getTerritoryCount()).andReturn(1);
    EasyMock.expect(players.get(5).getTerritoryCount()).andReturn(1);
    players.get(0).setAvailableTroops(18);
    players.get(1).setAvailableTroops(18);
    players.get(2).setAvailableTroops(18);
    players.get(3).setAvailableTroops(19);
    players.get(4).setAvailableTroops(19);
    players.get(5).setAvailableTroops(19);

    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.distributeStartingTroops();

    verifyAll(players, map);
  }

  @Test
  public void chooseFirstPlayer_twoPlayers_resultZero_lowerBoundChosen() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(2)).andReturn(0);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();

    assertEquals(0, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void chooseFirstPlayer_twoPlayers_resultOne_upperBoundChosen() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(2)).andReturn(1);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();

    assertEquals(1, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void chooseFirstPlayer_sixPlayers_resultZero_lowerBoundChosen() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(6);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(0);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();

    assertEquals(0, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void chooseFirstPlayer_sixPlayers_resultFive_upperBoundChosen() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(6);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(5);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();

    assertEquals(5, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void constructor_validPlayersAndMap_mapStoredCorrectly() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());

    assertEquals(map, game.getMap());
    verifyAll(players, map);
  }

  @Test
  public void startGame_twoPlayersWithTerritoriesAndDeck_allSetupStepsComplete() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<Territory> territories = makeTerritories(2);
    List<RiskCard> deck = makeCards(1);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(territories);
    expectIdentityShuffle(random, 2);
    players.get(0).addTerritory(territories.get(0));
    players.get(1).addTerritory(territories.get(1));
    expectTerritoryAssignment(territories.get(0), players.get(0));
    expectTerritoryAssignment(territories.get(1), players.get(1));
    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(1);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(1);
    players.get(0).setAvailableTroops(39); // 40 - 1 territory
    players.get(1).setAvailableTroops(39);
    EasyMock.expect(random.nextInt(2)).andReturn(0); // chooseFirstPlayer

    replayAll(players, map);
    territories.forEach(EasyMock::replay);
    deck.forEach(EasyMock::replay);
    EasyMock.replay(random);

    Game game = new Game(players, map, deck, random);
    game.startGame();

    assertEquals(0, game.getCurrentPlayerIndex());
    assertEquals(1, game.getDeckSize());
    verifyAll(players, map);
    territories.forEach(EasyMock::verify);
    deck.forEach(EasyMock::verify);
    EasyMock.verify(random);
  }

  @Test
  public void startGame_twoPlayers_currentPlayerIndexAtLowerBound() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(new ArrayList<>());
    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(0);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(0);
    players.get(0).setAvailableTroops(40);
    players.get(1).setAvailableTroops(40);
    EasyMock.expect(random.nextInt(2)).andReturn(0);

    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.startGame();

    assertEquals(0, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void startGame_twoPlayers_currentPlayerIndexAtUpperBound() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);

    EasyMock.expect(map.getTerritories()).andReturn(new ArrayList<>());
    EasyMock.expect(players.get(0).getTerritoryCount()).andReturn(0);
    EasyMock.expect(players.get(1).getTerritoryCount()).andReturn(0);
    players.get(0).setAvailableTroops(40);
    players.get(1).setAvailableTroops(40);
    EasyMock.expect(random.nextInt(2)).andReturn(1);

    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.startGame();

    assertEquals(1, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void shuffleDeck_oneCard_deckStillContainsThatCard() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<RiskCard> deck = makeCards(1);
    RiskCard card = deck.get(0);
    EasyMock.replay(card);
    replayAll(players, map);

    Game game = new Game(players, map, deck, new Random());
    game.shuffleDeck();

    assertEquals(1, game.getDeckSize());
    assertEquals(card, game.getDeck().get(0));
    verifyAll(players, map);
    EasyMock.verify(card);
  }

  @Test
  public void shuffleDeck_twoCards_deckStillContainsBothCards() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<RiskCard> deck = makeCards(2);
    deck.forEach(EasyMock::replay);
    replayAll(players, map);

    Game game = new Game(players, map, deck, new Random());
    game.shuffleDeck();

    assertEquals(2, game.getDeckSize());
    assertTrue(game.getDeck().contains(deck.get(0)));
    assertTrue(game.getDeck().contains(deck.get(1)));
    verifyAll(players, map);
    deck.forEach(EasyMock::verify);
  }

  @Test
  public void shuffleDeck_fortyFourCards_allCardsStillPresent() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    List<RiskCard> deck = makeCards(44);
    deck.forEach(EasyMock::replay);
    replayAll(players, map);

    Game game = new Game(players, map, deck, new Random());
    game.shuffleDeck();

    assertEquals(44, game.getDeckSize());
    deck.forEach(card -> assertTrue(game.getDeck().contains(card)));
    verifyAll(players, map);
    deck.forEach(EasyMock::verify);
  }

  @Test
  public void shuffleDeck_emptyDeck_deckRemainsEmpty() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());
    game.shuffleDeck();

    assertEquals(0, game.getDeckSize());
    verifyAll(players, map);
  }

  @Test
  public void advanceToNextPlayer_gameNotStarted_throwsIllegalStateException() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    replayAll(players, map);

    Game game = new Game(players, map, new ArrayList<>(), new Random());

    assertThrows(IllegalStateException.class, game::advanceToNextPlayer);
    assertEquals(-1, game.getCurrentPlayerIndex());
    verifyAll(players, map);
  }

  @Test
  public void advanceToNextPlayer_twoPlayers_fromZero_advancesToOne() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(2)).andReturn(0);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();
    game.advanceToNextPlayer();

    assertEquals(1, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void advanceToNextPlayer_twoPlayers_fromOne_wrapsToZero() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(2);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(2)).andReturn(1);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();
    game.advanceToNextPlayer();

    assertEquals(0, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }

  @Test
  public void advanceToNextPlayer_sixPlayers_fromFive_wrapsToZero() {
    GameMap map = makeMap();
    List<Player> players = makePlayers(6);
    Random random = EasyMock.createMock(Random.class);
    EasyMock.expect(random.nextInt(6)).andReturn(5);
    replayAll(players, map);
    EasyMock.replay(random);

    Game game = new Game(players, map, new ArrayList<>(), random);
    game.chooseFirstPlayer();
    game.advanceToNextPlayer();

    assertEquals(0, game.getCurrentPlayerIndex());
    verifyAll(players, map);
    EasyMock.verify(random);
  }
}
