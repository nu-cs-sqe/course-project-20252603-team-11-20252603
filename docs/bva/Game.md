# Game - BVA Analysis

### Method under test: `Game(List<Player> players, GameMap map)`

`players` is a collection with a constrained size in the interval [2, 6].

**`players` parameter (Collection with size interval [2, 6]):**

- **TC1: 0 players (empty list, below lower bound)** ( :white_check_mark: )
    - **State of the system**: No game created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC2: 1 player (one below lower bound)** ( :white_check_mark: )
    - **State of the system**: No game created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC3: 2 players (lower bound, valid)** ( :white_check_mark: )
    - **State of the system**: No game created yet
    - **Expected output**: Game constructed; players list stored, map stored
- **TC4: 6 players (upper bound, valid)** ( :white_check_mark: )
    - **State of the system**: No game created yet
    - **Expected output**: Game constructed; players list stored, map stored
- **TC5: 7 players (one above upper bound)** ( :white_check_mark: )
    - **State of the system**: No game created yet
    - **Expected output**: IllegalArgumentException thrown

**`map` parameter:**

- **TC6: valid map** ( :white_check_mark: )
    - **State of the system**: No game created yet (valid players list provided)
    - **Expected output**: Game constructed; map stored

---

### Method under test: `void assignTerritories()`

Precondition: Game constructed with 2–6 players and a valid map.

The territories collection from the map is a Count variable (size ≥ 0). The pair (territories.size(), players.size())
drives distribution behavior.

- **TC7: 0 territories in map** ( :white_check_mark: )
    - **State of the system**: Game constructed; map contains no territories
    - **Expected output**: All players own 0 territories; no exception thrown
- **TC8: 1 territory in map, 2 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; map contains exactly 1 territory
    - **Expected output**: Exactly 1 player owns the territory; that territory has troopCount = 1; the other player owns
      0 territories
- **TC9: territories.size() > 1, evenly divisible by players.size()** ( :white_check_mark: )
    - **State of the system**: Game constructed; e.g. 4 territories, 2 players
    - **Expected output**: All territories assigned; each player owns territories.size() / players.size() territories;
      each territory has troopCount = 1
- **TC10: territories.size() > 1, NOT evenly divisible by players.size()** ( :white_check_mark: )
    - **State of the system**: Game constructed; e.g. 3 territories, 2 players
    - **Expected output**: All territories assigned (no territory is unowned); territory counts across players differ by
      at most 1; each territory has troopCount = 1
- **TC10a: 2 territories, random produces a swap → assignment reflects shuffled order** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; map has [t0, t1]; random.nextInt(2) returns 0 (causes
      swap → [t1, t0])
    - **Expected output**: player[0] receives t1; player[1] receives t0
- **TC10b: 2 territories, random produces no swap → assignment reflects original order** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; map has [t0, t1]; random.nextInt(2) returns 1 (no
      swap → [t0, t1])
    - **Expected output**: player[0] receives t0; player[1] receives t1

---

### Method under test: `void distributeStartingTroops()`

Precondition: `assignTerritories()` has already been called.

The number of starting armies per player is determined by `players.size()`, which is a Case variable over {2, 3, 4, 5,
6}. Every case must be tested.

- **TC11: 2 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; territories assigned
    - **Expected output**: Each player's availableTroops = 40 − (number of territories they own)
- **TC12: 3 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 3 players; territories assigned
    - **Expected output**: Each player's availableTroops = 35 − (number of territories they own)
- **TC13: 4 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 4 players; territories assigned
    - **Expected output**: Each player's availableTroops = 30 − (number of territories they own)
- **TC14: 5 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 5 players; territories assigned
    - **Expected output**: Each player's availableTroops = 25 − (number of territories they own)
- **TC15: 6 players** ( :white_check_mark: )
    - **State of the system**: Game constructed with 6 players; territories assigned
    - **Expected output**: Each player's availableTroops = 20 − (number of territories they own)

---

### Method under test: `void shuffleDeck()`

Precondition: Game constructed. BVA is on `deck` (Count variable, size ≥ 0).

- **TC16: deck has 0 cards (empty)** ( :white_check_mark: )
    - **State of the system**: deck is empty
    - **Expected output**: deck remains empty; no exception thrown
- **TC17: deck has 1 card** ( :white_check_mark: )
    - **State of the system**: deck contains exactly 1 card
    - **Expected output**: deck still contains the same 1 card; no exception thrown
