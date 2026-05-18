# BattleResult - BVA Analysis

### Method under test: `int getAttackerLosses()` / `int getDefenderLosses()`

Precondition: `BattleResult` has been produced by `DiceRoller.compare(a, d)` with valid
pre-rolled dice. Losses are determined by `min(|a|, |d|)` pair-wise comparisons of dice
sorted descending. Each comparison: attacker die > defender die → defender loses 1 army;
otherwise (tie or less) → attacker loses 1 army.

**1v1 — 1 comparison; attacker losses ∈ {0, 1}:**

- **TC1: attacker [3] vs defender [2] — attacker wins** ( :white_check_mark: )
    - **State of the system**: DiceRoller.compare([3], [2]) called
    - **Expected output**: getAttackerLosses() = 0, getDefenderLosses() = 1

- **TC2: attacker [3] vs defender [3] — tie (defender wins by rule)** ( :white_check_mark: )
    - **State of the system**: DiceRoller.compare([3], [3]) called
    - **Expected output**: getAttackerLosses() = 1, getDefenderLosses() = 0

- **TC3: attacker [2] vs defender [3] — defender wins** ( :x: )
    - **State of the system**: DiceRoller.compare([2], [3]) called
    - **Expected output**: getAttackerLosses() = 1, getDefenderLosses() = 0

**3v2 — 2 comparisons; attacker losses ∈ {0, 1, 2}:**

- **TC4: attacker [6,5,4] vs defender [3,1] — attacker wins both (0 attacker losses, lower bound)** ( :x: )
    - **State of the system**: DiceRoller.compare([6,5,4], [3,1]) called
    - **Expected output**: getAttackerLosses() = 0, getDefenderLosses() = 2

- **TC5: attacker [6,1,1] vs defender [5,5] — split result (1 loss each)** ( :x: )
    - **State of the system**: DiceRoller.compare([6,1,1], [5,5]) called
    - **Expected output**: getAttackerLosses() = 1, getDefenderLosses() = 1

- **TC6: attacker [1,1,1] vs defender [6,6] — defender wins both (2 attacker losses, upper bound)** ( :x: )
    - **State of the system**: DiceRoller.compare([1,1,1], [6,6]) called
    - **Expected output**: getAttackerLosses() = 2, getDefenderLosses() = 0

**Unequal dice counts — only min(|a|, |d|) comparisons are made:**

- **TC7: 2v1 — attacker [4,3] vs defender [2] — 1 comparison using highest pair** ( :x: )
    - **State of the system**: DiceRoller.compare([4,3], [2]) called
    - **Expected output**: getAttackerLosses() = 0, getDefenderLosses() = 1

- **TC8: 1v2 — attacker [5] vs defender [6,4] — 1 comparison using highest pair** ( :x: )
    - **State of the system**: DiceRoller.compare([5], [6,4]) called
    - **Expected output**: getAttackerLosses() = 1, getDefenderLosses() = 0

---

### Method under test: `boolean isConquered()`

`conquered` is set by `DiceRoller.compare()` based on whether the target territory's army
count is reduced to zero after applying `defenderLosses`. It is a boolean flag stored
in the result.

- **TC9: territory not conquered** ( :x: )
    - **State of the system**: BattleResult produced with conquered=false (defender has armies remaining after losses)
    - **Expected output**: isConquered() = false

- **TC10: territory conquered** ( :x: )
    - **State of the system**: BattleResult produced with conquered=true (defender army count reaches 0)
    - **Expected output**: isConquered() = true

