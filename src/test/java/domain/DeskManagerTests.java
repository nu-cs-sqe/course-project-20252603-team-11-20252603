package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import java.util.Random;

public class DeskManagerTests {
  private static final int FIXED_SEED = 42;

  @Test
  public void constructor_nullRandom_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new DeckManager(null));
  }

  @Test
  public void constructor_validRandom_initialPilesAreEmpty() {
    DeckManager dm = new DeckManager(new Random(FIXED_SEED));
    assertEquals(0, dm.size());
    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
  }

}
