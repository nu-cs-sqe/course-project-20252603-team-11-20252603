# Turn - BVA Analysis

### Method under test: `Turn(Player currentPlayer, Game game, Random random)`
- **TC1: null player** ( :white_check_mark: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC2: null game** ( :white_check_mark: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC3: null random** ( :white_check_mark: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC4: all valid args** ( :white_check_mark: )
    - **State of the system**: No turn created yet
    - **Expected output**: Turn created, getPhase() == null, hasConqueredThisTurn() == false,
      getCurrentPlayer() returns injected player, getGame() returns injected game,
      getReinforcementPhase() == null, getAttackPhase() == null, getFortificationPhase() == null

### Method under test: `void startTurn()`
- **TC5: called twice** ( :white_check_mark: )
    - **State of the system**: startTurn() already called once, phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC6: valid first call** ( :white_check_mark: )
    - **State of the system**: phase == null, player.calculateReinforcements() returns 5
    - **Expected output**: player.setAvailableTroops(5) called, phase == REINFORCEMENT,
      getReinforcementPhase() returns a non-null ReinforcementPhase instance

### Method under test: `void runReinforcementPhase()`
- **TC7: called before startTurn (phase == null)** ( :white_check_mark: )
    - **State of the system**: Turn just constructed
    - **Expected output**: IllegalStateException thrown
- **TC8: called when phase == ATTACK** ( :white_check_mark: )
    - **State of the system**: Turn already advanced past REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC9: called when phase == FORTIFICATION** ( :white_check_mark: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalStateException thrown
- **TC10: called when phase == ENDED** ( :white_check_mark: )
    - **State of the system**: phase == ENDED
    - **Expected output**: IllegalStateException thrown
- **TC11: reinforcementPhase.isComplete() == false** ( :white_check_mark: )
    - **State of the system**: phase == REINFORCEMENT, troops not all placed
    - **Expected output**: IllegalStateException thrown, phase stays REINFORCEMENT,
      getAttackPhase() still null
- **TC12: reinforcementPhase.isComplete() == true (valid transition)** ( :white_check_mark: )
    - **State of the system**: phase == REINFORCEMENT, all troops placed
    - **Expected output**: phase == ATTACK, getAttackPhase() returns a non-null AttackPhase
      instance constructed with (currentPlayer, game, random)

### Method under test: `void runAttackPhase()`
- **TC13: phase == REINFORCEMENT** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC14: phase == FORTIFICATION** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalStateException thrown
- **TC15: phase == ENDED** ( :x: )
    - **State of the system**: phase == ENDED
    - **Expected output**: IllegalStateException thrown
- **TC16: attackPhase.isEnded() == false (pending conquest or not signaled end)** ( :x: )
    - **State of the system**: phase == ATTACK, attackPhase.isEnded() returns false
    - **Expected output**: IllegalStateException thrown, phase stays ATTACK,
      getFortificationPhase() still null
- **TC17: valid transition, no territories conquered** ( :x: )
    - **State of the system**: phase == ATTACK, attackPhase.isEnded() == true,
      attackPhase.getConqueredCount() == 0
    - **Expected output**: phase == FORTIFICATION, hasConqueredThisTurn() == false,
      getFortificationPhase() returns a non-null FortificationPhase instance
- **TC18: valid transition, 1 territory conquered (lower valid boundary)** ( :x: )
    - **State of the system**: phase == ATTACK, attackPhase.isEnded() == true,
      attackPhase.getConqueredCount() == 1
    - **Expected output**: phase == FORTIFICATION, hasConqueredThisTurn() == true
- **TC19: valid transition, many territories conquered** ( :x: )
    - **State of the system**: phase == ATTACK, attackPhase.isEnded() == true,
      attackPhase.getConqueredCount() == 7
    - **Expected output**: phase == FORTIFICATION, hasConqueredThisTurn() == true

### Method under test: `void runFortificationPhase()`
- **TC20: phase == REINFORCEMENT** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC21: phase == ATTACK** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalStateException thrown
- **TC22: phase == ENDED (called twice)** ( :x: )
    - **State of the system**: phase == ENDED
    - **Expected output**: IllegalStateException thrown
- **TC23: fortificationPhase.isComplete() == false** ( :x: )
    - **State of the system**: phase == FORTIFICATION, player hasn't fortified or skipped
    - **Expected output**: IllegalStateException thrown, phase stays FORTIFICATION
- **TC24: valid transition (fortified or skipped)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, fortificationPhase.isComplete() == true
    - **Expected output**: phase == ENDED

### Method under test: `void endTurn()`
- **TC25: phase == null (turn never started)** ( :x: )
    - **State of the system**: Turn just constructed
    - **Expected output**: IllegalStateException thrown
- **TC26: phase == REINFORCEMENT** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown, game.advanceToNextPlayer() NOT called
- **TC27: phase == ATTACK** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalStateException thrown
- **TC28: phase == FORTIFICATION** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalStateException thrown
- **TC29: valid call, phase == ENDED** ( :x: )
    - **State of the system**: phase == ENDED
    - **Expected output**: game.advanceToNextPlayer() called exactly once
