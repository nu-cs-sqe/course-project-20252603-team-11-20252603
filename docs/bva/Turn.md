# Turn - BVA Analysis

### Method under test: `Turn(Player currentPlayer, Game game, Random random)`

- **TC1: null player** ( :x: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC2: null game** ( :x: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC3: null random** ( :x: )
    - **State of the system**: No turn created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC4: all valid args** ( :x: )
    - **State of the system**: No turn created yet
    - **Expected output**: Turn created, getPhase() == null, hasConqueredThisTurn() == false,
      getCurrentPlayer() returns injected player, getGame() returns injected game

### Method under test: `void startTurn()`

- **TC5: called before startTurn (called twice)** ( :x: )
    - **State of the system**: startTurn() already called once
    - **Expected output**: IllegalStateException thrown
- **TC6: first valid call, player has 9 territories** ( :x: )
    - **State of the system**: phase == null, player.calculateReinforcements() returns 3
    - **Expected output**: player.setAvailableTroops(3) called, phase == REINFORCEMENT
- **TC7: first valid call, player has 12 territories** ( :x: )
    - **State of the system**: phase == null, player.calculateReinforcements() returns 4
    - **Expected output**: player.setAvailableTroops(4) called, phase == REINFORCEMENT

### Method under test: `void placeReinforcement(Territory territory, int amount)`

- **TC8: phase == null (startTurn not called)** ( :x: )
    - **State of the system**: Turn just constructed, phase == null
    - **Expected output**: IllegalStateException thrown
- **TC9: phase == ATTACK** ( :x: )
    - **State of the system**: phase advanced past REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC10: phase == FORTIFICATION** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalStateException thrown
- **TC11: phase == ENDED** ( :x: )
    - **State of the system**: phase == ENDED
    - **Expected output**: IllegalStateException thrown
- **TC12: null territory** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalArgumentException thrown
- **TC13: amount = 1 (min valid), availableTroops = 5** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player owns T1, availableTroops == 5
    - **Expected output**: player.placeTroops(T1, 1) called, phase stays REINFORCEMENT
- **TC14: amount = availableTroops, partial fills budget** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player owns T1, availableTroops == 5
    - **Expected output**: player.placeTroops(T1, 5) called, after call availableTroops == 0,
      phase auto-transitions to ATTACK
- **TC15: amount < availableTroops, more troops remain** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player owns T1, availableTroops == 5
    - **Expected output**: player.placeTroops(T1, 3) called, availableTroops == 2,
      phase stays REINFORCEMENT
- **TC16: amount = 0 (invalid lower boundary)** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player owns T1, availableTroops == 5
    - **Expected output**: IllegalArgumentException thrown (delegated by player.placeTroops)
- **TC17: amount = availableTroops + 1 (one over)** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player owns T1, availableTroops == 5
    - **Expected output**: IllegalArgumentException thrown
- **TC18: territory not owned by current player** ( :x: )
    - **State of the system**: phase == REINFORCEMENT, player does not own T1, availableTroops == 5
    - **Expected output**: IllegalArgumentException thrown

### Method under test: `void declareAttack(Territory source, Territory target, int attackerDice)`

- **TC19: phase != ATTACK** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC20: pending conquest unresolved** ( :x: )
    - **State of the system**: phase == ATTACK, previous attack conquered T2, moveAfterConquest not yet called
    - **Expected output**: IllegalStateException thrown
- **TC21: null source** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalArgumentException thrown
- **TC22: null target** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalArgumentException thrown
- **TC23: source not owned by current player** ( :x: )
    - **State of the system**: phase == ATTACK, source owned by another player
    - **Expected output**: IllegalArgumentException thrown
- **TC24: target owned by current player** ( :x: )
    - **State of the system**: phase == ATTACK, target.owner == currentPlayer
    - **Expected output**: IllegalArgumentException thrown
- **TC25: target not adjacent to source** ( :x: )
    - **State of the system**: phase == ATTACK, gameMap.areAdjacent(source, target) == false
    - **Expected output**: IllegalArgumentException thrown
- **TC26: attackerDice = 0 (lower invalid)** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalArgumentException thrown
- **TC27: attackerDice = -1 (lower invalid)** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalArgumentException thrown
- **TC28: attackerDice = 4 (upper invalid)** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalArgumentException thrown
- **TC29: source.troopCount = attackerDice (one too few)** ( :x: )
    - **State of the system**: phase == ATTACK, source has 3 troops, attackerDice = 3
    - **Expected output**: IllegalArgumentException thrown (need >= dice + 1)
- **TC30: attackerDice = 1, defender = 1, defender wins (mocked Random)** ( :x: )
    - **State of the system**: phase == ATTACK, source has 2 troops, target has 1, Random returns
      attacker roll 2, defender roll 6
    - **Expected output**: source.removeTroops(1) called, target unchanged, phase stays ATTACK,
      no pending conquest
- **TC31: attackerDice = 1, defender = 1, tie goes to defender** ( :x: )
    - **State of the system**: phase == ATTACK, source has 2 troops, target has 1, Random returns
      attacker roll 4, defender roll 4
    - **Expected output**: source.removeTroops(1) called, target unchanged
- **TC32: attackerDice = 1, attacker wins, target reaches 0 → pending conquest** ( :x: )
    - **State of the system**: phase == ATTACK, source has 2 troops, target has 1, Random returns
      attacker roll 6, defender roll 1
    - **Expected output**: target.removeTroops(1) called, pending conquest set
      (source, target, 1), phase stays ATTACK
- **TC33: attackerDice = 3, defender = 2, mixed outcome** ( :x: )
    - **State of the system**: phase == ATTACK, source has 4 troops, target has 2, Random returns
      attacker [6,5,2], defender [5,3]
    - **Expected output**: highest pair 6 vs 5 → attacker wins → target -1,
      second pair 5 vs 3 → attacker wins → target -1, target reaches 0, pending conquest set
- **TC34: minimum valid source.troopCount = attackerDice + 1** ( :x: )
    - **State of the system**: phase == ATTACK, source has 4 troops, attackerDice = 3
    - **Expected output**: roll proceeds normally (boundary accepted)

### Method under test: `void moveAfterConquest(int amount)`

- **TC35: no pending conquest** ( :x: )
    - **State of the system**: phase == ATTACK, no pending conquest
    - **Expected output**: IllegalStateException thrown
- **TC36: amount < attackerDice (below minimum)** ( :x: )
    - **State of the system**: pending conquest with attackerDice = 2, source has 5 troops
    - **Expected output**: IllegalArgumentException thrown
- **TC37: amount = attackerDice (minimum valid)** ( :x: )
    - **State of the system**: pending conquest with attackerDice = 2, source has 5 troops
    - **Expected output**: target.conquer(currentPlayer, 2) called, source.removeTroops(2) called,
      conqueredThisTurn == true, pending state cleared
- **TC38: amount = source.troopCount - 1 (maximum valid)** ( :x: )
    - **State of the system**: pending conquest with attackerDice = 2, source has 5 troops
    - **Expected output**: target.conquer(currentPlayer, 4) called, source.removeTroops(4) called
- **TC39: amount = source.troopCount (would leave 0 in source)** ( :x: )
    - **State of the system**: pending conquest with attackerDice = 2, source has 5 troops
    - **Expected output**: IllegalArgumentException thrown
- **TC40: amount between min and max, valid** ( :x: )
    - **State of the system**: pending conquest with attackerDice = 2, source has 5 troops
    - **Expected output**: target.conquer(currentPlayer, 3) called, source.removeTroops(3) called

### Method under test: `void endAttackPhase()`

- **TC41: phase != ATTACK** ( :x: )
    - **State of the system**: phase == REINFORCEMENT
    - **Expected output**: IllegalStateException thrown
- **TC42: pending conquest unresolved** ( :x: )
    - **State of the system**: phase == ATTACK, pending conquest exists
    - **Expected output**: IllegalStateException thrown
- **TC43: no conquest this turn** ( :x: )
    - **State of the system**: phase == ATTACK, conqueredThisTurn == false
    - **Expected output**: game.drawCard() NOT called, phase transitions to FORTIFICATION
- **TC44: at least one conquest this turn** ( :x: )
    - **State of the system**: phase == ATTACK, conqueredThisTurn == true
    - **Expected output**: game.drawCard() called exactly once, player.addCard(returnedCard) called,
      phase transitions to FORTIFICATION

### Method under test: `void fortify(Territory source, Territory dest, int amount)`

- **TC45: phase != FORTIFICATION** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalStateException thrown
- **TC46: fortify already used this turn** ( :x: )
    - **State of the system**: phase == FORTIFICATION, fortified == true
    - **Expected output**: IllegalStateException thrown
- **TC47: null source** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalArgumentException thrown
- **TC48: null dest** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalArgumentException thrown
- **TC49: source == dest** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: IllegalArgumentException thrown
- **TC50: source not owned by current player** ( :x: )
    - **State of the system**: phase == FORTIFICATION, source.owner != currentPlayer
    - **Expected output**: IllegalArgumentException thrown
- **TC51: dest not owned by current player** ( :x: )
    - **State of the system**: phase == FORTIFICATION, dest.owner != currentPlayer
    - **Expected output**: IllegalArgumentException thrown
- **TC52: source and dest not connected through owned territories** ( :x: )
    - **State of the system**: gameMap.isConnectedThroughOwnedTerritories(...) returns false
    - **Expected output**: IllegalArgumentException thrown
- **TC53: amount = 0 (lower invalid)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, connected, source has 5 troops
    - **Expected output**: IllegalArgumentException thrown
- **TC54: amount = -1 (lower invalid)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, connected, source has 5 troops
    - **Expected output**: IllegalArgumentException thrown
- **TC55: amount = source.troopCount (would leave 0)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, connected, source has 5 troops
    - **Expected output**: IllegalArgumentException thrown
- **TC56: amount = 1 (min valid)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, connected, source has 5 troops
    - **Expected output**: source.removeTroops(1) called, dest.addTroops(1) called,
      fortified == true, phase transitions to ENDED
- **TC57: amount = source.troopCount - 1 (max valid)** ( :x: )
    - **State of the system**: phase == FORTIFICATION, connected, source has 5 troops
    - **Expected output**: source.removeTroops(4) called, dest.addTroops(4) called,
      fortified == true, phase transitions to ENDED

### Method under test: `void skipFortify()`

- **TC58: phase != FORTIFICATION** ( :x: )
    - **State of the system**: phase == ATTACK
    - **Expected output**: IllegalStateException thrown
- **TC59: valid skip** ( :x: )
    - **State of the system**: phase == FORTIFICATION
    - **Expected output**: no troop movement, phase transitions to ENDED

### Method under test: `void endTurn()`

- **TC60: phase != ENDED** ( :x: )
    - **State of the system**: phase == FORTIFICATION (fortify/skip not yet called)
    - **Expected output**: IllegalStateException thrown
- **TC61: valid end-of-turn** ( :x: )
    - **State of the system**: phase == ENDED
    - **Expected output**: game.advanceToNextPlayer() called exactly once

### Method under test: `boolean hasConqueredThisTurn()`

- **TC62: never conquered** ( :x: )
    - **State of the system**: Turn just constructed
    - **Expected output**: returns false
- **TC63: after successful conquest** ( :x: )
    - **State of the system**: moveAfterConquest() has completed once
    - **Expected output**: returns true
