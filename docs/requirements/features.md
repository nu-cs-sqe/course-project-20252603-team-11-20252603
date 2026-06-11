# System Features (Integration Testing Index)

This file indexes the system features used for **integration (thread) testing**, per the
course's recommended approach: integrate modules along a *thread of execution* that
corresponds to a system feature, and write one test class per feature
(`F1Tests.java`, `F2Tests.java`, ...).

We use **TDD / JUnit thread tests** (the rubric accepts TDD *or* BDD equivalently). Each
feature test wires **real collaborators together with no mocks** — this is what
distinguishes an integration test from the per-class unit tests in `src/test/java/domain`,
which isolate a single class with EasyMock.

For an **A**, the rubric requires "integration testing is done on at least 2 main features."
We only need integration coverage on 2+ features, not all of them; the full list is
documented here for shared reference and to make clear to the grader which features are
covered.

| ID  | Feature | Primary collaborators | Integration test |
|-----|---------|-----------------------|------------------|
| F1  | Display the game map (territories, continents, borders) | `GameMap`, `Territory`, `MapView` (GUI) | — (GUI, excluded) |
| F2  | Create players with starting territories and armies | `Game`, `Player`, `Territory`, `GameMap`, `SetupPhase` | — |
| F3  | Place armies according to per-turn rules | `ReinforcementPhase`, `Player`, `Territory` | — |
| F4  | Count turns / turn-based gameplay loop | `GameLoop`, `Turn`, `Game`, `Player` | — |
| F5  | Receive & place reinforcements (territory + continent bonuses) | `ReinforcementPhase`, `Player`, `Territory`, `Game` | — |
| F6  | Attack enemy territories with dice mechanics | `AttackPhase`, `DiceRoller`, `BattleResult`, `Territory`, `Player`, `GameMap`, `Game`, `DeckManager` | ✅ `F6Tests.java` |
| F7  | Fortify: move armies between owned territories | `FortificationPhase`, `GameMap`, `Player`, `Territory`, `ConnectivityGraph` | ✅ `F7Tests.java` |
| F8  | Earn a card for a successful attack (one per turn) | `AttackPhase`, `Game`, `DeckManager`, `RiskCard`, `Player` | partially via F6 |
| F9  | Trade cards for reinforcements | `CardTradePhase`, `CardTradeValidator`, `Player`, `RiskCard` | unit-tested (`CardTradePhaseTests`, `CardTradeValidatorTests`) |
| F10 | Detect win (capture all territories / eliminate all players) | `GameLoop`, `Game`, `Player` | — |

## Status

All features F1–F10 are fully implemented. The A-level "integration testing on ≥ 2 main features" bar is met by **F6** and **F7**. All remaining features have dedicated unit test classes in `src/test/java/domain/`.

- **F6 (Attack)** — covered by `src/test/java/domain/F6Tests.java`. Exercises the full attack
  thread end to end: adjacency validation through `GameMap`, dice resolution through the real
  `DiceRoller`/`BattleResult`, troop attrition and ownership transfer across `Territory`/`Player`,
  and the card award through `Game`/`DeckManager`.
- **F7 (Fortify)** — covered by `src/test/java/domain/F7Tests.java`. Exercises the fortification
  thread with real collaborators: `FortificationPhase` drives a real `ConnectivityGraph` running a
  real ownership-aware BFS over a real `GameMap`, moving troops across real `Territory`/`Player`
  state and enforcing the once-per-turn rule. No mocks.