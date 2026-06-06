package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class CardTradeValidatorTests {

  @Test
  public void isValidSet_twoCards_throwsIllegalArgumentException() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(card1, card2);

    CardTradeValidator validator = new CardTradeValidator();
    assertThrows(IllegalArgumentException.class,
        () -> validator.isValidSet(List.of(card1, card2)));

    EasyMock.verify(card1, card2);
  }

  @Test
  public void isValidSet_fourCards_throwsIllegalArgumentException() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    RiskCard card4 = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(card1, card2, card3, card4);

    CardTradeValidator validator = new CardTradeValidator();
    assertThrows(IllegalArgumentException.class,
        () -> validator.isValidSet(List.of(card1, card2, card3, card4)));

    EasyMock.verify(card1, card2, card3, card4);
  }

  @Test
  public void isValidSet_threeInfantryCards_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isValidSet_threeCavalryCards_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isValidSet_threeArtilleryCards_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.ARTILLERY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.ARTILLERY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.ARTILLERY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isValidSet_oneOfEachNonWildcardType_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.ARTILLERY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isValidSet_oneWildcardOneInfantryOneCavalry_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.WILDCARD).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isMandatory_playerHoldsFourCards_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    EasyMock.expect(player.getCardCount()).andReturn(4).anyTimes();
    EasyMock.replay(player);

    CardTradeValidator validator = new CardTradeValidator();
    assertFalse(validator.isMandatory(player));

    EasyMock.verify(player);
  }

  @Test
  public void isValidSet_twoInfantryOneCavalry_returnsFalse() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertFalse(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }

  @Test
  public void isValidSet_twoWildcardsOneInfantry_returnsTrue() {
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.WILDCARD).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.WILDCARD).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.replay(card1, card2, card3);

    CardTradeValidator validator = new CardTradeValidator();
    assertTrue(validator.isValidSet(List.of(card1, card2, card3)));

    EasyMock.verify(card1, card2, card3);
  }
}
