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

- **TC1: 2 cards (one below required size 3) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: list contains 2 cards
  - **Expected output**: IllegalArgumentException thrown

- **TC2: 4 cards (one above required size 3) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: list contains 4 cards
  - **Expected output**: IllegalArgumentException thrown

**Card type combinations (Case variable — all distinct legal and illegal set types):**

- **TC3: three INFANTRY cards (all same type) → true** ( :white_check_mark: )
  - **State of the system**: 3-card list, all INFANTRY
  - **Expected output**: true

- **TC4: three CAVALRY cards (all same type) → true** ( :white_check_mark: )
  - **State of the system**: 3-card list, all CAVALRY
  - **Expected output**: true

- **TC5: three ARTILLERY cards (all same type) → true** ( :white_check_mark: )
  - **State of the system**: 3-card list, all ARTILLERY
  - **Expected output**: true

- **TC6: one INFANTRY, one CAVALRY, one ARTILLERY (one of each) → true** ( :white_check_mark: )
  - **State of the system**: 3-card list, one of each non-wildcard type
  - **Expected output**: true

- **TC7: one WILDCARD, one INFANTRY, one CAVALRY (contains wildcard) → true** ( :white_check_mark: )
  - **State of the system**: 3-card list containing 1 wildcard
  - **Expected output**: true

- **TC8: two WILDCARDs, one INFANTRY (contains two wildcards) → true** ( :x: )
  - **State of the system**: 3-card list containing 2 wildcards
  - **Expected output**: true

- **TC9: two INFANTRY, one CAVALRY (not all-same, not one-of-each, no wildcard) → false** ( :x: )
  - **State of the system**: 3-card list with 2 of same type and 1 of different, no wildcard
  - **Expected output**: false

---

### Method under test: `boolean isMandatory(Player player)`

Precondition: called at the start of a player's turn to determine whether card trading is
forced before reinforcement placement. Returns true iff the player holds 5 or more cards
(the threshold at which trading is mandatory per game rules).

**`player.getCardCount()` (Count variable ≥ 0, mandatory threshold 5):**

- **TC10: player holds 4 cards (one below threshold of 5) → false** ( :x: )
  - **State of the system**: player holds 4 cards
  - **Expected output**: false

- **TC11: player holds 5 cards (at threshold, lower bound of mandatory range) → true** ( :x: )
  - **State of the system**: player holds 5 cards
  - **Expected output**: true

---

### Method under test: `boolean mustTrade(Player player)`

Precondition: called after each completed trade to determine whether the player must trade
again before proceeding. Returns true iff the player still holds 5 or more cards (same
threshold as `isMandatory`; distinct method because it is invoked mid-phase rather than
at turn start).

**`player.getCardCount()` (Count variable ≥ 0, must-continue threshold 5):**

- **TC12: player holds 4 cards after a trade (one below threshold of 5) → false** ( :x: )
  - **State of the system**: player holds 4 cards after a trade
  - **Expected output**: false

- **TC13: player holds 5 cards after a trade (at threshold, lower bound of must-continue range) → true** ( :x: )
  - **State of the system**: player holds 5 cards after a trade (e.g. received cards from
    eliminated opponent)
  - **Expected output**: true
