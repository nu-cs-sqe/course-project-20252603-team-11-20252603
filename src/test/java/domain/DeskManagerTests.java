package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
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

}
