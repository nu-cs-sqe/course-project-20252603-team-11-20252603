# Game - BVA Analysis

### Method under test: `Game(List<Player> players, GameMap map)`

`players` is a collection with a constrained size in the interval [2, 6].

**`players` parameter (Pointer + Collection with size interval [2, 6]):**
- **TC1: null players list** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC2: 0 players (empty list, below lower bound)** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC3: 1 player (one below lower bound)** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC4: 2 players (lower bound, valid)** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: Game constructed; players list stored, map stored
- **TC5: 6 players (upper bound, valid)** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: Game constructed; players list stored, map stored
- **TC6: 7 players (one above upper bound)** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC7: players list contains a null element** ( :white_check_mark: )
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown

**`map` parameter (Pointer):**
- **TC8: null map**
  - **State of the system**: No game created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC9: valid non-null map**
  - **State of the system**: No game created yet (valid players list provided)
  - **Expected output**: Game constructed; map stored

---

### Method under test: `void assignTerritories()`

Precondition: Game constructed with 2–6 players and a valid map.

The territories collection from the map is a Count variable (size ≥ 0). The pair (territories.size(), players.size()) drives distribution behavior.

- **TC10: 0 territories in map**
  - **State of the system**: Game constructed; map contains no territories
  - **Expected output**: All players own 0 territories; no exception thrown
- **TC11: 1 territory in map, 2 players**
  - **State of the system**: Game constructed with 2 players; map contains exactly 1 territory
  - **Expected output**: Exactly 1 player owns the territory; that territory has troopCount = 1; the other player owns 0 territories
- **TC12: territories.size() > 1, evenly divisible by players.size()**
  - **State of the system**: Game constructed; e.g. 4 territories, 2 players
  - **Expected output**: All territories assigned; each player owns territories.size() / players.size() territories; each territory has troopCount = 1
- **TC13: territories.size() > 1, NOT evenly divisible by players.size()**
  - **State of the system**: Game constructed; e.g. 3 territories, 2 players
  - **Expected output**: All territories assigned (no territory is unowned); territory counts across players differ by at most 1; each territory has troopCount = 1

---

### Method under test: `void distributeStartingTroops()`

Precondition: `assignTerritories()` has already been called.

The number of starting armies per player is determined by `players.size()`, which is a Case variable over {2, 3, 4, 5, 6}. Every case must be tested.

- **TC14: 2 players**
  - **State of the system**: Game constructed with 2 players; territories assigned
  - **Expected output**: Each player's availableTroops = 40 − (number of territories they own)
- **TC15: 3 players**
  - **State of the system**: Game constructed with 3 players; territories assigned
  - **Expected output**: Each player's availableTroops = 35 − (number of territories they own)
- **TC16: 4 players**
  - **State of the system**: Game constructed with 4 players; territories assigned
  - **Expected output**: Each player's availableTroops = 30 − (number of territories they own)
- **TC17: 5 players**
  - **State of the system**: Game constructed with 5 players; territories assigned
  - **Expected output**: Each player's availableTroops = 25 − (number of territories they own)
- **TC18: 6 players**
  - **State of the system**: Game constructed with 6 players; territories assigned
  - **Expected output**: Each player's availableTroops = 20 − (number of territories they own)

---

### Method under test: `void shuffleDeck()`

Precondition: Game constructed. BVA is on `deck` (Count variable, size ≥ 0).

- **TC19: deck has 0 cards (empty)**
  - **State of the system**: deck is empty
  - **Expected output**: deck remains empty; no exception thrown
- **TC20: deck has 1 card**
  - **State of the system**: deck contains exactly 1 card
  - **Expected output**: deck still contains the same 1 card; no exception thrown
- **TC21: deck has 2 cards (minimum meaningful shuffle)**
  - **State of the system**: deck contains exactly 2 cards
  - **Expected output**: deck still contains both original cards (order may differ)
- **TC22: deck has 44 cards (maximum: 42 territory cards + 2 wildcards)**
  - **State of the system**: deck contains all 44 standard Risk cards
  - **Expected output**: deck still contains all 44 original cards after shuffle (order may differ)

---

### Method under test: `void chooseFirstPlayer()`

Precondition: Game constructed with 2–6 players.

Sets `currentPlayerIndex` to a random value. BVA is on the resulting index as an interval [0, players.size() − 1].

- **TC23: 2 players, result = 0 (lower bound of valid range)**
  - **State of the system**: Game constructed with 2 players
  - **Expected output**: currentPlayerIndex = 0 (a valid outcome; lower boundary of [0, 1])
- **TC24: 2 players, result = 1 (upper bound of valid range)**
  - **State of the system**: Game constructed with 2 players
  - **Expected output**: currentPlayerIndex = 1 (a valid outcome; upper boundary of [0, 1])
- **TC25: 6 players, result = 0 (lower bound of valid range)**
  - **State of the system**: Game constructed with 6 players
  - **Expected output**: currentPlayerIndex = 0 (a valid outcome; lower boundary of [0, 5])
- **TC26: 6 players, result = 5 (upper bound of valid range)**
  - **State of the system**: Game constructed with 6 players
  - **Expected output**: currentPlayerIndex = 5 (a valid outcome; upper boundary of [0, 5])
