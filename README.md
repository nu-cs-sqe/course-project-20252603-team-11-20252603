![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-11-20252603/actions/workflows/main.yml/badge.svg)

[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23633488)

# Risk

A digital implementation of the classic Risk board game set in the world of *Avatar: The Last Airbender*. The map replaces the standard world map with 42 territories across 5 factions: Moon Tribe, Ba Sing Se Kingdom, Fire Nation, Omashu Kingdom, and Ocean Tribe.

## Contributors

- Jefferson Wu
- Kris Yun
- Nandan Dhanesh
- Brock Brown

## Architecture

The project is organized into three packages:

| Package | Responsibility |
|---|---|
| `domain` | Core game logic — `Game`, `GameLoop`, `Player`, `Territory`, `GameMap`, `AtlaMapData`, and all phase classes (`SetupPhase`, `ReinforcementPhase`, `AttackPhase`, `FortificationPhase`, `CardTradePhase`). No dependency on `ui` or `i18n`. |
| `i18n` | Internationalization facade (`Messages`) backed by Java `ResourceBundle`. Supports runtime locale switching (ships English and Spanish). |
| `ui` | JavaFX front end — `Main`, `MapView`, `GameController`, `SidebarPanel`, `TerritoryNode`. Depends on `domain` and `i18n`; never imported by them. |

## Test-Driven Development

All `domain` classes were developed following strict **TDD**: BVA test cases were written and documented in `docs/bva/` before any production code for those classes. Unit tests use **JUnit 5** with **EasyMock** for collaborator isolation. Integration tests (`F6Tests`, `F7Tests`) wire real collaborators together with no mocks, covering the Attack and Fortify features end-to-end.

**Note on internationalization (`i18n`):** The `Messages` class and its tests in `i18n/MessagesTests` were not developed under TDD. Internationalization was added through the UI layer, and we realized retrospectively that the course directions did not say to exclude i18n from testing. Tests were therefore added after the fact to achieve full non-GUI coverage. The BVA analysis for `Messages` is in `docs/bva/Messages.md`.

## Dependencies

| Dependency | Version |
|---|---|
| JDK | 11 |
| JavaFX | 17.0.6 |
| JUnit 5 | 5.10.0 |
| EasyMock | 5.4.0 |
| Gradle | 8.10 |
| Checkstyle | 10.21.4 |
| JaCoCo | 0.8.12 |
| PIT (mutation testing) | 1.15.0 |
| SpotBugs | 4.8.6 |

## Building and Running

```bash
# Run all tests
./gradlew test

# Launch the application
./gradlew run

# Generate JaCoCo coverage report  →  build/reports/jacoco/
./gradlew jacocoTestReport

# Run mutation testing  →  build/reports/pitest/
./gradlew pitest

# Run Checkstyle and SpotBugs static analysis
./gradlew check
```

## Documentation

| Document | Description |
|---|---|
| `docs/requirements/game-rules.md` | Human-readable game rules |
| `docs/requirements/features.md` | Integration test index (F1–F10) |
| `docs/design/README.md` | System design: use cases, class design, and architecture decisions |
| `docs/bva/` | Boundary value analysis for every domain class |
| `docs/weekly-reports/` | Weekly progress reports and instructor feedback |

## Acknowledgements

- Game rules adapted from [Dice Breaker — How to Play Risk](https://www.dicebreaker.com/games/risk/how-to/how-to-play-risk-board-game)
- Map and territory names themed around *Avatar: The Last Airbender*
