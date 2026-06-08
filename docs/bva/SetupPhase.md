# SetupPhase - BVA Analysis

Handles all pre-game initialization logic: territory assignment, troop distribution, and first-player
selection. Extracted from `Game` to keep boot orchestration out of the core aggregate.

**Preconditions for most tests:** A `Game` mock is injected into `SetupPhase`. All collaborators are mocked.

### Method under test: `SetupPhase(Game game)`

**`game` parameter:**

- **TC1: null game** ( :white_check_mark: )
    - **State of the system**: No SetupPhase created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC2: valid game, 2 players (lower bound of player count)** ( :white_check_mark: )
    - **State of the system**: No SetupPhase created yet; game.getPlayerCount() returns 2
    - **Expected output**: SetupPhase constructed without exception; getPlayerCount() == 2
- **TC3: valid game, 6 players (upper bound of player count)** ( :white_check_mark: )
    - **State of the system**: No SetupPhase created yet; game.getPlayerCount() returns 6
    - **Expected output**: SetupPhase constructed without exception; getPlayerCount() == 6

---

### Method under test: `void assignTerritories()`

Precondition: SetupPhase constructed with a valid game.

No input parameters. Delegates entirely to `game.assignTerritories()`.

- **TC4: delegates to game.assignTerritories()** ( :white_check_mark: )
    - **State of the system**: SetupPhase constructed; game.assignTerritories() not yet called
    - **Expected output**: game.assignTerritories() called exactly once

---

### Method under test: `void distributeStartingTroops()`

Precondition: SetupPhase constructed with a valid game.

No input parameters. Delegates entirely to `game.distributeStartingTroops()`.

- **TC5: delegates to game.distributeStartingTroops()** ( :x: )
    - **State of the system**: SetupPhase constructed; game.distributeStartingTroops() not yet called
    - **Expected output**: game.distributeStartingTroops() called exactly once

---

### Method under test: `Player chooseFirstPlayer()`

Precondition: SetupPhase constructed with a valid game.

Calls `game.chooseFirstPlayer()` then returns `game.getCurrentActivePlayer()`. The returned
player is an Interval [player[0], player[n−1]] driven by the game's internal random.

- **TC6: 2 players, lower-bound player returned (player at index 0)** ( :x: )
    - **State of the system**: SetupPhase constructed with 2-player game; game.getCurrentActivePlayer() returns player[0]
    - **Expected output**: game.chooseFirstPlayer() called exactly once; returned player == player[0]
- **TC7: 2 players, upper-bound player returned (player at index 1)** ( :x: )
    - **State of the system**: SetupPhase constructed with 2-player game; game.getCurrentActivePlayer() returns player[1]
    - **Expected output**: game.chooseFirstPlayer() called exactly once; returned player == player[1]
- **TC8: 6 players, lower-bound player returned (player at index 0)** ( :x: )
    - **State of the system**: SetupPhase constructed with 6-player game; game.getCurrentActivePlayer() returns player[0]
    - **Expected output**: game.chooseFirstPlayer() called exactly once; returned player == player[0]
- **TC9: 6 players, upper-bound player returned (player at index 5)** ( :x: )
    - **State of the system**: SetupPhase constructed with 6-player game; game.getCurrentActivePlayer() returns player[5]
    - **Expected output**: game.chooseFirstPlayer() called exactly once; returned player == player[5]

---

### Method under test: `void run()`

Precondition: SetupPhase constructed with a valid game.

`run()` is a sequencing method with no input parameters. It orchestrates three setup steps in
order — `assignTerritories` → `distributeStartingTroops` → `chooseFirstPlayer` — then sets
`game.gameState = IN_PROGRESS`. BVA focuses on: (1) all steps execute, and (2) the resulting
`gameState` is always `IN_PROGRESS`.

- **TC10: 2-player game — all steps execute; gameState set to IN_PROGRESS** ( :x: )
    - **State of the system**: SetupPhase constructed with 2-player game; game.gameState == SETUP
    - **Expected output**: game.assignTerritories() called; game.distributeStartingTroops() called;
      game.chooseFirstPlayer() called; game.getCurrentActivePlayer() called; game.setGameState(IN_PROGRESS) called
- **TC11: 6-player game — all steps execute; gameState set to IN_PROGRESS** ( :x: )
    - **State of the system**: SetupPhase constructed with 6-player game; game.gameState == SETUP
    - **Expected output**: game.assignTerritories() called; game.distributeStartingTroops() called;
      game.chooseFirstPlayer() called; game.getCurrentActivePlayer() called; game.setGameState(IN_PROGRESS) called
