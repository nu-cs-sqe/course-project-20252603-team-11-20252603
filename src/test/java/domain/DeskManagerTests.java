package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeskManagerTests {
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

}
