package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class CardTradePhaseTests {

  @Test
  public void constructor_mandatoryTrue_playerHoldsFiveCards_isCompleteReturnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.expect(validator.mustTrade(player)).andReturn(true).anyTimes();
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true, validator);
    assertFalse(phase.isComplete());
  }

  @Test
  public void constructor_mandatoryFalse_isCompleteReturnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    assertTrue(phase.isComplete());
  }

  @Test
  public void isComplete_mandatoryFalse_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    assertTrue(phase.isComplete());
  }

  @Test
  public void isComplete_mandatoryTrue_playerHoldsFourCards_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.expect(validator.mustTrade(player)).andReturn(false).anyTimes();
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true, validator);
    assertTrue(phase.isComplete());
  }

  @Test
  public void isComplete_mandatoryTrue_playerHoldsFiveCards_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.expect(validator.mustTrade(player)).andReturn(true).anyTimes();
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, true, validator);
    assertFalse(phase.isComplete());
  }

  @Test
  public void validateSet_validThreeCardSet_returnsTrue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    List<RiskCard> cards = List.of(card1, card2, card3);
    EasyMock.expect(validator.isValidSet(cards)).andReturn(true);
    EasyMock.replay(player, tradeBonus, validator, card1, card2, card3);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    assertTrue(phase.validateSet(cards));

    EasyMock.verify(validator);
  }

  @Test
  public void validateSet_invalidThreeCardSet_returnsFalse() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    RiskCard card1 = EasyMock.createMock(RiskCard.class);
    RiskCard card2 = EasyMock.createMock(RiskCard.class);
    RiskCard card3 = EasyMock.createMock(RiskCard.class);
    List<RiskCard> cards = List.of(card1, card2, card3);
    EasyMock.expect(validator.isValidSet(cards)).andReturn(false);
    EasyMock.replay(player, tradeBonus, validator, card1, card2, card3);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    assertFalse(phase.validateSet(cards));

    EasyMock.verify(validator);
  }

  @Test
  public void run_firstCall_awardsBonusToPlayerAndIncrements() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.expect(player.getAvailableTroops()).andReturn(10);
    EasyMock.expect(tradeBonus.getValue()).andReturn(4);
    player.setAvailableTroops(14);
    EasyMock.expectLastCall().once();
    tradeBonus.increment();
    EasyMock.expectLastCall().once();
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    phase.run();

    EasyMock.verify(player, tradeBonus);
  }

  @Test
  public void run_secondCall_awardsIncrementedBonusValue() {
    Player player = EasyMock.createMock(Player.class);
    TradeBonus tradeBonus = EasyMock.createMock(TradeBonus.class);
    CardTradeValidator validator = EasyMock.createMock(CardTradeValidator.class);
    EasyMock.expect(player.getAvailableTroops()).andReturn(10).andReturn(14);
    EasyMock.expect(tradeBonus.getValue()).andReturn(4).andReturn(6);
    player.setAvailableTroops(14);
    EasyMock.expectLastCall().once();
    player.setAvailableTroops(20);
    EasyMock.expectLastCall().once();
    tradeBonus.increment();
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(player, tradeBonus, validator);

    CardTradePhase phase = new CardTradePhase(player, tradeBonus, false, validator);
    phase.run();
    phase.run();

    EasyMock.verify(player, tradeBonus);
  }
}
