package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PlayerTests {
  // ! Constructor tests
  @Test
  public void constructor_nullName_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new Player(null));
  }

  @Test
  public void constructor_emptyName_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new Player(""));
  }

  @Test
  public void constructor_validName_initialStateIsCorrect() {
    Player player = new Player("Alice");
    assertEquals("Alice", player.getName());
    assertTrue(player.getTerritories().isEmpty());
    assertTrue(player.getCards().isEmpty());
    assertEquals(0, player.getAvailableTroops());
    assertEquals(0, player.getTerritoryCount());
    assertFalse(player.isEliminated());
  }

  // ! addTerritory tests
  @Test
  public void addTerritory_nullTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    assertThrows(IllegalArgumentException.class, () -> player.addTerritory(null));
  }

  @Test
  public void addTerritory_emptyList_sizeBecomesOne() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);
    player.addTerritory(t1);

    assertEquals(1, player.getTerritoryCount());

    assertTrue(player.getTerritories().contains(t1));
    EasyMock.verify(t1);
  }

  @Test
  public void addTerritory_oneExisting_sizeBecomesTwo() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);

    player.addTerritory(t1);
    assertEquals(1, player.getTerritoryCount());
    assertTrue(player.getTerritories().contains(t1));

    player.addTerritory(t2);
    assertEquals(2, player.getTerritoryCount());
    assertTrue(player.getTerritories().contains(t2));

    EasyMock.verify(t1, t2);
  }

  @Test
  public void addTerritory_duplicateTerritory_sizeRemainsOne() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);

    player.addTerritory(t1);
    player.addTerritory(t1);

    assertEquals(1, player.getTerritoryCount());
    EasyMock.verify(t1);
  }

  // removeTerritory tests
  @Test
  public void removeTerritory_nullTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    assertThrows(IllegalArgumentException.class, () -> player.removeTerritory(null));
  }

  @Test
  public void removeTerritory_singleItemList_listBecomesEmpty() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);

    player.addTerritory(t1);
    player.removeTerritory(t1);

    assertEquals(0, player.getTerritoryCount());
    assertFalse(player.getTerritories().contains(t1));

    EasyMock.verify(t1);
  }

  @Test
  public void removeTerritory_multiItemList_onlyTargetRemoved() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);

    player.addTerritory(t1);
    player.addTerritory(t2);
    player.removeTerritory(t1);

    assertEquals(1, player.getTerritoryCount());
    assertFalse(player.getTerritories().contains(t1));
    assertTrue(player.getTerritories().contains(t2));

    EasyMock.verify(t1, t2);
  }

  @Test
  public void removeTerritory_territoryNotInList_noOpSizeUnchanged() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);
    Territory t2 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1, t2);

    player.addTerritory(t1);
    player.removeTerritory(t2);

    assertEquals(1, player.getTerritoryCount());

    EasyMock.verify(t1, t2);
  }

  @Test
  public void removeTerritory_emptyList_noOpNoException() {
    Player player = new Player("Alice");
    Territory t1 = EasyMock.createMock(Territory.class);

    EasyMock.replay(t1);

    assertDoesNotThrow(() -> player.removeTerritory(t1));
    assertEquals(0, player.getTerritoryCount());

    EasyMock.verify(t1);
  }

  // setAvailableTroops tests
  @Test
  public void setAvailableTroops_negativeAmount_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    assertThrows(IllegalArgumentException.class, () -> player.setAvailableTroops(-1));
  }

  @Test
  public void setAvailableTroops_zero_setsToZero() {
    Player player = new Player("Alice");
    player.setAvailableTroops(0);
    assertEquals(0, player.getAvailableTroops());
  }

  @Test
  public void setAvailableTroops_positiveAmount_setsCorrectly() {
    Player player = new Player("Alice");
    player.setAvailableTroops(5);
    assertEquals(5, player.getAvailableTroops());
  }

  // addCard tests
  @Test
  public void addCard_nullCard_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    assertThrows(IllegalArgumentException.class, () -> player.addCard(null));
  }

  @Test
  public void addCard_emptyHand_sizeBecomesOne() {
    Player player = new Player("Alice");
    RiskCard card = EasyMock.createMock(RiskCard.class);

    EasyMock.replay(card);

    player.addCard(card);
    assertEquals(1, player.getCards().size());
    assertTrue(player.getCards().contains(card));

    EasyMock.verify(card);
  }

  @Test
  public void addCard_oneExisting_sizeBecomesTwo() {
    Player player = new Player("Alice");
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);

    EasyMock.replay(card1, card2);

    player.addCard(card1);
    player.addCard(card2);
    assertEquals(2, player.getCards().size());
    assertTrue(player.getCards().contains(card1));
    assertTrue(player.getCards().contains(card2));

    EasyMock.verify(card1, card2);
  }

  @Test
  public void addCard_infantryCard_addedToHand() {
    Player player = new Player("Alice");
    RiskCard card = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card.getType()).andStubReturn(RiskCardType.INFANTRY);

    EasyMock.replay(card);

    player.addCard(card);
    assertEquals(RiskCardType.INFANTRY, player.getCards().get(0).getType());

    EasyMock.verify(card);
  }

  @Test
  public void addCard_cavalryCard_addedToHand() {
    Player player = new Player("Alice");
    RiskCard card = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card.getType()).andStubReturn(RiskCardType.CAVALRY);

    EasyMock.replay(card);

    player.addCard(card);
    assertEquals(RiskCardType.CAVALRY, player.getCards().get(0).getType());

    EasyMock.verify(card);
  }

  @Test
  public void addCard_artilleryCard_addedToHand() {
    Player player = new Player("Alice");
    RiskCard card = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card.getType()).andStubReturn(RiskCardType.ARTILLERY);

    EasyMock.replay(card);

    player.addCard(card);
    assertEquals(RiskCardType.ARTILLERY, player.getCards().get(0).getType());

    EasyMock.verify(card);
  }

  @Test
  public void addCard_wildcardCard_addedToHand() {
    Player player = new Player("Alice");
    RiskCard card = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card.getType()).andStubReturn(RiskCardType.WILDCARD);

    EasyMock.replay(card);

    player.addCard(card);
    assertEquals(RiskCardType.WILDCARD, player.getCards().get(0).getType());

    EasyMock.verify(card);
  }

  @Test
  public void addCard_duplicateCardType_bothAdded() {
    Player player = new Player("Alice");
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);

    EasyMock.replay(card1, card2);

    player.addCard(card1);
    player.addCard(card2);
    assertEquals(2, player.getCards().size());

    EasyMock.verify(card1, card2);
  }

  // placeTroops tests
  @Test
  public void placeTroops_nullTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    player.setAvailableTroops(5);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(null, 1));
  }

  @Test
  public void placeTroops_unownedTerritory_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);

    EasyMock.replay(t);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, 1));

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_amountZero_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);

    EasyMock.replay(t);

    player.addTerritory(t);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, 0));

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_amountNegative_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);

    EasyMock.replay(t);

    player.addTerritory(t);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, -1));

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_amountExceedsAvailable_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);
    EasyMock.replay(t);

    player.addTerritory(t);

    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, 6));
    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_amountOne_troopsPlacedAndAvailableDecremented() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);
    t.addTroops(1);

    EasyMock.expectLastCall().once();
    EasyMock.replay(t);

    player.addTerritory(t);
    player.placeTroops(t, 1);
    assertEquals(4, player.getAvailableTroops());

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_amountEqualsAvailable_availableBecomesZero() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    player.setAvailableTroops(5);
    t.addTroops(5);

    EasyMock.expectLastCall().once();
    EasyMock.replay(t);

    player.addTerritory(t);
    player.placeTroops(t, 5);
    assertEquals(0, player.getAvailableTroops());

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_availableZeroAmountZero_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);

    player.addTerritory(t);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, 0));

    EasyMock.verify(t);
  }

  @Test
  public void placeTroops_availableZeroAmountOne_throwsIllegalArgumentException() {
    Player player = new Player("Alice");
    Territory t = EasyMock.createMock(Territory.class);

    EasyMock.replay(t);

    player.addTerritory(t);
    assertThrows(IllegalArgumentException.class, () -> player.placeTroops(t, 1));

    EasyMock.verify(t);
  }

  @Test
  public void isEliminated_newPlayer_defaultsFalse() {
    Player player = new Player("Alice");
    assertFalse(player.isEliminated());
  }

  // calculateReinforcements tests
  @ParameterizedTest
  @CsvSource({"0,  3", "1,  3", "2,  3", "3,  3", "9,  3", "10, 3", "11, 3", "12, 4", "30, 10"})
  public void calculateReinforcements_nTerritories_returnsExpected(int n, int expected) {
    Player player = new Player("Alice");

    for (int i = 0; i < n; i++) {
      Territory t = EasyMock.createMock(Territory.class);
      EasyMock.replay(t);
      player.addTerritory(t);
    }

    assertEquals(expected, player.calculateReinforcements());
  }
}
