# DiceRoller - BVA Analysis

### Method under test: `List<Integer> rollAttacker(int n)`

`n` is the number of attacking dice to roll (interval [1, 3]). AttackPhase enforces the
army constraint upstream; DiceRoller enforces the count range. Each die value is an
interval [1, 6], produced by `random.nextInt(6) + 1`.

**`n` count (interval [1, 3]):**

- **TC1: n = 0 (below lower bound)** ( :white_check_mark: )
    - **State of the system**: DiceRoller constructed with any Random
    - **Expected output**: IllegalArgumentException thrown

- **TC2: n = 1 (lower bound, valid)** ( :white_check_mark: )
    - **State of the system**: DiceRoller constructed with mocked Random returning nextInt(6) = 2
    - **Expected output**: list of size 1 containing [3]

- **TC3: n = 3 (upper bound, valid)** ( :white_check_mark: )
    - **State of the system**: DiceRoller constructed with mocked Random returning nextInt(6) = 2 each call
    - **Expected output**: list of size 3 containing [3, 3, 3]

- **TC4: n = 4 (above upper bound)** ( :white_check_mark: )
    - **State of the system**: DiceRoller constructed with any Random
    - **Expected output**: IllegalArgumentException thrown

**Die face value (interval [1, 6]):**

- **TC5: random produces minimum — die value = 1 (lower bound)** ( :x: )
    - **State of the system**: DiceRoller constructed with mocked Random; nextInt(6) returns 0
    - **Expected output**: list containing [1]

- **TC6: random produces maximum — die value = 6 (upper bound)** ( :x: )
    - **State of the system**: DiceRoller constructed with mocked Random; nextInt(6) returns 5
    - **Expected output**: list containing [6]

---

### Method under test: `List<Integer> rollDefender(int n)`

`n` is the number of defending dice to roll (interval [1, 2]). Die value bounds [1, 6]
are the same as `rollAttacker` and are covered by TC5–TC6.

**`n` count (interval [1, 2]):**

- **TC7: n = 0 (below lower bound)** ( :x: )
    - **State of the system**: DiceRoller constructed with any Random
    - **Expected output**: IllegalArgumentException thrown

- **TC8: n = 1 (lower bound, valid)** ( :x: )
    - **State of the system**: DiceRoller constructed with mocked Random returning nextInt(6) = 2
    - **Expected output**: list of size 1 containing [3]

- **TC9: n = 2 (upper bound, valid)** ( :x: )
    - **State of the system**: DiceRoller constructed with mocked Random returning nextInt(6) = 2 each call
    - **Expected output**: list of size 2 containing [3, 3]

- **TC10: n = 3 (above upper bound)** ( :x: )
    - **State of the system**: DiceRoller constructed with any Random
    - **Expected output**: IllegalArgumentException thrown

---

### Method under test: `void sortDescending(List<Integer> dice)`

Sorts the list in-place in descending order. Called by `compare()` before constructing
`BattleResult`. Input is a mutable list (not the immutable lists from `List.of()`).

- **TC11: empty list — no change** ( :x: )
    - **State of the system**: dice = []
    - **Expected output**: dice = [] (unchanged, no exception)

- **TC12: single element — no change** ( :x: )
    - **State of the system**: dice = [4]
    - **Expected output**: dice = [4] (unchanged)

- **TC13: already sorted descending — no change** ( :x: )
    - **State of the system**: dice = [5, 3, 1]
    - **Expected output**: dice = [5, 3, 1] (unchanged)

- **TC14: ascending order — reversed** ( :x: )
    - **State of the system**: dice = [1, 3, 5]
    - **Expected output**: dice = [5, 3, 1]

- **TC15: duplicates present — stable relative order** ( :x: )
    - **State of the system**: dice = [3, 1, 3]
    - **Expected output**: dice = [3, 3, 1]

---

### Method under test: `BattleResult compare(List<Integer> a, List<Integer> d)`

Precondition: `a` and `d` are pre-rolled dice lists (from `rollAttacker`/`rollDefender`),
not necessarily sorted. `compare()` calls `sortDescending` on both lists before
constructing `BattleResult`. The returned `BattleResult` always has `conquered=false`
since `compare()` has no access to territory army counts.

**Sorting is applied before comparison (white-box — BattleResult does not sort):**

- **TC16: unsorted attacker dice — sort applied before comparison** ( :x: )
    - **State of the system**: a = [2, 4] (ascending), d = [3]
    - **Expected output**: BattleResult with getAttackerLosses() = 0, getDefenderLosses() = 1
      (correct only if attacker [4] is compared against defender [3], not [2])

- **TC17: unsorted defender dice — sort applied before comparison** ( :x: )
    - **State of the system**: a = [5], d = [4, 6] (ascending)
    - **Expected output**: BattleResult with getAttackerLosses() = 1, getDefenderLosses() = 0
      (correct only if defender [6] is compared against attacker [5], not [4])

**Returned BattleResult has conquered=false:**

- **TC18: compare returns BattleResult with conquered=false** ( :x: )
    - **State of the system**: a = [3], d = [2]
    - **Expected output**: BattleResult.isConquered() = false
