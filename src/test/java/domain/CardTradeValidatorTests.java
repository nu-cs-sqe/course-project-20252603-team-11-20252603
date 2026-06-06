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
}
