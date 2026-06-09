package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class DeckManagerTests {
  private List<Territory> makeTerritoryMocks(int count) {
    List<Territory> ts = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ts.add(EasyMock.createMock(Territory.class));
    }
    return ts;
  }

  private List<RiskCard> makeCardMocks(int count) {
    List<RiskCard> cs = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cs.add(EasyMock.createMock(RiskCard.class));
    }
    return cs;
  }

  private void expectIdentityShuffle(Random random, int size) {
    for (int i = size; i > 1; i--) {
      EasyMock.expect(random.nextInt(i)).andReturn(i - 1);
    }
  }
  
  private void expectRotationShuffle(Random random, int size) {
    for (int i = size; i > 1; i--) {
      EasyMock.expect(random.nextInt(i)).andReturn(0);
    }
  }

  @Test
  public void constructor_nullRandom_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new DeckManager(null));
  }

  @Test
  public void seededConstructor_nullRandom_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new DeckManager(null, new ArrayList<>()));
  }

  @Test
  public void seededConstructor_nullInitialDrawPile_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);
    assertThrows(IllegalArgumentException.class, () -> new DeckManager(random, null));
    EasyMock.verify(random);
  }

  @Test
  public void seededConstructor_emptyInitialDrawPile_bothPilesEmpty() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);
    DeckManager dm = new DeckManager(random, new ArrayList<>());
    assertEquals(0, dm.size());
    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
    EasyMock.verify(random);
  }

  @Test
  public void seededConstructor_oneCard_drawPileHoldsThatCardDiscardEmpty() {
    Random random = EasyMock.createMock(Random.class);
    RiskCard card = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(random, card);
    DeckManager dm = new DeckManager(random, Arrays.asList(card));
    assertEquals(1, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
    assertSame(card, dm.getDrawPile().get(0));
    EasyMock.verify(random, card);
  }

  @Test
  public void seededConstructor_fortyFourCards_drawPileHoldsAllDiscardEmpty() {
    Random random = EasyMock.createMock(Random.class);
    List<RiskCard> cards = makeCardMocks(44);
    EasyMock.replay(random);
    cards.forEach(EasyMock::replay);
    DeckManager dm = new DeckManager(random, cards);
    assertEquals(44, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
    EasyMock.verify(random);
    cards.forEach(EasyMock::verify);
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
  public void buildDeck_emptyTerritories_producesTwoWildcardsOnly() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(new ArrayList<>());

    assertEquals(2, dm.size());
    assertEquals(2, dm.getDrawPileSize());
    for (RiskCard c : dm.getDrawPile()) {
      assertEquals(RiskCardType.WILDCARD, c.getType());
      assertNull(c.getTerritory());
    }

    EasyMock.verify(random);
  }

  @Test
  public void buildDeck_oneTerritory_producesInfantryPlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    Territory a = EasyMock.createMock(Territory.class);
    EasyMock.replay(random, a);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(Collections.singletonList(a));

    assertEquals(3, dm.size());
    List<RiskCard> cards = dm.getDrawPile();
    assertEquals(RiskCardType.INFANTRY, cards.get(0).getType());
    assertSame(a, cards.get(0).getTerritory());
    assertEquals(RiskCardType.WILDCARD, cards.get(1).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(2).getType());

    EasyMock.verify(random, a);
  }

  @Test
  public void buildDeck_threeTerritories_producesOneOfEachTypePlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);

    List<RiskCard> cards = dm.getDrawPile();
    assertEquals(5, dm.size());
    assertEquals(RiskCardType.INFANTRY, cards.get(0).getType());
    assertEquals(RiskCardType.CAVALRY, cards.get(1).getType());
    assertEquals(RiskCardType.ARTILLERY, cards.get(2).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(3).getType());
    assertEquals(RiskCardType.WILDCARD, cards.get(4).getType());

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void buildDeck_fortyTwoTerritories_producesEvenDistributionPlusTwoWildcards() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(42);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);

    int infantry = 0;
    int cavalry = 0;
    int artillery = 0;
    int wildcard = 0;
    for (RiskCard c : dm.getDrawPile()) {
      switch (c.getType()) {
        case INFANTRY:
          infantry++;
          break;
        case CAVALRY:
          cavalry++;
          break;
        case ARTILLERY:
          artillery++;
          break;
        case WILDCARD:
          wildcard++;
          break;
        default:
          break;
      }
    }

    assertEquals(44, dm.size());
    assertEquals(14, infantry);
    assertEquals(14, cavalry);
    assertEquals(14, artillery);
    assertEquals(2, wildcard);

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void buildDeck_calledTwice_replacesContents() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> first = makeTerritoryMocks(3);
    List<Territory> second = makeTerritoryMocks(6);
    EasyMock.replay(random);
    first.forEach(EasyMock::replay);
    second.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(first);
    assertEquals(5, dm.size());
    dm.buildDeck(second);
    assertEquals(8, dm.size());

    EasyMock.verify(random);
    first.forEach(EasyMock::verify);
    second.forEach(EasyMock::verify);
  }

  @Test
  public void buildDeck_calledAfterReturnCards_clearsDiscardPile() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    RiskCard returned = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(random, returned);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    dm.returnCards(Collections.singletonList(returned));
    assertEquals(1, dm.getDiscardPileSize());

    dm.buildDeck(ts);
    assertEquals(0, dm.getDiscardPileSize());
    assertEquals(5, dm.getDrawPileSize());

    EasyMock.verify(random, returned);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void shuffle_drawNonEmptyDiscardEmpty_reordersDrawDiscardUntouched() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3); // drawPile size 5
    expectRotationShuffle(random, 5);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    List<RiskCard> before = new ArrayList<>(dm.getDrawPile());

    dm.shuffle();

    assertEquals(5, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());
    assertNotEquals(before, new ArrayList<>(dm.getDrawPile()));

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void shuffle_drawAndDiscardNonEmpty_onlyDrawShuffledDiscardUntouched() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3); // drawPile size 5
    List<RiskCard> discarded = makeCardMocks(3);
    expectIdentityShuffle(random, 5);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);
    discarded.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    dm.returnCards(discarded);

    dm.shuffle();

    assertEquals(5, dm.getDrawPileSize());
    assertEquals(3, dm.getDiscardPileSize());

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
    discarded.forEach(EasyMock::verify);
  }

  @Test
  public void shuffle_drawEmptyDiscardNonEmpty_mergesDiscardIntoDrawAndShuffles() {
    Random random = EasyMock.createMock(Random.class);
    List<RiskCard> discarded = makeCardMocks(2);
    expectIdentityShuffle(random, 2);
    EasyMock.replay(random);
    discarded.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.returnCards(discarded);

    dm.shuffle();

    assertEquals(2, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());

    EasyMock.verify(random);
    discarded.forEach(EasyMock::verify);
  }

  @Test
  public void shuffle_bothPilesEmpty_noOpNoException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    dm.shuffle();

    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());

    EasyMock.verify(random);
  }

  @Test
  public void shuffle_invokesRandomNextIntWithDescendingPileSizes() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3); // drawPile size 5
    expectIdentityShuffle(random, 5); // expects nextInt(5), (4), (3), (2)
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    dm.shuffle();

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void draw_drawPileSizeOne_returnsCardDrawBecomesEmpty() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(new ArrayList<>()); // 2 wildcards
    dm.draw(); // reduce to size 1
    assertEquals(1, dm.getDrawPileSize());

    RiskCard card = dm.draw();

    assertEquals(RiskCardType.WILDCARD, card.getType());
    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());

    EasyMock.verify(random);
  }

  @Test
  public void draw_drawPileSizeTwo_returnsTopAndShrinksByOne() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(new ArrayList<>()); // 2 wildcards
    int before = dm.getDrawPileSize();

    RiskCard card = dm.draw();

    assertEquals(RiskCardType.WILDCARD, card.getType());
    assertEquals(before - 1, dm.getDrawPileSize());

    EasyMock.verify(random);
  }

  @Test
  public void draw_drawEmptyDiscardNonEmpty_autoReshufflesThenDraws() {
    Random random = EasyMock.createMock(Random.class);
    RiskCard recycled = EasyMock.createMock(RiskCard.class);
    // shuffle on size 1 makes 0 nextInt calls
    EasyMock.replay(random, recycled);

    DeckManager dm = new DeckManager(random);
    dm.returnCards(Collections.singletonList(recycled));

    RiskCard card = dm.draw();

    assertSame(recycled, card);
    assertEquals(0, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());

    EasyMock.verify(random, recycled);
  }

  @Test
  public void draw_bothPilesEmpty_throwsIllegalStateException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    assertThrows(IllegalStateException.class, dm::draw);

    EasyMock.verify(random);
  }

  @Test
  public void returnCards_null_throwsIllegalArgumentException() {
    Random random = EasyMock.createMock(Random.class);
    EasyMock.replay(random);

    DeckManager dm = new DeckManager(random);
    assertThrows(IllegalArgumentException.class, () -> dm.returnCards(null));

    EasyMock.verify(random);
  }

  @Test
  public void returnCards_emptyList_isNoOp() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    int drawBefore = dm.getDrawPileSize();

    dm.returnCards(new ArrayList<>());

    assertEquals(drawBefore, dm.getDrawPileSize());
    assertEquals(0, dm.getDiscardPileSize());

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void returnCards_oneCard_addsToDiscardDrawUnchanged() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    RiskCard returned = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(random, returned);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    int drawBefore = dm.getDrawPileSize();

    dm.returnCards(Collections.singletonList(returned));

    assertEquals(drawBefore, dm.getDrawPileSize());
    assertEquals(1, dm.getDiscardPileSize());

    EasyMock.verify(random, returned);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void returnCards_threeCards_addsAllToDiscardDrawUnchanged() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    List<RiskCard> returned = makeCardMocks(3);
    EasyMock.replay(random);
    ts.forEach(EasyMock::replay);
    returned.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    int drawBefore = dm.getDrawPileSize();

    dm.returnCards(returned);
    assertEquals(drawBefore, dm.getDrawPileSize());
    assertEquals(3, dm.getDiscardPileSize());

    EasyMock.verify(random);
    ts.forEach(EasyMock::verify);
    returned.forEach(EasyMock::verify);
  }

  @Test
  public void size_invariantHoldsAcrossBothPiles() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    RiskCard returned = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(random, returned);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    dm.returnCards(Collections.singletonList(returned));

    assertEquals(6, dm.size());
    assertEquals(5, dm.getDrawPileSize());
    assertEquals(1, dm.getDiscardPileSize());

    EasyMock.verify(random, returned);
    ts.forEach(EasyMock::verify);
  }

  @Test
  public void getDrawPile_returnedViewRejectsMutation() {
    Random random = EasyMock.createMock(Random.class);
    List<Territory> ts = makeTerritoryMocks(3);
    RiskCard rogue = EasyMock.createMock(RiskCard.class);
    EasyMock.replay(random, rogue);
    ts.forEach(EasyMock::replay);

    DeckManager dm = new DeckManager(random);
    dm.buildDeck(ts);
    List<RiskCard> view = dm.getDrawPile();

    assertThrows(UnsupportedOperationException.class, () -> view.add(rogue));

    EasyMock.verify(random, rogue);
    ts.forEach(EasyMock::verify);
  }
}
