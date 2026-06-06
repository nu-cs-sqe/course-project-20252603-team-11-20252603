package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class CardTradePhaseTests {

  @Test
  public void run_firstCall_awardsBonusToPlayerAndIncrements() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.expect(player.getAvailableTroops()).andReturn(10);
    EasyMock.expect(tradeBonus.getValue()).andReturn(4);
    player.setAvailableTroops(14);
    EasyMock.expectLastCall().once();
    tradeBonus.increment();
    EasyMock.expectLastCall().once();
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false);
    phase.run();

    EasyMock.verify(player, tradeBonus);
  }

  @Test
  public void validateSet_invalidThreeCardSet_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.CAVALRY).anyTimes();
    EasyMock.replay(player, tradeBonus, card1, card2, card3);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false);
    assertFalse(phase.validateSet(List.of(card1, card2, card3)));
  }

  @Test
  public void validateSet_validThreeCardSet_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    EasyMock.expect(card1.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card2.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.expect(card3.getType()).andReturn(RiskCardType.INFANTRY).anyTimes();
    EasyMock.replay(player, tradeBonus, card1, card2, card3);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false);
    assertTrue(phase.validateSet(List.of(card1, card2, card3)));
  }

  @Test
  public void isComplete_mandatoryTrue_playerHoldsFiveCards_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.expect(player.getCardCount()).andReturn(5).anyTimes();
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true);
    assertFalse(phase.isComplete());
  }

  @Test
  public void isComplete_mandatoryTrue_playerHoldsFourCards_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    EasyMock.expect(player.getCardCount()).andReturn(4).anyTimes();
    EasyMock.replay(player, tradeBonus);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true);
    assertTrue(phase.isComplete());
  }

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
