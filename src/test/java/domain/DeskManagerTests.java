package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeskManagerTests {
  // Helpers
  private List<Territory> makeTerritoryMocks(int count) {
    List<Territory> ts = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ts.add(EasyMock.createMock(Territory.class));
    }
    return ts;
  }

  @Test
  public void constructor_nullRandom_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new DeckManager(null));
  }

  @Test
  public void constructor_validRandom_initialPilesAreEmpty() {
    Random random = EasyMock.createMock(Random.class);
    DeckManager dm = new DeckManager(random);
    assertEquals(0, dm.size());
    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
  }

  @Test
  public void buildDeck_nullTerritories_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    DeckManager dm = new DeckManager(random);
    assertThrows(IllegalArgumentException.class, () -> dm.buildDeck(null));
  }

  @Test
  public void buildDeck_listContainsNull_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    Territory a = EasyMock.createMock(Territory.class);
    Territory c = EasyMock.createMock(Territory.class);
    EasyMock.replay(random, a, c);

    DeckManager dm = new DeckManager(random);
    List<Territory> ts = Arrays.asList(a, null, c);
    assertThrows(IllegalArgumentException.class, () -> dm.buildDeck(ts));

    EasyMock.verify(random, a, c);
  }

  @Test
  public void buildDeck_oneTerritory_producesInfantryPlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    Territory a = EasyMock.createMock(Territory.class);
    EasyMock.replay(random, a);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(Arrays.asList(a));

    assertEquals(3, dm.size());
    List<RiskCard> cards = dm.getDrawPile();
    assertEquals(RiskCardType.INFANTRY, cards.get(0).getType());
    assertSame(a, cards.get(0).getTerritory());
    assertEquals(RiskCardType.WILDCARD, cards.get(1).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(2).getType());

    EasyMock.verify(random, a);
  }
  @Test
  public void buildDeck_threeTerritories_producesOneOfEachTypePlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);

    List<RiskCard> cards = dm.getDrawPile();
    assertEquals(5, dm.size());
    assertEquals(RiskCardType.INFANTRY, cards.get(0).getType());
    assertEquals(RiskCardType.CAVALRY, cards.get(1).getType());
    assertEquals(RiskCardType.ARTILLERY, cards.get(2).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(3).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(4).getType());

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }
  @Test
  public void buildDeck_fortyTwoTerritories_producesEvenDistributionPlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(42);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);

    int infantry = 0;
    int cavalry = 0;
    int artillery = 0;
    int wildcard = 0;
    for (RiskCard c : dm.getDrawPile()) {
      switch (c.getType()) {
        case INFANTRY: infantry++; break;
        case CAVALRY: cavalry++; break;
        case ARTILLERY: artillery++; break;
        case WILDCARD: wildcard++; break;
        default: break;
      }
    }

    assertEquals(44, dm.size());
    assertEquals(14, infantry);
    assertEquals(14, cavalry);
    assertEquals(14, artillery);
    assertEquals(2, wildcard);

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }
  @Test
  public void buildDeck_calledTwice_replacesContents() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> first = makeTerritoryMocks(3);
    List<Territory> second = makeTerritoryMocks(6);
    EasyMock.replay(random);
    first.forEach(EasyMock::replay);
    second.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(first);
    assertEquals(5, dm.size());
    dm.buildDeck(second);
    assertEquals(8, dm.size());

    EasyMock.verify(random);
    first.forEach(EasyMock::verify);
    second.forEach(EasyMock::verify);
  }

}
