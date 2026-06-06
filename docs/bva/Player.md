# Player - BVA Analysis 

### Method under test: `Player(String name)`
- **TC1: null name** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC2: empty string** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC3: valid non-empty name** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: Player created, name stored, territories empty, cards empty, availableTroops = 0, isEliminated = false

### Method under test: `void addTerritory(ITerritory territory)`
- **TC4: null territory** ( :white_check_mark: )
  - **State of the system**: any 
  - **Expected output**: IllegalArgumentException thrown
- **TC5: add to empty list**: ( :white_check_mark: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: No explicit output, Player.territories.size() = 1 now.
- **TC6: add to non-empty list**: ( :white_check_mark: )
  - **State of the system**: Player.territories has 1 element
  - **Expected output**: No explicit output, Player.territories.size() == 2 now. 
- **TC7: add duplicate territory to list with 1 territory**: ( :white_check_mark: )
  - **State of the system**: Player.territories already contains some T1
  - **Expected output**: No explicit output, Player.territories.size() == 1 (no-op, no duplicate added)

### Method under test: `void removeTerritory(ITerritory territory)`
- **TC8: input is null** ( :white_check_mark: )
  - **State of system**: Player created, Player.territories can be in any state
  - **Expected output**: IllegalArgumentException thrown
- **TC9: remove only one element, list becomes empty** ( :white_check_mark: )
  - **State of the system**: Player.territories has exactly [T1]
  - **Expected output**: No explicit output, Player.territories.size() == 0
- **TC10: remove one of many territories** ( :white_check_mark: )
  - **State of the system**: Player.territories has [T1, T2]
  - **Expected output**: No explicit output, Player.territories.size() == 1, containing only T2
- **TC11: remove territory not in list** ( :white_check_mark: )
  - **State of the system**: Player.territories has [T1], but T2 is not present
  - **Expected output**: No explicit output, Player.territories.size() == 1 still (no-op, no exception)
- **TC12: remove from an empty territories list** ( :white_check_mark: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: No explicit output, Player.territories.size() == 0 (no-op, no exception)

### Method under test: `void setAvailableTroops(int amount)`
- **TC13: negative amount** ( :white_check_mark: )
  - **State of the system**: Player created
  - **Expected output**: IllegalArgumentException thrown
- **TC14: amount = 0, still valid** ( :white_check_mark: )
  - **State of the system**: Player created
  - **Expected output**: Player.availableTroops == 0
- **TC15: positive amount** ( :white_check_mark: )
  - **State of the system**: Player created
  - **Expected output**: Player.availableTroops == 5

### Method under test: `void addCard(IRiskCard card)`
- **TC16: null card** ( :white_check_mark: )
  - **State of the system**: any
  - **Expected output**: IllegalArgumentException thrown
- **TC17: add to empty hand** ( :white_check_mark: )
  - **State of the system**: Player.cards is empty
  - **Expected output**: Player.cards.size() = 1, list contains added card
- **TC18: add second card** ( :white_check_mark: )
  - **State of the system**: Player.cards has 1 card
  - **Expected output**: Player.cards.size() = 2, list contains both cards
- **TC19: add INFANTRY card** ( :white_check_mark: )
  - **State of the system**: Player.cards is empty
  - **Expected output**: Player.cards.get(0).getType() = INFANTRY
- **TC20: add CAVALRY card** ( :white_check_mark: )
  - **State of the system**: Player.cards is empty
  - **Expected output**: Player.cards.get(0).getType() = CAVALRY
- **TC21: add ARTILLERY card** ( :white_check_mark: )
  - **State of the system**: Player.cards is empty
  - **Expected output**: Player.cards.get(0).getType() = ARTILLERY
- **TC22: add WILDCARD card** ( :white_check_mark: )
  - **State of the system**: Player.cards is empty
  - **Expected output**: Player.cards.get(0).getType() = WILDCARD
- **TC23: duplicate card type allowed** ( :white_check_mark: )
  - **State of the system**: Player.cards already has 1 INFANTRY card
  - **Expected output**: Player.cards.size() = 2 (cards are not de-duplicated)

### Method under test: `boolean isEliminated()`
- **TC24: new player defaults to not eliminated** ( :white_check_mark: )
  - **State of the system**: Player just constructed
  - **Expected output**: isEliminated() == false

### Method under test: `int calculateReinforcements()`
- **TC25: 0 territories, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: 3 (minimum floor)
- **TC26: 1 territory, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 1
  - **Expected output**: 3 (floor(1/3) = 0, minimum applies)
- **TC27: 2 territories, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 2
  - **Expected output**: 3 (floor(2/3) = 0, minimum applies)
- **TC28: 3 territories, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 3
  - **Expected output**: 3 (floor(3/3) = 1, still below minimum of 3)
- **TC29: 9 territories, no continents (formula equals minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 9
  - **Expected output**: 3 (floor(9/3) = 3, equals minimum exactly)
- **TC30: 10 territories, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 10
  - **Expected output**: 3 (floor(10/3) = 3)
- **TC31: 11 territories, no continents (last value before formula exceeds minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 11
  - **Expected output**: 3 (floor(11/3) = 3)
- **TC32: 12 territories, no continents (first value where formula exceeds minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 12
  - **Expected output**: 4 (floor(12/3) = 4)
- **TC33: 30 territories, no continents** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 30
  - **Expected output**: 10 (floor(30/3) = 10)
- **TC34: 1 territory, player owns all territories of a 1-territory continent with +3 bonus** ( :white_check_mark: )
  - **State of the system**: Player.territories = [T1], continent C1 contains [T1] with bonus 3
  - **Expected output**: 6 (3 base + 3 continent bonus)
- **TC35: 1 territory, player owns only some territories of a continent (no bonus)** ( :white_check_mark: )
  - **State of the system**: Player.territories = [T1], continent C1 contains [T1, T2] with bonus 3; player does not own T2
  - **Expected output**: 3 (floor(1/3)=0 → minimum 3; continent not fully owned, no bonus)
- **TC36: 42 territories, player owns all territories of 5 continents (total bonus 21)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 42 spanning 5 fully-owned continents with bonuses summing to 21
  - **Expected output**: 35 (floor(42/3) = 14 + 21 continent bonus)
