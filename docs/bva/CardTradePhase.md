# CardTradePhase - BVA Analysis

`CardTradePhase` manages the pre-reinforcement card trading phase for a single player's turn.
It is constructed with the current player, the shared trade bonus tracker, and a flag indicating
whether trading is mandatory (player holds ≥ 5 cards). The phase exposes three methods:
`validateSet(cards)` to check a candidate card set, `run()` to execute one trade (award bonus
troops and advance the bonus counter), and `isComplete()` to tell the caller whether another
trade must occur. Card removal from the player's hand is the responsibility of the caller, which
calls `validateSet` before committing a selection and `run()` after removing the cards.

---

### Method under test: `CardTradePhase(Player player, TradeBonus tradeBonus, boolean mandatory)`

Precondition: player and tradeBonus are valid objects. `mandatory` is a Case variable over
{false, true} — the only parameter that drives distinct construction-time behaviour.

**`mandatory` parameter (boolean Case variable):**

- **TC1: mandatory = false → CardTradePhase constructed; isComplete() returns true** ( :white_check_mark: )
  - **State of the system**: player and tradeBonus provided; mandatory = false
  - **Expected output**: CardTradePhase constructed; isComplete() = true (no forced trading)

- **TC2: mandatory = true → CardTradePhase constructed; isComplete() reflects player's card count** ( :white_check_mark: )
  - **State of the system**: player and tradeBonus provided; mandatory = true; player holds 5 cards
  - **Expected output**: CardTradePhase constructed; isComplete() = false (player must still trade)

---

### Method under test: `boolean isComplete()`

Precondition: CardTradePhase constructed. Returns true when no further mandatory trade is
required: always true when mandatory=false; true when mandatory=true and the player has
traded down below the threshold; false when mandatory=true and the player still holds ≥ 5 cards.

**`mandatory` and `player.getCardCount()` (jointly drive completion):**

- **TC3: mandatory = false → true** ( :white_check_mark: )
  - **State of the system**: CardTradePhase constructed with mandatory=false
  - **Expected output**: true (no mandatory requirement; phase is always complete)

- **TC4: mandatory = true, player holds 4 cards (one below mandatory threshold of 5) → true** ( :white_check_mark: )
  - **State of the system**: CardTradePhase constructed with mandatory=true; player holds 4 cards
  - **Expected output**: true (player has traded below the threshold; mandatory obligation met)

- **TC5: mandatory = true, player holds 5 cards (at mandatory threshold) → false** ( :x: )
  - **State of the system**: CardTradePhase constructed with mandatory=true; player holds 5 cards
  - **Expected output**: false (player still holds enough cards to require another trade)

---

### Method under test: `boolean validateSet(List<RiskCard> cards)`

Precondition: CardTradePhase constructed. Delegates to `CardTradeValidator.isValidSet(cards)`.
BVA covers one valid and one invalid combination; full set-type coverage belongs to
CardTradeValidator's own BVA.

**Card set validity (Case variable — valid / invalid):**

- **TC6: valid 3-card set (three of the same non-wildcard type) → true** ( :x: )
  - **State of the system**: 3-card list, all INFANTRY
  - **Expected output**: true

- **TC7: invalid 3-card set (two of same type, one different, no wildcard) → false** ( :x: )
  - **State of the system**: 3-card list, two INFANTRY and one CAVALRY
  - **Expected output**: false

---

### Method under test: `void run()`

Precondition: a valid trade set has been selected and the 3 cards removed from the player's
hand by the caller. `run()` executes one trade: it adds `tradeBonus.getValue()` to the player's
`availableTroops` and calls `tradeBonus.increment()` so future trades are worth more. BVA is on
the number of `run()` calls (Count variable ≥ 1); each call is an independent trade.

**Number of `run()` calls:**

- **TC8: first call to run() → player receives current bonus value; tradeBonus advances** ( :x: )
  - **State of the system**: player has N availableTroops; tradeBonus.getValue() = B
  - **Expected output**: player.getAvailableTroops() = N + B; tradeBonus incremented (next getValue() = B + step)

- **TC9: second call to run() → player receives the already-incremented bonus value** ( :x: )
  - **State of the system**: run() already called once; tradeBonus.getValue() = B + step
  - **Expected output**: player.getAvailableTroops() increases by B + step; tradeBonus incremented again
