# CardTradeValidator - BVA Analysis

`CardTradeValidator` is a pure logic class with no state. It answers two questions: is a given
3-card set a legal trade, and must a given player trade right now? All three methods are
stateless — no constructor BVA is needed.

---

### Method under test: `boolean isValidSet(List<RiskCard> cards)`

Precondition: none. Returns true iff the list contains exactly 3 cards that form a legal Risk
trade set. Legal sets are: three cards of the same non-wildcard type (all INFANTRY, all
CAVALRY, or all ARTILLERY); one of each non-wildcard type (INFANTRY + CAVALRY +
ARTILLERY); or any 3-card combination containing at least one WILDCARD.

**`cards` parameter (Collection with fixed size 3):**

- **TC1: cards is null → IllegalArgumentException** ( :x: )
  - **State of the system**: validator constructed
  - **Expected output**: IllegalArgumentException thrown

- **TC2: cards.size() = 2 (one below required size 3) → IllegalArgumentException** ( :x: )
  - **State of the system**: list contains 2 cards
  - **Expected output**: IllegalArgumentException thrown

- **TC3: cards.size() = 4 (one above required size 3) → IllegalArgumentException** ( :x: )
  - **State of the system**: list contains 4 cards
  - **Expected output**: IllegalArgumentException thrown

**Card type combinations (Case variable — all distinct legal and illegal set types):**

- **TC4: [INFANTRY, INFANTRY, INFANTRY] → true** ( :x: )
  - **State of the system**: 3-card list, all INFANTRY
  - **Expected output**: true

- **TC5: [CAVALRY, CAVALRY, CAVALRY] → true** ( :x: )
  - **State of the system**: 3-card list, all CAVALRY
  - **Expected output**: true

- **TC6: [ARTILLERY, ARTILLERY, ARTILLERY] → true** ( :x: )
  - **State of the system**: 3-card list, all ARTILLERY
  - **Expected output**: true

- **TC7: [INFANTRY, CAVALRY, ARTILLERY] → true** ( :x: )
  - **State of the system**: 3-card list, one of each non-wildcard type
  - **Expected output**: true

- **TC8: [WILDCARD, INFANTRY, CAVALRY] → true** ( :x: )
  - **State of the system**: 3-card list containing 1 wildcard
  - **Expected output**: true

- **TC9: [WILDCARD, WILDCARD, INFANTRY] → true** ( :x: )
  - **State of the system**: 3-card list containing 2 wildcards
  - **Expected output**: true

- **TC10: [INFANTRY, INFANTRY, CAVALRY] → false** ( :x: )
  - **State of the system**: 3-card list with 2 of same type and 1 of different, no wildcard
    (the only non-wildcard combination that is neither all-same nor one-of-each)
  - **Expected output**: false

---

### Method under test: `boolean isMandatory(Player player)`

Precondition: called at the start of a player's turn to determine whether card trading is
forced before reinforcement placement. Returns true iff the player holds 5 or more cards
(the threshold at which trading is mandatory per game rules).

**`player.getCardCount()` (Count variable ≥ 0, mandatory threshold 5):**

- **TC11: player.getCardCount() = 4 (one below threshold) → false** ( :x: )
  - **State of the system**: player holds 4 cards
  - **Expected output**: false

- **TC12: player.getCardCount() = 5 (at threshold, lower bound of mandatory range) → true** ( :x: )
  - **State of the system**: player holds 5 cards
  - **Expected output**: true

---

### Method under test: `boolean mustTrade(Player player)`

Precondition: called after each completed trade to determine whether the player must trade
again before proceeding. Returns true iff the player still holds 5 or more cards (same
threshold as `isMandatory`; distinct method because it is invoked mid-phase rather than
at turn start).

**`player.getCardCount()` (Count variable ≥ 0, must-continue threshold 5):**

- **TC13: player.getCardCount() = 4 (one below threshold) → false** ( :x: )
  - **State of the system**: player holds 4 cards after a trade
  - **Expected output**: false

- **TC14: player.getCardCount() = 5 (at threshold, lower bound of must-continue range) → true** ( :x: )
  - **State of the system**: player holds 5 cards after a trade (e.g. received cards from
    eliminated opponent)
  - **Expected output**: true
