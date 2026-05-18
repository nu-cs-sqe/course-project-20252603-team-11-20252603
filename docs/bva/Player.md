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
  - **Expected output**: Player created, name stored, territories empty, cards empty, availableTroops = 0

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

### Method under test: `void placeTroops(ITerritory territory, int amount)`
- **TC24: null territory** ( :white_check_mark: )
  - **State of the system**: availableTroops = 5
  - **Expected output**: IllegalArgumentException thrown
- **TC25: territory not owned by player** ( :white_check_mark: )
  - **State of the system**: player does not own T1, availableTroops = 5
  - **Expected output**: IllegalArgumentException thrown
- **TC26: amount = 0 (lower boundary, invalid)** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 5
  - **Expected output**: IllegalArgumentException thrown
- **TC27: amount is negative** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 5
  - **Expected output**: IllegalArgumentException thrown
- **TC28: amount = availableTroops + 1 (one over, invalid)** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 5
  - **Expected output**: IllegalArgumentException thrown
- **TC29: amount = 1 (minimum valid)** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 5
  - **Expected output**: territory.addTroops(1) called, availableTroops = 4
- **TC30: amount = availableTroops (upper valid boundary)** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 5
  - **Expected output**: territory.addTroops(5) called, availableTroops = 0
- **TC31: availableTroops = 0, amount = 0** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 0
  - **Expected output**: IllegalArgumentException thrown (amount = 0 invalid regardless)
- **TC32: availableTroops = 0, amount = 1** ( :white_check_mark: )
  - **State of the system**: player owns T1, availableTroops = 0
  - **Expected output**: IllegalArgumentException thrown (insufficient troops)  

### Method under test: `int calculateReinforcements()`
- **TC33: 0 territories** ( :white_check_mark: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: 3 (minimum floor)
- **TC34: 1 territory** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 1
  - **Expected output**: 3 (floor(1/3) = 0, minimum applies)
- **TC35: 2 territories** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 2
  - **Expected output**: 3 (floor(2/3) = 0, minimum applies)
- **TC36: 3 territories** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 3
  - **Expected output**: 3 (floor(3/3) = 1, still below minimum of 3)
- **TC37: 9 territories (formula equals minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 9
  - **Expected output**: 3 (floor(9/3) = 3, equals minimum exactly)
- **TC38: 10 territories** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 10
  - **Expected output**: 3 (floor(10/3) = 3)
- **TC39: 11 territories (last value before formula exceeds minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 11
  - **Expected output**: 3 (floor(11/3) = 3)
- **TC40: 12 territories (first value where formula exceeds minimum)** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 12
  - **Expected output**: 4 (floor(12/3) = 4)
- **TC41: 30 territories** ( :white_check_mark: )
  - **State of the system**: Player.territories.size() = 30
  - **Expected output**: 10 (floor(30/3) = 10)   

## calculateTroops()

|             | System under test              | Expected output          | Implemented?       |
|-------------|--------------------------------|--------------------------|--------------------|
| Test Case 1 | 0 territories, 0 continents    | IllegalArgumentException | :white_check_mark: |
| Test Case 2 | 1 territory, 1 (3pt) continent | 6 new troops             | :white_check_mark: |
| Test Case 3 | 11 territory, 0 continents     | 3 new troops             | :white_check_mark: |
| Test Case 4 | 12 territory, 0 continents     | 4 new troops             | :white_check_mark: |
| Test Case 5 | 42 territories, 5 continents   | 35 new troops            | :white_check_mark: |
