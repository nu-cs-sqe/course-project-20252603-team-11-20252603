# AttackPhase - BVA Analysis

### Constructor `AttackPhase(Player attacker, DiceRoller diceRoller, Game game)`

Precondition: all arguments non-null. `conqueredCount` initializes to 0.

- **TC1: valid arguments — conqueredCount initialized to 0** ( :white_check_mark: )
  - **State of the system**: attacker, diceRoller, game provided
  - **Expected output**: getConqueredCount() = 0

---

### Method under test: `boolean canAttack(Territory s, Territory t)`

Returns true iff all of: s is owned by the attacker; s.getTroopCount() ≥ 2;
s and t are adjacent (game.getMap().areAdjacent(s, t)); t is not owned by the attacker.

**s.getTroopCount() (interval, lower bound 2):**

- **TC2: s.getTroopCount() = 1 (below lower bound) → false** ( :white_check_mark: )
  - **State of the system**: s owned by attacker, s has 1 troop, s and t adjacent, t owned by enemy
  - **Expected output**: false

- **TC3: s.getTroopCount() = 2 (lower bound) and all conditions met → true** ( :white_check_mark: )
  - **State of the system**: s owned by attacker, s has 2 troops, s and t adjacent, t owned by enemy
  - **Expected output**: true

**Boolean conditions (each tested independently; all other conditions valid):**

- **TC4: s not owned by attacker → false** ( :white_check_mark: )
  - **State of the system**: s.getOwner() returns a different player
  - **Expected output**: false

- **TC5: s and t not adjacent → false** ( :white_check_mark: )
  - **State of the system**: game.getMap().areAdjacent(s, t) returns false
  - **Expected output**: false

- **TC6: t owned by attacker → false** ( :white_check_mark: )
  - **State of the system**: t.getOwner() returns the attacker
  - **Expected output**: false

---

### Method under test: `void declareAttack(Territory s, Territory t, int n)`

Validates attack parameters. n is the number of attacking dice: interval [1, 3] with additional
constraint n ≤ s.getTroopCount() − 1 (source must retain at least 1 army after attackers commit).

**n count (interval [1, 3]):**

- **TC7: n = 0 (below lower bound) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: valid s, t; n = 0
  - **Expected output**: IllegalArgumentException thrown

- **TC8: n = 1 (lower bound), s.getTroopCount() = 2 → no exception** ( :white_check_mark: )
  - **State of the system**: s owned by attacker, s has 2 troops, s and t adjacent, t owned by enemy
  - **Expected output**: no exception; ownership and adjacency checks made on mocks

- **TC9: n = 3 (upper bound), s.getTroopCount() = 4 → no exception** ( :white_check_mark: )
  - **State of the system**: s owned by attacker, s has 4 troops, s and t adjacent, t owned by enemy
  - **Expected output**: no exception; ownership and adjacency checks made on mocks

- **TC10: n = 4 (above upper bound) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: valid s, t; n = 4
  - **Expected output**: IllegalArgumentException thrown

**Army constraint (n ≤ s.getTroopCount() − 1):**

- **TC11: n > s.getTroopCount() − 1 (e.g., n = 2, s.getTroopCount() = 2) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: n within [1, 3] but s does not have enough armies (needs n + 1)
  - **Expected output**: IllegalArgumentException thrown

**Territory validation:**

- **TC12: s not owned by attacker → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: s.getOwner() returns a different player
  - **Expected output**: IllegalArgumentException thrown

- **TC13: s and t not adjacent → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: game.getMap().areAdjacent(s, t) returns false
  - **Expected output**: IllegalArgumentException thrown

- **TC14: t owned by attacker → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: t.getOwner() returns the attacker
  - **Expected output**: IllegalArgumentException thrown

---

### Method under test: `void resolveBattle(Territory s, Territory t, int n)`

Precondition: `declareAttack(s, t, n)` passed validation. Rolls n attacker dice and
min(2, t.getTroopCount()) defender dice via diceRoller, compares them, and applies losses.
If defenderLosses exhaust t's armies (conquest), conqueredCount is incremented and
t.removeTroops is NOT called (Territory.removeTroops throws at 0; ownership transferred
later by moveInTroops). If no conquest, t.removeTroops(defenderLosses) is called normally.

**Defender dice count (min(2, t.getTroopCount()), interval [1, 2]):**

