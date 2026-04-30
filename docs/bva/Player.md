# Player - BVA Analysis 

### Method under test: `Player(String name)`
- **TC1: null name** ( :x: )
    - **State of the system**: No player created yet
    - **Expected output**: IllegalArgumentException thrown
- **TC 2: empty string** ( :x: )
  - **State of the system**: No player created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC-3: valid non-empty name** ( :x: )
  - **State of the system**: No player created yet
  - **Expected output**: Player created; name stored; territories empty; cards empty; availableTroops = 0