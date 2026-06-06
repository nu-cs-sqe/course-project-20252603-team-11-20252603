# DeckManager - BVA Analysis

`DeckManager` owns the lifecycle of the Risk deck: building it from a list of territories,
shuffling the draw pile (with auto-recycle from the discard pile when the draw pile is empty),
drawing single cards, and returning traded/discarded cards to the discard pile. State lives in
two `List<RiskCard>` fields (`drawPile`, `discardPile`) and a `Random` used only for shuffling.

The deck composition rule is: one card per territory (types assigned round-robin
INFANTRY → CAVALRY → ARTILLERY) plus exactly 2 WILDCARDs. Wildcard cards have a null
`Territory` (RiskCard allows this only when type is WILDCARD).

---

### Method under test: `DeckManager(Random random)`

Precondition: none. Constructs an empty deck (both piles size 0) bound to the given Random.

**`random` reference (Reference variable, non-null required):**

- **TC1: random = null → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: no DeckManager constructed yet
    - **Expected output**: IllegalArgumentException thrown

- **TC2: random = valid Random → both piles empty** ( :white_large_square: )
    - **State of the system**: DeckManager constructed with a seeded Random
    - **Expected output**: size() = 0, getDrawPileSize() = 0, getDiscardPileSize() = 0

---

### Method under test: `DeckManager(Random random, List<RiskCard> initialDrawPile)`

Precondition: none. Two-arg constructor that pre-seeds the draw pile from an existing list of
cards (the discard pile starts empty). Used by callers (e.g. `Game`) that hold a pre-built
deck and want to wrap it without going through `buildDeck`. Delegates Random null-check to
the single-arg constructor.

**`random` reference (Reference variable, non-null required — same contract as TC1):**

- **TC26: random = null → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: no DeckManager constructed yet; initialDrawPile = empty list
    - **Expected output**: IllegalArgumentException thrown (from the delegated single-arg ctor)

**`initialDrawPile` reference (Reference variable, non-null required):**

- **TC27: initialDrawPile = null → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: no DeckManager constructed yet; random = valid Random
    - **Expected output**: IllegalArgumentException thrown

**`initialDrawPile` size (Count variable ≥ 0):**

- **TC28: initialDrawPile.size = 0 (lower bound) → both piles empty** ( :white_large_square: )
    - **State of the system**: DeckManager constructed with random + empty list
    - **Expected output**: size() = 0, getDrawPileSize() = 0, getDiscardPileSize() = 0

- **TC29: initialDrawPile.size = 1 (one above lower bound) → draw pile holds that card, discard empty** ( :white_large_square: )
    - **State of the system**: DeckManager constructed with random + list of 1 RiskCard
    - **Expected output**: getDrawPileSize() = 1, getDiscardPileSize() = 0, getDrawPile().get(0) is the given card

- **TC30: initialDrawPile.size = 44 (real Risk deck size) → draw pile holds all 44 cards, discard empty** ( :white_large_square: )
    - **State of the system**: DeckManager constructed with random + list of 44 RiskCards
    - **Expected output**: getDrawPileSize() = 44, getDiscardPileSize() = 0

---

### Method under test: `void buildDeck(List<Territory> territories)`

Precondition: DeckManager constructed. Clears both piles, then populates the draw pile with
one RiskCard per territory (types assigned round-robin) followed by exactly 2 WILDCARDs.

**`territories` reference (Reference variable, non-null required, list may contain null entries):**

- **TC3: territories = null → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: fresh DeckManager
    - **Expected output**: IllegalArgumentException thrown

- **TC4: territories contains a null entry → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: fresh DeckManager; territories = [Territory("A"), null, Territory("C")]
    - **Expected output**: IllegalArgumentException thrown

**`territories` size (Count variable ≥ 0, drives draw-pile size = N + 2):**

- **TC5: territories.size = 0 (lower bound) → 2 wildcards only** ( :white_large_square: )
    - **State of the system**: fresh DeckManager; territories = []
    - **Expected output**: size() = 2, all cards have type WILDCARD and null Territory

- **TC6: territories.size = 1 (one above lower bound) → 1 INFANTRY + 2 wildcards** ( :white_large_square: )
    - **State of the system**: fresh DeckManager; territories = [Territory("A")]
    - **Expected output**: size() = 3; card[0] is INFANTRY for "A"; card[1], card[2] are WILDCARDs

- **TC7: territories.size = 3 (one full round of type rotation) → INFANTRY/CAVALRY/ARTILLERY + 2 wildcards** ( :white_large_square: )
    - **State of the system**: fresh DeckManager; territories = [Territory("A"), Territory("B"), Territory("C")]
    - **Expected output**: size() = 5; types in draw order = [INFANTRY, CAVALRY, ARTILLERY, WILDCARD, WILDCARD]

- **TC8: territories.size = 42 (real Risk deck size) → 14 of each non-wildcard type + 2 wildcards** ( :white_large_square: )
    - **State of the system**: fresh DeckManager; territories = 42 distinct Territory objects
    - **Expected output**: size() = 44; counts: INFANTRY = 14, CAVALRY = 14, ARTILLERY = 14, WILDCARD = 2

**State carryover (buildDeck called when piles are non-empty — boundary behavior):**

- **TC9: buildDeck called twice → second call replaces draw pile entirely** ( :white_large_square: )
    - **State of the system**: DeckManager already has a 5-card draw pile from a prior buildDeck(3)
    - **Expected output**: after second call with 6 territories, size() = 8 (6 + 2), with no leftover cards from the first deck

