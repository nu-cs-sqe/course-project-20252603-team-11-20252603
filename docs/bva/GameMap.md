# GameMap - BVA Analysis

### Method under test: `GameMap()`

No parameters. Verifies the constructed map's initial observable state.

- **TC1: empty map after construction** ( :white_check_mark: )
    - **State of the system**: No map created yet
    - **Expected output**: GameMap constructed; `getTerritories()` returns an empty list

### Method under test: `void addTerritory(ITerritory territory)`

`territory` parameter is a Pointer; the territories collection is a Count variable that drives duplicate behavior.

- **TC2: null territory** ( :white_check_mark: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC3: add to empty map** ( :white_check_mark: )
    - **State of the system**: GameMap.territories is empty
    - **Expected output**: No explicit output; `getTerritories().size() == 1`; the added territory is contained
- **TC4: add second distinct territory** ( :white_check_mark: )
    - **State of the system**: GameMap.territories has exactly 1 element
    - **Expected output**: No explicit output; `getTerritories().size() == 2`; both territories contained
- **TC5: add duplicate territory** ( :white_check_mark: )
    - **State of the system**: GameMap.territories already contains some T1
    - **Expected output**: No explicit output; `getTerritories().size() == 1` (no-op, no duplicate added)

### Method under test: `void addConnection(ITerritory a, ITerritory b)`

`a` and `b` are Pointers. Self-connection is invalid. Both must already be in the map. Edges are undirected; duplicate edges are no-ops.

- **TC6: a is null** ( :white_check_mark: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC7: b is null** ( :white_check_mark: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC8: a == b (self-connection)** ( :white_check_mark: )
    - **State of the system**: GameMap.territories contains T1
    - **Expected output**: IllegalArgumentException thrown
- **TC9: a not in map** ( :white_check_mark: )
    - **State of the system**: GameMap.territories contains T2 but not T1
    - **Expected output**: IllegalArgumentException thrown
- **TC10: b not in map** ( :white_check_mark: )
    - **State of the system**: GameMap.territories contains T1 but not T2
    - **Expected output**: IllegalArgumentException thrown
- **TC11: valid pair, both in map** ( :white_check_mark: )
    - **State of the system**: GameMap.territories contains T1 and T2
    - **Expected output**: No explicit output; no exception thrown (bidirectionality verified by later `areAdjacent` tests)
- **TC12: duplicate connection (same pair (a,b))** ( :x: )
    - **State of the system**: GameMap contains T1, T2; addConnection(T1, T2) already called
    - **Expected output**: No explicit output; calling addConnection(T1, T2) again is a no-op; T1's neighbor list still has size 1
- **TC13: duplicate connection reversed (b,a after a,b)** ( :x: )
    - **State of the system**: GameMap contains T1, T2; addConnection(T1, T2) already called
    - **Expected output**: No explicit output; calling addConnection(T2, T1) is a no-op; T1's neighbor list still has size 1

### Method under test: `List<ITerritory> getNeighbors(ITerritory territory)`

`territory` is a Pointer. The neighbors collection is a Count variable.

- **TC14: null territory** ( :white_check_mark: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC15: territory not in map** ( :white_check_mark: )
    - **State of the system**: GameMap does not contain T1
    - **Expected output**: empty list
- **TC16: territory in map with no connections** ( :white_check_mark: )
    - **State of the system**: GameMap contains T1; no connections involve T1
    - **Expected output**: empty list
- **TC17: territory with exactly 1 neighbor** ( :white_check_mark: )
    - **State of the system**: GameMap contains T1, T2; addConnection(T1, T2) called
    - **Expected output**: list of size 1 containing T2
- **TC18: territory with multiple neighbors** ( :white_check_mark: )
    - **State of the system**: GameMap contains T1, T2, T3; addConnection(T1, T2) and addConnection(T1, T3) called
    - **Expected output**: list of size 2 containing T2 and T3

### Method under test: `boolean areAdjacent(ITerritory a, ITerritory b)`

`a` and `b` are Pointers. Both must be in the map. Adjacency is symmetric.

- **TC19: a is null** ( :x: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC20: b is null** ( :x: )
    - **State of the system**: any
    - **Expected output**: IllegalArgumentException thrown
- **TC21: a not in map** ( :x: )
    - **State of the system**: GameMap contains T2 but not T1
    - **Expected output**: IllegalArgumentException thrown
- **TC22: b not in map** ( :x: )
    - **State of the system**: GameMap contains T1 but not T2
    - **Expected output**: IllegalArgumentException thrown
- **TC23: both in map, not connected** ( :x: )
    - **State of the system**: GameMap contains T1, T2; no connection between them
    - **Expected output**: false
- **TC24: both in map, connected — query (a, b)** ( :x: )
    - **State of the system**: GameMap contains T1, T2; addConnection(T1, T2) called
    - **Expected output**: true
- **TC25: both in map, connected — reverse query (b, a)** ( :x: )
    - **State of the system**: GameMap contains T1, T2; addConnection(T1, T2) called
    - **Expected output**: true (bidirectionality)