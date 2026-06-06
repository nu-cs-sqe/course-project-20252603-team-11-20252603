package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class CardTradePhaseTests {

  @Test
  public void isComplete_mandatoryFalse_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false);
    assertTrue(phase.isComplete());
  }

  @Test
  public void constructor_mandatoryTrue_playerHoldsFiveCards_isCompleteReturnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.expect(player.getCardCount()).andReturn(5).anyTimes();
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true);
    assertFalse(phase.isComplete());
  }

  @Test
  public void constructor_mandatoryFalse_isCompleteReturnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false);
    assertTrue(phase.isComplete());
  }
}
