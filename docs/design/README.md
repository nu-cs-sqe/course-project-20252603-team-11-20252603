# System Design

> **Note:** This design was created collaboratively in a [Google Document](https://docs.google.com/document/d/1YCv8tL5Uc0MT8etI7hFDUxLlvCKmzwen8G1awSIu3ZA/edit) before any production code was written. The document was used to agree on use cases, class responsibilities, and method signatures up front. This README is a transcription of that plan, updated to reflect the final implementation, produced at project close-out.

---

## Overview

The system implements a digital version of Risk themed around *Avatar: The Last Airbender*. The board has 42 territories organized into 5 factions (continents). All game logic lives in the `domain` package, which has no dependency on the UI or i18n layers.

---

## Territory Map

### Moon Tribe — 7 territories *(Color: Gray)*

Northern Tundra, Frost Hills, Moon Tribe, Bin-Er, Yue Bay, Wulong, Western Air Temple

### Ba Sing Se Kingdom — 10 territories *(Color: Green)*

Zigan, Northern Air Temple, Northern Mountains, Taihua, Ba Sing Se City, Ba Sing Se Province, Continental Corridor, Taku, Green Farmlands, Charmeleon Province

### Fire Nation — 7 territories *(Color: Red)*

Sun Isles, Burning Gates, Caldera City, Black Cliffs, Keonso, Fire Archipelago, Whale Tail Isle

### Omashu Kingdom — 10 territories *(Color: Tan/Beige)*

Hei Bei, Great Divide, Serpent Pass, Full Moon Bay, Omashu, Western Si Wong Desert, Eastern Si Wong Desert, Heart Farmlands, Chin, Gao Ling

### Ocean Tribe — 8 territories *(Color: Light Blue)*

Eastern Air Temple, Seafoam Isles, Hakoda Island, Shimsom Isle, Southern Air Temple, Ocean Tribe, Southern Tundra, Icy Plains

---

## Class Design

### `Territory`

Represents one territory on the board.

**Fields:** `name: String`, `owner: Player`, `troopCount: int`

**Key methods:** `addTroops(int)`, `removeTroops(int)`, `getTroopCount()`, `getOwner()`, `setOwner(Player)`, `getName()`, `conquer(Player newOwner, int troopsMovedIn)`

---

### `GameMap`

Owns the territory graph. Adjacency is stored as `Map<Territory, Set<Territory>>`.

**Key methods:** `addTerritory(Territory)`, `addConnection(Territory, Territory)`, `getTerritories()`, `getNeighbors(Territory)`, `areAdjacent(Territory, Territory)`, `findPath(Territory, Territory, Player)`

---

### `AtlaMapData`

Owns the 5 faction groupings and their territory membership. `buildMap()` constructs the full 42-territory `GameMap` with all adjacencies. `continentCount()` returns 5.

---

### `Player`

Represents one player in the game.

**Fields:** `name: String`, `territories: List<Territory>`, `cards: List<RiskCard>`, `availableTroops: int`, `isEliminated: boolean`

**Key methods:** `addTerritory(Territory)`, `removeTerritory(Territory)`, `addCard(RiskCard)`, `inheritCardsFrom(Player)`, `calculateReinforcements()`, `getAvailableTroops()`, `setAvailableTroops(int)`, `getTerritoryCount()`, `getCardCount()`, `isEliminated()`, `setEliminated(boolean)`

---

### `RiskCard`

**Fields:** `riskCardType: RiskCardType`, `territory: Territory`

**Key methods:** `getType()`, `getTerritory()`

---

### `RiskCardType` (enum)

`INFANTRY`, `CAVALRY`, `ARTILLERY`, `WILDCARD`

---

### `GameState` (enum)

`SETUP`, `IN_PROGRESS`, `FINISHED`

---

### `Game`

Aggregate root. Holds all shared game state.

**Fields:** `players: List<Player>`, `map: GameMap`, `deckManager: DeckManager`, `random: Random`, `gameState: GameState`, `winner: Optional<Player>`, `currentPlayerIndex: int`

**Key methods:** `assignTerritories()`, `distributeStartingTroops()`, `chooseFirstPlayer()`, `getCurrentActivePlayer()`, `setGameState(GameState)`, `setWinner(Player)`, `getPlayers()`, `getMap()`, `getRandom()`

---

### `DeckManager`

Manages the 42-card Risk deck: building, shuffling, drawing, and the discard pile.

**Key methods:** `buildDeck(List<Territory>)`, `shuffle()`, `draw()`, `returnCards(List<RiskCard>)`, `getDrawPileSize()`, `getDiscardPileSize()`

---

### `SetupPhase`

Orchestrates game initialization.

**Key methods:** `assignTerritories()`, `distributeStartingTroops()`, `chooseFirstPlayer()`, `run()`

---

### `ReinforcementPhase`

Constructed with `troopsToPlace` pre-calculated by `GameLoop`. Handles interactive troop placement.

**Key methods:** `validatePlacement(int troops, Territory)`, `placeTroops(int troops, Territory)`, `getRemaining()`, `isComplete()`

---

### `AttackPhase`

Manages the attack sequence. The attack is split into discrete steps so the UI can prompt the player between each.

**Key methods:** `declareAttack(Territory s, Territory t, int n)`, `resolveBattle(Territory s, Territory t, int n)`, `moveInTroops(Territory s, Territory t, int n)`, `canAttack(Territory s, Territory t)`, `awardCardIfEarned()`, `endPhase()`, `isEnded()`, `getConqueredCount()`

---

### `DiceRoller`

Rolls and compares dice for a single battle.

**Key methods:** `rollAttacker(int n)` → `List<Integer>`, `rollDefender(int n)` → `List<Integer>`, `sortDescending(List<Integer>)`, `compare(List<Integer> attackerDice, List<Integer> defenderDice)` → `BattleResult`

---

### `BattleResult`

Immutable value produced by `DiceRoller.compare()`. Computes losses from sorted dice lists in its constructor.

**Key methods:** `getAttackerLosses()`, `getDefenderLosses()`, `isConquered()`

---

### `FortificationPhase`

Validates connectivity through player-owned territories and enforces the once-per-turn rule.

**Key methods:** `moveTroops(Territory s, Territory d, int n)`, `validateMove(Territory s, Territory d, int n)`, `skipPhase()`, `isMoved()`, `isComplete()`, `isConnected(Territory s, Territory d)`, `findPath(Territory s, Territory d)`

---

### `ConnectivityGraph`

Ownership-aware BFS over `GameMap`.

**Key methods:** `isConnected(Territory s, Territory d, Player owner)`, `findPath(Territory s, Territory d, Player owner)`, `getReachable(Territory src, Player owner)`

---

### `Turn`

Represents one player's full turn. Runs all three phases in order.

**Key methods:** `startTurn()`, `runReinforcementPhase()`, `runAttackPhase()`, `runFortificationPhase()`, `endTurn()`, `getEliminatedDefender()` → `Optional<Player>`, `hasConqueredThisTurn()`

---

### `GameLoop`

Central coordinator for the main game loop. Manages the `TradeBonus` counter.

**Key methods:** `start()`, `runNextTurn()`, `checkWinCondition()`

---

### `CardTradePhase`

Manages the pre-reinforcement card trading phase. `mandatory` is `true` when the player holds ≥ 5 cards.

**Constants:** `PRE_TURN_THRESHOLD = 5`, `POST_ELIMINATION_THRESHOLD = 6`

**Key methods:** `validateSet(List<RiskCard>)`, `run()`, `isComplete()`

---

### `CardTradeValidator`

Pure logic class with no state.

**Key methods:** `isValidSet(List<RiskCard>)`, `mustTrade(Player)`, `isMandatory(Player)`

---

### `TradeBonus`

Tracks the globally incrementing trade bonus. Starts at 4; increments by 2 each trade.

**Key methods:** `getValue()`, `increment()`

---

## Use Cases

### Use Case 1: Start New Game

**Actor:** Player

**Preconditions:** The game application is launched.

**Main Flow:**
1. Player clicks "Start Game"
2. System prompts for the number of players
3. Player enters number of players
4. System validates (2–6 players)
5. System shows the map
6. System randomly distributes all territories among players
7. System assigns 1 army to each territory
8. System calculates remaining armies per player based on player count
9. System allows players to place remaining armies
10. System randomizes turn order
11. System transitions to the first player's turn

**Alternate Flows:**
- *3.a Invalid number of players:* System displays error; user re-enters.

**Postconditions:** All territories are assigned with 1 army each; all players have placed their initial armies; turn order is established.

---

### Use Case 2: Reinforcement Phase

**Actor:** Current Player

**Preconditions:** It is the current player's turn; game is not in setup or end-game state; current player owns at least one territory.

**Main Flow:**
1. System calculates reinforcement armies: `max(3, territories_owned ÷ 3)` plus any continent bonuses
2. System displays available reinforcements
3. Player selects an owned territory and enters armies to place
4. System validates and adds armies; decrements available troops
5. Steps 3–4 repeat until available troops reach zero
6. System transitions to attack phase

**Alternate Flows:**
- *3.a Player selects unowned territory:* Error; return to step 3.
- *3.b Player enters more armies than available:* Error; return to step 3.

---

### Use Case 3: Attack Phase

**Actor:** Current Player (attacker), Other Player (defender)

**Preconditions:** Reinforcement phase complete; attacker owns a territory with ≥ 2 armies adjacent to an enemy territory.

**Main Flow:**
1. Player selects an owned source territory (≥ 2 armies) and an adjacent enemy target territory
2. Player chooses attacking dice count (1–3, capped by source armies − 1)
3. System rolls defender dice (1–2, capped by target armies) and compares pairs in descending order; ties go to defender
4. Each losing comparison costs that side 1 army
5. If target reaches 0 armies: attacker captures territory; attacker must move in ≥ attacking dice armies (and ≤ source armies − 1)
6. Player may continue attacking from step 1 or end the attack phase
7. If player conquered any territory this turn, system awards 1 Risk card via `awardCardIfEarned()`

**Alternate Flows:**
- *Source territory < 2 armies:* Error.
- *Target not adjacent or owned by attacker:* Error.
- *Armies to move in out of valid range:* Error.

---

### Use Case 4: Fortification Phase

**Actor:** Current Player

**Preconditions:** Attack phase complete; current player owns ≥ 2 connected territories with at least one having > 1 army.

**Main Flow:**
1. Player selects an owned source territory (≥ 2 armies)
2. Player selects a destination territory owned by the current player and **connected through a chain of owned territories** (not required to be directly adjacent)
3. Player enters armies to move (1 to source armies − 1)
4. System validates connectivity via `ConnectivityGraph` and army count, then moves armies
5. System ends the turn and advances to the next player

**Alternate Flows:**
- *Player skips fortification:* `skipPhase()` called; proceed to step 5.
- *Destination not reachable through owned territories:* Error; return to step 2.
- *Movement would leave source with 0 armies:* Error; return to step 3.

**Postconditions:** At most one army movement occurred this turn; every territory the current player owns has ≥ 1 army; next player is now active.

---

### Use Case 5: Game Loop

**Actor:** Game System

**Main Flow:**
1. System identifies current player (by `currentPlayerIndex`)
2. If current player is eliminated, advance index and repeat step 1
3. Check win condition: if only one active player remains, declare winner and transition to `FINISHED`
4. If player holds ≥ 5 cards, run `CardTradePhase` (mandatory = true)
5. Calculate reinforcements via `player.calculateReinforcements()`; create and run `Turn`
6. After turn: check if a defender was eliminated; if so, transfer cards to attacker via `inheritCardsFrom(Player)`
7. If attacker now holds ≥ 6 cards, run `CardTradePhase` (mandatory = true)
8. Re-check win condition; advance `currentPlayerIndex`; return to step 1

---

### Use Case 6: Card Trading Phase

**Actor:** Current Player

**Preconditions:** Player holds ≥ 3 cards.

**Main Flow:**
1. If player holds ≥ 5 cards, trading is mandatory
2. Player selects a valid 3-card set (three of the same type, one of each, or any set containing a wildcard)
3. System validates the set via `CardTradeValidator.isValidSet()`; cards are removed from hand
4. `CardTradePhase.run()` awards `TradeBonus.getValue()` troops and increments the bonus
5. If player still holds ≥ 5 cards, return to step 2

---

### Use Case 7: Win Condition Check

**Actor:** Game System

**Main Flow:**
1. Count active players (not eliminated, owns ≥ 1 territory)
2. If exactly one remains, declare that player the winner
3. Transition to `FINISHED` state via `game.setGameState(GameState.FINISHED)` and `game.setWinner(Player)`

---

## Design Decisions

### Changes from Initial Google Doc Design

| Decision | Original Plan | Final Implementation |
|---|---|---|
| Troop calculation | `ReinforcementPhase` computed its own troop count | `GameLoop` calls `player.calculateReinforcements()` and passes the result into the `ReinforcementPhase` constructor |
| Attack API | Single `attack()` method | Split into `declareAttack()`, `resolveBattle()`, and `moveInTroops()` so the UI can prompt between steps |
| Adjacency storage | `Map<Territory, List<Territory>>` | `Map<Territory, Set<Territory>>` to prevent duplicate edges |

### Gap Resolution

The initial design identified three classes that were referenced but not yet designed. All three were implemented:

| Gap | Resolution |
|---|---|
| Territory connectivity for `FortificationPhase` | `ConnectivityGraph` — ownership-aware BFS over `GameMap` |
| Deck management | `DeckManager` — builds the 42-card deck, handles shuffle, draw, and discard |
| Game initialization algorithms | `SetupPhase` — random territory assignment and troop distribution |

### Army Representation

Only infantry pieces are used. Card set trade-in values follow the course use-cases model as a system-enforced calculation starting at 4 troops and increasing by 2 with each trade globally.