- **TC18: deck has 2 cards (minimum meaningful shuffle)** ( :white_check_mark: )
    - **State of the system**: deck contains exactly 2 cards
    - **Expected output**: deck still contains both original cards (order may differ)
- **TC19: deck has 44 cards (maximum: 42 territory cards + 2 wildcards)** ( :white_check_mark: )
    - **State of the system**: deck contains all 44 standard Risk cards
    - **Expected output**: deck still contains all 44 original cards after shuffle (order may differ)

---

### Method under test: `void chooseFirstPlayer()`

Precondition: Game constructed with 2–6 players.

Sets `currentPlayerIndex` to a random value. BVA is on the resulting index as an interval [0, players.size() − 1].

- **TC20: 2 players, result = 0 (lower bound of valid range)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players
    - **Expected output**: currentPlayerIndex = 0 (a valid outcome; lower boundary of [0, 1])
- **TC21: 2 players, result = 1 (upper bound of valid range)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players
    - **Expected output**: currentPlayerIndex = 1 (a valid outcome; upper boundary of [0, 1])
- **TC22: 6 players, result = 0 (lower bound of valid range)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 6 players
    - **Expected output**: currentPlayerIndex = 0 (a valid outcome; lower boundary of [0, 5])
- **TC23: 6 players, result = 5 (upper bound of valid range)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 6 players
    - **Expected output**: currentPlayerIndex = 5 (a valid outcome; upper boundary of [0, 5])

---

### Method under test: `void startGame()`

Precondition: Game constructed with 2–6 players, a valid map, and a deck.

`startGame()` is a sequencing method with no input parameters. It orchestrates four setup steps in order:
`shuffleDeck` → `assignTerritories` → `distributeStartingTroops` → `chooseFirstPlayer`. BVA focuses on two concerns: (1)
all four steps execute and produce correct observable state, and (2) the resulting `currentPlayerIndex` is valid (an
interval [0, players.size() − 1] — boundary values inherited from `chooseFirstPlayer`).

**All four setup steps execute in correct sequence:**

- **TC24: startGame executes all four setup steps in the correct order** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players, 2 territories, 1 deck card; random controlled
    - **Expected output**: deck shuffled; both territories assigned (each with 1 troop); each player's availableTroops
      set correctly; currentPlayerIndex set to the value returned by random

**Resulting `currentPlayerIndex` after startGame (Interval [0, players.size() − 1]):**

- **TC25: 2 players, result = 0 (lower bound of resulting index)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; random returns 0 for chooseFirstPlayer call
    - **Expected output**: currentPlayerIndex = 0
- **TC26: 2 players, result = 1 (upper bound of resulting index)** ( :white_check_mark: )
    - **State of the system**: Game constructed with 2 players; random returns 1 for chooseFirstPlayer call
    - **Expected output**: currentPlayerIndex = 1

---

### Method under test: `void advanceToNextPlayer()`

Precondition: Game constructed with 2–6 players.

`advanceToNextPlayer` is gated by `currentPlayerIndex`: throw if the game has not started (`currentPlayerIndex == -1`),
otherwise increment with modulo wraparound. BVA covers the unstarted boundary plus the two valid-range boundaries (
no-wrap vs wraparound) at minimum and maximum player counts.

- **TC27: game not started (currentPlayerIndex == -1)** ( :white_check_mark: )
    - **State of the system**: Game just constructed; `chooseFirstPlayer()` not yet called
    - **Expected output**: IllegalStateException thrown; currentPlayerIndex stays at -1
- **TC28: 2 players, currentPlayerIndex == 0 (lower boundary, no wraparound)** ( :white_check_mark: )
    - **State of the system**: Game with 2 players; currentPlayerIndex == 0
    - **Expected output**: currentPlayerIndex == 1
- **TC29: 2 players, currentPlayerIndex == 1 (upper boundary, wraps to 0)** ( :white_check_mark: )
    - **State of the system**: Game with 2 players; currentPlayerIndex == 1
    - **Expected output**: currentPlayerIndex == 0
- **TC30: 6 players, currentPlayerIndex == 5 (upper boundary, wraps to 0)** ( :white_check_mark: )
    - **State of the system**: Game with 6 players; currentPlayerIndex == 5
    - **Expected output**: currentPlayerIndex == 0
