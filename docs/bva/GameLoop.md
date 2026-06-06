# GameLoop - BVA Analysis

Central coordinator for the multi-turn game cycle (Issue #64, Use Cases 4, 6, 7).

**Threshold constants (from acceptance criteria / game rules):**
- Pre-reinforcement card trade trigger: player holds **≥ 5** cards at turn start
- Post-elimination card trade trigger: attacker holds **≥ 6** cards after inheriting eliminated player's cards

**Preconditions for most tests:** Game is in `IN_PROGRESS`, `startGame()` has been called, and `checkWinCondition()` returns false unless stated otherwise. All collaborators (`Game`, `Player`, `Turn`, `CardTradePhase`) are mocked in tests; protected factory methods (`createTurn`, `createCardTradePhase`) are overridden to inject mocks (same pattern as `TurnTests`).

---

### Method under test: `GameLoop(Game game)`

**`game` parameter:**

- **TC1: null game** ( :white_check_mark: )
    - **State of the system**: No GameLoop created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC2: valid non-null game** ( :white_check_mark: )
    - **State of the system**: No GameLoop created yet
    - **Expected output**: GameLoop created; getGame() returns injected game

---

### Method under test: `boolean checkWinCondition()`

**`activePlayerCount` (Count variable — non-eliminated players with at least one territory):**

The win boundary is at exactly **1** active player.

- **TC3: 2 active players (one above win threshold)** ( :white_check_mark: )
    - **State of the system**: 2 players not eliminated; gameState == IN_PROGRESS; winner unset
    - **Expected output**: returns false; gameState stays IN_PROGRESS; winner remains unset
- **TC4: 1 active player (win threshold, lower boundary of "game over")** ( :white_check_mark: )
    - **State of the system**: 1 player not eliminated, all others eliminated; gameState == IN_PROGRESS
    - **Expected output**: returns true; game.gameState set to GAME_OVER; game.winner set to the sole remaining player
- **TC5: 3 active players (nominal in-progress state)** ( :white_check_mark: )
    - **State of the system**: 3 players not eliminated
    - **Expected output**: returns false; gameState stays IN_PROGRESS; winner remains unset

---

### Method under test: `void runNextTurn()`

**A. Current player identification — skip eliminated players**

`currentPlayerIndex` may point at an eliminated player; GameLoop must advance to the next active player before creating a Turn.

- **TC6: current index points to active player (no skip needed)** ( :white_check_mark: )
    - **State of the system**: currentPlayerIndex == 0; player[0].isEliminated() == false
    - **Expected output**: Turn created for player[0]; no index adjustment before turn creation
- **TC7: current index points to eliminated player, next player active (skip 1)** ( :x: )
    - **State of the system**: currentPlayerIndex == 0; player[0].isEliminated() == true; player[1].isEliminated() == false
    - **Expected output**: Turn created for player[1] (not player[0])
- **TC8: current index points to eliminated player, two consecutive eliminated, third active (skip 2)** ( :x: )
    - **State of the system**: 4 players; currentPlayerIndex == 0; player[1] and player[2] eliminated; player[3] active
    - **Expected output**: Turn created for player[3]

**B. Pre-turn CardTradePhase — current player's card count (Count, trigger interval starts at 5)**

- **TC9: 4 cards (one below trigger threshold)** ( :x: )
    - **State of the system**: current player holds 4 cards
    - **Expected output**: CardTradePhase NOT created/run before reinforcement
- **TC10: 5 cards (lower boundary of trigger threshold)** ( :x: )
    - **State of the system**: current player holds 5 cards
    - **Expected output**: CardTradePhase created and run before reinforcement begins
- **TC11: 6 cards (above lower boundary, still triggers)** ( :x: )
    - **State of the system**: current player holds 6 cards
    - **Expected output**: CardTradePhase created and run before reinforcement begins

**C. Reinforcement count calculated externally**

- **TC12: calculateReinforcements() result passed into ReinforcementPhase, not computed inside ReinforcementPhase** ( :x: )
    - **State of the system**: current player holds < 5 cards; player.calculateReinforcements() returns 7
    - **Expected output**: player.calculateReinforcements() called exactly once; ReinforcementPhase constructed with troopsToPlace == 7; player.setAvailableTroops(7) called (reinforcement count sourced from Player, not ReinforcementPhase internals)

**D. Fresh Turn and delegation**

- **TC13: a new Turn is created each call** ( :x: )
    - **State of the system**: runNextTurn() called once on a GameLoop with active player
    - **Expected output**: createTurn() called exactly once; returned Turn receives (currentPlayer, game, random); Turn lifecycle delegated (startTurn → runReinforcementPhase → runAttackPhase → runFortificationPhase → endTurn)
- **TC14: second call creates a separate Turn instance** ( :x: )
    - **State of the system**: runNextTurn() called twice; win condition false after first turn
    - **Expected output**: createTurn() called twice; two distinct Turn mock instances used

**E. Post-turn elimination handling**

- **TC15: no elimination during turn** ( :x: )
    - **State of the system**: Turn completes; no defending player eliminated
    - **Expected output**: no card transfer; no post-elimination CardTradePhase; checkWinCondition() called once after turn (returns false)
- **TC16: defending player eliminated — cards transferred to attacker** ( :x: )
    - **State of the system**: Turn completes; defender eliminated; defender had 3 cards; attacker had 2 cards
    - **Expected output**: all 3 defender cards added to attacker's hand (attacker now holds 5); defender marked eliminated
- **TC17: post-elimination card count == 5 (one below immediate-trade threshold)** ( :x: )
    - **State of the system**: after card transfer, attacker holds exactly 5 cards
    - **Expected output**: no immediate CardTradePhase triggered after elimination
- **TC18: post-elimination card count == 6 (lower boundary of immediate-trade threshold)** ( :x: )
    - **State of the system**: after card transfer, attacker holds exactly 6 cards
    - **Expected output**: CardTradePhase created and run immediately for the attacker
- **TC19: post-elimination card count == 7 (above immediate-trade threshold)** ( :x: )
    - **State of the system**: after card transfer, attacker holds 7 cards
    - **Expected output**: CardTradePhase created and run immediately for the attacker

**F. Win re-check after elimination**

- **TC20: elimination leaves 2+ active players — game continues** ( :x: )
    - **State of the system**: one player eliminated this turn; 2 active players remain
    - **Expected output**: checkWinCondition() returns false; gameState stays IN_PROGRESS
- **TC21: elimination leaves exactly 1 active player — game ends** ( :x: )
    - **State of the system**: one player eliminated this turn; only 1 active player remains
    - **Expected output**: checkWinCondition() returns true; gameState == GAME_OVER; winner set to remaining player

---

### Method under test: `void start()`

No input parameters. BVA focuses on loop termination driven by `checkWinCondition()`.

- **TC22: win condition already met before loop — exits without running a turn** ( :x: )
    - **State of the system**: checkWinCondition() returns true on first evaluation
    - **Expected output**: runNextTurn() never called; gameState == GAME_OVER
- **TC23: win condition met after exactly 1 iteration** ( :x: )
    - **State of the system**: checkWinCondition() returns false, then true after one runNextTurn()
    - **Expected output**: runNextTurn() called exactly once; loop exits; gameState == GAME_OVER
- **TC24: win condition met after multiple iterations** ( :x: )
    - **State of the system**: checkWinCondition() returns false twice, then true on third check
    - **Expected output**: runNextTurn() called exactly twice; loop exits on third checkWinCondition(); gameState == GAME_OVER
- **TC25: loop continues while multiple active players remain** ( :x: )
    - **State of the system**: 3 active players; checkWinCondition() returns false for first 5 iterations, then true
    - **Expected output**: runNextTurn() called exactly 5 times before loop exits