- **TC10: buildDeck called after returnCards → second call clears discard pile too** ( :white_large_square: )
    - **State of the system**: DeckManager has 3 territories built, then returnCards added 1 card to discard
    - **Expected output**: after second buildDeck call, getDiscardPileSize() = 0

---

### Method under test: `void shuffle()`

Precondition: DeckManager constructed. Behavior depends on pile state: if draw is empty and
discard has cards, discard is moved into draw and *then* shuffled; otherwise the draw pile is
shuffled in place. The discard pile is never shuffled in isolation.

**Pile-state cases (drawPile × discardPile, empty vs non-empty):**

- **TC11: drawPile non-empty, discardPile empty → drawPile shuffled, discard untouched** ( :white_large_square: )
    - **State of the system**: drawPile has 42 cards, discardPile is empty
    - **Expected output**: drawPile still size 42 but in a different order than before; discardPile still size 0

- **TC12: drawPile non-empty, discardPile non-empty → only drawPile shuffled, discard untouched** ( :white_large_square: )
    - **State of the system**: drawPile has 5 cards, discardPile has 3 cards
    - **Expected output**: drawPile still size 5 (possibly reordered); discardPile still size 3 unchanged

- **TC13: drawPile empty, discardPile non-empty → discard merged into draw and shuffled** ( :white_large_square: )
    - **State of the system**: drawPile is empty, discardPile has 2 cards
    - **Expected output**: drawPile size 2, discardPile size 0

- **TC14: drawPile empty, discardPile empty → no-op (no exception)** ( :white_large_square: )
    - **State of the system**: both piles empty
    - **Expected output**: both piles still empty, no exception

**Determinism property (Random control — testable via seeded Random per AC):**

- **TC15: two DeckManagers seeded with the same Random produce the same shuffle order** ( :white_large_square: )
    - **State of the system**: dmA and dmB both constructed with new Random(42), both built from identical territories
    - **Expected output**: after shuffle(), the draw piles of dmA and dmB are in the same order (by type sequence)

---

### Method under test: `RiskCard draw()`

Precondition: DeckManager constructed. Removes and returns the top card from the draw pile.
If the draw pile is empty but the discard pile has cards, auto-reshuffles before drawing.
If both piles are empty, throws.

**Pile-state cases (drawPile × discardPile):**

- **TC16: drawPile.size = 1, discardPile empty → returns that card, draw becomes empty** ( :white_large_square: )
    - **State of the system**: drawPile has 1 card, discardPile empty
    - **Expected output**: returned RiskCard is the single card; getDrawPileSize() = 0; getDiscardPileSize() = 0

- **TC17: drawPile.size = 2, discardPile empty → returns top card, draw shrinks by 1** ( :white_large_square: )
    - **State of the system**: drawPile has 2 cards, discardPile empty
    - **Expected output**: returned RiskCard is the original top card; getDrawPileSize() = 1; getDiscardPileSize() = 0

- **TC18: drawPile empty, discardPile.size = 1 → auto-reshuffles, returns the recycled card** ( :white_large_square: )
    - **State of the system**: drawPile empty, discardPile has 1 wildcard
    - **Expected output**: returned card is WILDCARD; getDrawPileSize() = 0; getDiscardPileSize() = 0

- **TC19: drawPile empty, discardPile empty → IllegalStateException** ( :white_large_square: )
    - **State of the system**: both piles empty
    - **Expected output**: IllegalStateException thrown

---

### Method under test: `void returnCards(List<RiskCard> cards)`

Precondition: DeckManager constructed. Appends all given cards to the discard pile. The
draw pile is never modified.

**`cards` reference (Reference variable, non-null required):**

- **TC20: cards = null → IllegalArgumentException** ( :white_large_square: )
    - **State of the system**: fresh DeckManager
    - **Expected output**: IllegalArgumentException thrown

**`cards` size (Count variable ≥ 0, drives discardPile growth):**

- **TC21: cards.size = 0 (lower bound) → no-op** ( :white_large_square: )
    - **State of the system**: DeckManager has 3 cards in drawPile, 0 in discard
    - **Expected output**: drawPile and discardPile sizes unchanged (3, 0)

- **TC22: cards.size = 1 (one above lower bound) → 1 card added to discard, drawPile unchanged** ( :white_large_square: )
    - **State of the system**: DeckManager has 3 cards in drawPile, 0 in discard
    - **Expected output**: drawPile size 3 (unchanged); discardPile size 1

- **TC23: cards.size = 3 (typical trade set size) → 3 cards added to discard, drawPile unchanged** ( :white_large_square: )
    - **State of the system**: DeckManager has 3 cards in drawPile, 0 in discard
    - **Expected output**: drawPile size 3 (unchanged); discardPile size 3

---

### Method under test: `int size()`, `int getDrawPileSize()`, `int getDiscardPileSize()`

`size()` returns the total card count across both piles; the other two return per-pile counts.
Pure accessors with no mutation. Most cases are covered by upstream TCs; this block focuses on
the invariant `size() == getDrawPileSize() + getDiscardPileSize()` across distinct pile states.

- **TC24: invariant holds when both piles non-empty** ( :white_large_square: )
    - **State of the system**: drawPile has 5 cards from buildDeck(3), discardPile has 1 card from returnCards
    - **Expected output**: size() = 6, getDrawPileSize() = 5, getDiscardPileSize() = 1

---

### Method under test: `List<RiskCard> getDrawPile()`

Returns a read-only view of the draw pile. Callers must not be able to mutate the underlying
pile through the returned reference.

- **TC25: returned view rejects mutation → UnsupportedOperationException on add** ( :white_large_square: )
    - **State of the system**: DeckManager with a built draw pile of size 5
    - **Expected output**: calling view.add(...) throws UnsupportedOperationException