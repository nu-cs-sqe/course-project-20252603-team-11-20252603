# FortificationPhase - BVA Analysis

### Method under test: `FortificationPhase(Player player, GameMap map)`

- **TC1: null player** ( :x: )
  - **State of the system**: No FortificationPhase created yet
  - **Expected output**: IllegalArgumentException thrown

- **TC2: null map** ( :x: )
  - **State of the system**: No FortificationPhase created yet; valid Player provided
  - **Expected output**: IllegalArgumentException thrown

- **TC3: valid player and map** ( :x: )
  - **State of the system**: No FortificationPhase created yet; valid Player and GameMap provided
  - **Expected output**: FortificationPhase constructed; moved = false

### Method under test: `void moveTroops(Territory s, Territory d, int n)`

- **TC4: null source territory** ( :x: )
  - **State of the system**: Phase created, moved = false
  - **Expected output**: IllegalArgumentException thrown

- **TC5: null destination territory** ( :x: )
  - **State of the system**: Phase created, moved = false; valid territory provided as source
  - **Expected output**: IllegalArgumentException thrown

- **TC6: source == destination (same territory object)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns T1
  - **Expected output**: IllegalArgumentException thrown

- **TC7: source not owned by player** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns d but not s
  - **Expected output**: IllegalArgumentException thrown

- **TC8: destination not owned by player** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s but not d
  - **Expected output**: IllegalArgumentException thrown

- **TC9: n = 0 (below minimum valid count)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC10: n = −1 (negative)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC11: n = s.troopCount (one over maximum valid, source would be left with 0 troops)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC12: no path between source and destination** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s and d; no connection between s and d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC13: path exists in map but intermediate territory is enemy-owned** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s and d; map has s−mid−d but mid is owned by a different player
  - **Expected output**: IllegalArgumentException thrown

- **TC14: n = 1 (minimum valid), source has 2 troops, s and d directly connected** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d (troopCount = 1); direct connection s−d in map
  - **Expected output**: s.troopCount = 1; d.troopCount = 2; moved = true

- **TC15: n = s.troopCount − 1 (maximum valid), source has 3 troops, s and d directly connected** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 3) and d (troopCount = 1); direct connection s−d in map
  - **Expected output**: s.troopCount = 1; d.troopCount = 3; moved = true

- **TC16: second moveTroops call after a successful first call** ( :x: )
  - **State of the system**: Phase created; moveTroops already called successfully; moved = true
  - **Expected output**: IllegalStateException thrown

- **TC17: moveTroops called after skipPhase** ( :x: )
  - **State of the system**: Phase created; skipPhase already called; moved = true
  - **Expected output**: IllegalStateException thrown

### Method under test: `boolean isConnected(Territory s, Territory d)`

- **TC18: null source** ( :x: )
  - **State of the system**: Phase created
  - **Expected output**: IllegalArgumentException thrown

- **TC19: null destination** ( :x: )
  - **State of the system**: Phase created; valid territory provided as source
  - **Expected output**: IllegalArgumentException thrown

- **TC20: source == destination (same territory object)** ( :x: )
  - **State of the system**: Phase created; player owns T1
  - **Expected output**: IllegalArgumentException thrown

- **TC21: source and destination are direct neighbors, both owned by player (1 hop)** ( :x: )
  - **State of the system**: Phase created; player owns s and d; map has direct connection s−d
  - **Expected output**: true

- **TC22: source and destination connected via one intermediate territory, all player-owned (2 hops)** ( :x: )
  - **State of the system**: Phase created; player owns s, mid, and d; map has connections s−mid and mid−d
  - **Expected output**: true

- **TC23: source and destination connected via two intermediate territories, all player-owned (3 hops)** ( :x: )
  - **State of the system**: Phase created; player owns s, mid1, mid2, and d; map has connections s−mid1, mid1−mid2, mid2−d
  - **Expected output**: true

- **TC24: no path between source and destination in map** ( :x: )
  - **State of the system**: Phase created; player owns s and d; no connection between s and d in map
  - **Expected output**: false

- **TC25: path exists in map but intermediate territory is enemy-owned** ( :x: )
  - **State of the system**: Phase created; player owns s and d; map has s−mid−d but mid is owned by a different player
  - **Expected output**: false