- **TC15: t.getTroopCount() = 1 (lower bound → defender rolls 1 die)** ( :white_check_mark: )
  - **State of the system**: t has 1 troop; diceRoller.rollDefender(1) mocked
  - **Expected output**: diceRoller.rollDefender(1) called; losses applied

- **TC16: t.getTroopCount() = 2 (upper bound → defender rolls 2 dice)** ( :white_check_mark: )
  - **State of the system**: t has 2 troops; diceRoller.rollDefender(2) mocked
  - **Expected output**: diceRoller.rollDefender(2) called; losses applied

**Post-battle conquest boundary (Count with 0 as conquest threshold):**

- **TC17: no conquest — t has armies remaining after battle** ( :white_check_mark: )
  - **State of the system**: defenderLosses < t.getTroopCount(); mocked dice produce this outcome
  - **Expected output**: s.removeTroops(attackerLosses) called; t.removeTroops(defenderLosses)
    called; conqueredCount remains 0

- **TC18: conquest — defenderLosses exhaust t's armies** ( :white_check_mark: )
  - **State of the system**: defenderLosses = t.getTroopCount(); mocked dice produce this outcome
  - **Expected output**: s.removeTroops(attackerLosses) called; t.removeTroops NOT called;
    conqueredCount = 1

---

### Method under test: `void moveInTroops(Territory s, Territory t, int n)`

Precondition: t was just conquered via resolveBattle. n is the number of troops to move from s
into t. n must be in [lastAttackDice, s.getTroopCount() − 1], where lastAttackDice is the dice
count from the preceding resolveBattle call (stored internally by AttackPhase).

**n count (interval [lastAttackDice, s.getTroopCount() − 1]):**

- **TC19: n < lastAttackDice (below lower bound) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: lastAttackDice = 2; n = 1; s has 5 troops
  - **Expected output**: IllegalArgumentException thrown

- **TC20: n = lastAttackDice (lower bound) — valid** ( :white_check_mark: )
  - **State of the system**: lastAttackDice = 2; n = 2; s has 5 troops; t conquered
  - **Expected output**: t.conquer(attacker, 2) called; s.removeTroops(2) called;
    old owner's territory list updated; attacker's territory list updated

- **TC21: n = s.getTroopCount() − 1 (upper bound) — valid** ( :white_check_mark: )
  - **State of the system**: lastAttackDice = 1; n = s.getTroopCount() − 1; t conquered
  - **Expected output**: t.conquer(attacker, n) called; s.removeTroops(n) called;
    territory ownership updated

- **TC22: n = s.getTroopCount() (above upper bound) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: n equals s.getTroopCount() (would leave source with 0 armies)
  - **Expected output**: IllegalArgumentException thrown

---

### Method under test: `void awardCardIfEarned()`

conqueredCount is a Count variable (≥ 0). Exactly 1 card is drawn from game and given to
attacker iff conqueredCount ≥ 1, regardless of how many territories were conquered.

**conqueredCount (Count, lower bound 0):**

- **TC23: conqueredCount = 0 (no territories conquered) → no card drawn** ( :white_check_mark: )
  - **State of the system**: conqueredCount = 0 (no resolveBattle resulted in conquest)
  - **Expected output**: game.drawCard() not called; attacker.addCard() not called

- **TC24: conqueredCount = 1 (lower bound for card award) → one card drawn** ( :white_check_mark: )
  - **State of the system**: conqueredCount = 1 (one conquest this turn); game.drawCard() mocked
  - **Expected output**: game.drawCard() called exactly once; attacker.addCard(card) called

- **TC25: conqueredCount = 2 (above lower bound) → exactly one card drawn** ( :x: )
  - **State of the system**: conqueredCount = 2; game.drawCard() mocked
  - **Expected output**: game.drawCard() called exactly once (not twice)

---

### Method under test: `void endPhase()`

Postcondition: card awarded if any territory was conquered. endPhase delegates to
awardCardIfEarned; BVA tests verify the observable effects through that delegation.

- **TC26: endPhase with conqueredCount = 0 → no card drawn** ( :x: )
  - **State of the system**: no conquests this turn
  - **Expected output**: game.drawCard() not called; attacker.addCard() not called

- **TC27: endPhase with conqueredCount ≥ 1 → card drawn and given to attacker** ( :x: )
  - **State of the system**: at least one conquest this turn; game.drawCard() mocked
  - **Expected output**: game.drawCard() called once; attacker.addCard(card) called
