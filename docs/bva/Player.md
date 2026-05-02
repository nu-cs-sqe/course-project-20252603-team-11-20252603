# Player - BVA Analysis 

### Method under test: `Player(String name)`
- **TC1: null name** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC 2: empty string** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC-3: valid non-empty name** ( :white_check_mark: )
  - **State of the system**: No player created yet
  - **Expected output**: Player created; name stored; territories empty; cards empty; availableTroops = 0

### Method under test: `void addTerritory(ITerritory territory)`
- **TC 4: null territory** ( :white_check_mark: )
  - **State of the system**: any 
  - **Expected output**: IllegalArgumentException thrown
- **TC 5: add to empty list**: ( :white_check_mark: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: No explicit output, Player.territories.size() = 1 now.
- **TC 6: add to non-empty list**: ( :white_check_mark: )
  - **State of the system**: Player.territories has 1 element
  - **Expected output**: No explicit output, Player.territories.size() == 2 now. 
- **TC 6: add duplicate territory to list with 1 territory**: ( :white_check_mark: )
  - **State of the system**: Player.territories already contains some T1
  - **Expected output**: No explicit output, Player.territories.size() == 1 (no-op, no duplicate added)

### Method under test: `void removeTerritory(ITerritory territory)`
- **TC 7: input is null** ( :x: )
  - **State of system**: Player created, Player.territories can be in any state
  - **Expected output**: IllegalArgumentException thrown
- **TC 8: remove only one element, list becomes empty** ( :x: )
  - **State of the system**: Player.territories has exactly [T1]
  - **Expected output**: No explicit output, Player.territories.size() == 0
- **TC 9: remove one of many territories** ( :x: )
  - **State of the system**: Plauyer.territories has [T1, T2]
  - **Expected output**: No explicit output, Player.territories.size() == 1, containing only T2
- **TC 10: remove territory not in list** ( :x: )
  - **State of the system**: Player.territories has [T1], but T2 is not present
  - **Expected output**: No explicit output, Player.territories.size() == 1 still (no-op, no exception)
- **TC 11: remove from an empty territories list** ( :x: )
  - **State of the system**: Player.territories is empty
  - **Expected output**: No explicit output, Player.territories.size() == 0 (no-op, no exception)