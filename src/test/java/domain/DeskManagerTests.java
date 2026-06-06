package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


}