### Method under test: `List<Territory> findPath(Territory s, Territory d)`

- **TC26: null source** ( :x: )
  - **State of the system**: Phase created
  - **Expected output**: IllegalArgumentException thrown

- **TC27: null destination** ( :x: )
  - **State of the system**: Phase created; valid territory provided as source
  - **Expected output**: IllegalArgumentException thrown

- **TC28: source == destination (same territory object)** ( :x: )
  - **State of the system**: Phase created; player owns T1
  - **Expected output**: IllegalArgumentException thrown

- **TC29: source and destination are direct neighbors, both player-owned (1 hop)** ( :x: )
  - **State of the system**: Phase created; player owns s and d; map has direct connection s−d
  - **Expected output**: list of size 2 containing [s, d]

- **TC30: source and destination connected via one intermediate, all player-owned (2 hops)** ( :x: )
  - **State of the system**: Phase created; player owns s, mid, and d; map has connections s−mid and mid−d
  - **Expected output**: list of size 3: [s, mid, d]

- **TC31: source and destination connected via two intermediates, all player-owned (3 hops)** ( :x: )
  - **State of the system**: Phase created; player owns s, mid1, mid2, and d; map has connections s−mid1, mid1−mid2, mid2−d
  - **Expected output**: list of size 4: [s, mid1, mid2, d]

- **TC32: no path between source and destination in map** ( :x: )
  - **State of the system**: Phase created; player owns s and d; no connection between s and d in map
  - **Expected output**: empty list

- **TC33: path exists in map but intermediate territory is enemy-owned** ( :x: )
  - **State of the system**: Phase created; player owns s and d; map has s−mid−d but mid is owned by a different player
  - **Expected output**: empty list

### Method under test: `void validateMove(Territory s, Territory d, int n)`

- **TC34: null source** ( :x: )
  - **State of the system**: Phase created, moved = false
  - **Expected output**: IllegalArgumentException thrown

- **TC35: null destination** ( :x: )
  - **State of the system**: Phase created, moved = false; valid territory provided as source
  - **Expected output**: IllegalArgumentException thrown

- **TC36: source == destination (same territory object)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns T1
  - **Expected output**: IllegalArgumentException thrown

- **TC37: source not owned by player** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns d but not s
  - **Expected output**: IllegalArgumentException thrown

- **TC38: destination not owned by player** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s but not d
  - **Expected output**: IllegalArgumentException thrown

- **TC39: neither source nor destination owned by player** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns neither s nor d
  - **Expected output**: IllegalArgumentException thrown (source ownership fails first)

- **TC40: n = 0 (below minimum valid count)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC41: n = −1 (negative)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC42: n = s.troopCount (one over maximum valid, source has 2 troops)** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC43: moved already true (phase already completed)** ( :x: )
  - **State of the system**: Phase created; moved = true (prior moveTroops or skipPhase call succeeded)
  - **Expected output**: IllegalStateException thrown

- **TC44: no path between source and destination** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s and d; no connection between s and d in map
  - **Expected output**: IllegalArgumentException thrown

- **TC45: path exists in map but intermediate territory is enemy-owned** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s and d; map has s−mid−d but mid is owned by a different player
  - **Expected output**: IllegalArgumentException thrown

- **TC46: n = 1 (minimum valid), source has 2 troops, s and d directly connected** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 2) and d; direct connection s−d in map
  - **Expected output**: no exception thrown

- **TC47: n = s.troopCount − 1 (maximum valid), source has 3 troops, s and d directly connected** ( :x: )
  - **State of the system**: Phase created, moved = false; player owns s (troopCount = 3) and d; direct connection s−d in map
  - **Expected output**: no exception thrown

### Method under test: `void skipPhase()`

- **TC48: fresh phase, skip without moving** ( :x: )
  - **State of the system**: Phase created, moved = false
  - **Expected output**: moved = true; no territory troop counts changed

- **TC49: skipPhase called after moveTroops already succeeded** ( :x: )
  - **State of the system**: Phase created; moveTroops already called successfully; moved = true
  - **Expected output**: IllegalStateException thrown

- **TC50: skipPhase called twice** ( :x: )
  - **State of the system**: Phase created; skipPhase already called once; moved = true
  - **Expected output**: IllegalStateException thrown
