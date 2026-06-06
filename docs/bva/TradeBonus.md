# TradeBonus - BVA Analysis

`TradeBonus` tracks the globally incrementing card-trade bonus value. It is constructed with
an initial troop award and a fixed step size, then incremented once per completed trade so
that each successive trade is worth more than the last. It exposes two methods: `getValue()`
to read the current bonus and `increment()` to advance it.

---

### Method under test: `TradeBonus(int initialValue, int incrementStep)`

**`initialValue` parameter (Count variable ≥ 0, valid range ≥ 1):**

- **TC1: initialValue = 0 (one below lower bound of valid range) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: no TradeBonus created yet
  - **Expected output**: IllegalArgumentException thrown

- **TC2: initialValue = 1 (lower bound of valid range) → TradeBonus constructed** ( :white_check_mark: )
  - **State of the system**: no TradeBonus created yet
  - **Expected output**: TradeBonus constructed; getValue() returns 1

**`incrementStep` parameter (Count variable ≥ 0, valid range ≥ 1):**

- **TC3: incrementStep = 0 (one below lower bound of valid range) → IllegalArgumentException** ( :white_check_mark: )
  - **State of the system**: no TradeBonus created yet (valid initialValue provided)
  - **Expected output**: IllegalArgumentException thrown

- **TC4: incrementStep = 1 (lower bound of valid range) → TradeBonus constructed** ( :white_check_mark: )
  - **State of the system**: no TradeBonus created yet (valid initialValue provided)
  - **Expected output**: TradeBonus constructed; increment() increases getValue() by 1

---

### Method under test: `int getValue()`

Precondition: TradeBonus constructed with valid initialValue and incrementStep. Returns the
current trade bonus value. BVA is on the number of prior `increment()` calls (Count
variable ≥ 0).

**Number of prior `increment()` calls:**

- **TC5: 0 prior increments (initial state, lower bound) → returns initialValue** ( :white_check_mark: )
  - **State of the system**: TradeBonus freshly constructed; increment() not yet called
  - **Expected output**: getValue() returns initialValue

- **TC6: 1 prior increment (one above lower bound) → returns initialValue + incrementStep** ( :white_check_mark: )
  - **State of the system**: TradeBonus constructed; increment() called once
  - **Expected output**: getValue() returns initialValue + incrementStep

---

### Method under test: `void increment()`

Precondition: TradeBonus constructed with valid initialValue and incrementStep. Each call
adds incrementStep to currentValue. BVA is on the number of calls made (Count variable ≥ 1).

**Number of calls:**

- **TC7: first call to increment() → currentValue increases by incrementStep** ( :white_check_mark: )
  - **State of the system**: TradeBonus freshly constructed; increment() called once
  - **Expected output**: getValue() returns initialValue + incrementStep

- **TC8: second call to increment() → currentValue increases by incrementStep again** ( :x: )
  - **State of the system**: TradeBonus constructed; increment() called twice
  - **Expected output**: getValue() returns initialValue + 2 * incrementStep
