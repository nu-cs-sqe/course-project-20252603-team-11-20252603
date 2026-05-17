package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class RiskCardTests {

  @Test
  public void constructor_nullRiskCardType_throwsIllegalArgumentException() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("TestTerritory", p, 1);
    assertThrows(IllegalArgumentException.class, () -> new RiskCard(null, t));
  }

  @Test
  public void constructor_nullTerritory_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new RiskCard(RiskCardType.INFANTRY, null));
  }

  @Test
  public void constructor_validTypeAndTerritory_fieldsStoredCorrectly() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("Alaska", p, 1);
    RiskCard card = new RiskCard(RiskCardType.INFANTRY, t);

    assertEquals(RiskCardType.INFANTRY, card.getType());
    assertEquals(t, card.getTerritory());
  }

  @Test
  public void getType_infantry_returnsInfantry() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("Alaska", p, 1);
    RiskCard card = new RiskCard(RiskCardType.INFANTRY, t);

    assertEquals(RiskCardType.INFANTRY, card.getType());
  }

  @Test
  public void getType_cavalry_returnsCavalry() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("Alaska", p, 1);
    RiskCard card = new RiskCard(RiskCardType.CAVALRY, t);

    assertEquals(RiskCardType.CAVALRY, card.getType());
  }

  @Test
  public void getType_artillery_returnsArtillery() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("Alaska", p, 1);
    RiskCard card = new RiskCard(RiskCardType.ARTILLERY, t);

    assertEquals(RiskCardType.ARTILLERY, card.getType());
  }

  @Test
  public void getType_wildcard_returnsWildcard() {
    Player p = EasyMock.createMock(Player.class);
    Territory t = new Territory("Alaska", p, 1);
    RiskCard card = new RiskCard(RiskCardType.WILDCARD, t);

    assertEquals(RiskCardType.WILDCARD, card.getType());
  }
}
