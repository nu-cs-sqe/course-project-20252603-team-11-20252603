# ConnectivityGraph - BVA Analysis

### Method under test: `ConnectivityGraph(GameMap map)`

- **TC1: null map** ( :white_check_mark: )
    - **State of the system**: No ConnectivityGraph created yet
    - **Expected output**: IllegalArgumentException thrown

- **TC2: valid map** ( :white_check_mark: )
    - **State of the system**: No ConnectivityGraph created yet; valid GameMap provided
    - **Expected output**: ConnectivityGraph constructed without exception

### Method under test: `boolean isConnected(Territory s, Territory d, Player owner)`

- **TC3: null source** ( :x: )
    - **State of the system**: ConnectivityGraph constructed with valid map
    - **Expected output**: IllegalArgumentException thrown

- **TC4: null destination** ( :x: )
    - **State of the system**: ConnectivityGraph constructed; valid source provided
    - **Expected output**: IllegalArgumentException thrown

- **TC5: source == destination (same territory object)** ( :x: )
    - **State of the system**: ConnectivityGraph constructed; player owns T1
    - **Expected output**: IllegalArgumentException thrown

- **TC6: path exists through owner-owned territories** ( :x: )
    - **State of the system**: map.findPath(s, d, owner) returns a non-empty list
    - **Expected output**: true

- **TC7: no path through owner-owned territories** ( :x: )
    - **State of the system**: map.findPath(s, d, owner) returns empty list
    - **Expected output**: false

### Method under test: `List<Territory> findPath(Territory s, Territory d, Player owner)`

- **TC8: path found — delegates to GameMap and returns path** ( :x: )
    - **State of the system**: map.findPath(s, d, owner) returns [s, d]
    - **Expected output**: [s, d]

- **TC9: no path — delegates to GameMap and returns empty list** ( :x: )
    - **State of the system**: map.findPath(s, d, owner) returns empty list
    - **Expected output**: empty list (not null)

### Method under test: `Set<Territory> getReachable(Territory src, Player owner)`

- **TC10: null src** ( :x: )
    - **State of the system**: ConnectivityGraph constructed
    - **Expected output**: IllegalArgumentException thrown

- **TC11: null owner** ( :x: )
    - **State of the system**: ConnectivityGraph constructed; valid src provided
    - **Expected output**: IllegalArgumentException thrown

- **TC12: src not owned by owner — returns empty set** ( :x: )
    - **State of the system**: src.getOwner() returns a different player
    - **Expected output**: empty set

- **TC13: src owned by owner, no neighbors — returns singleton set** ( :x: )
    - **State of the system**: src.getOwner() == owner; map.getNeighbors(src) returns empty list
    - **Expected output**: {src}

- **TC14: src owned by owner, one owner-owned neighbor — returns both** ( :x: )
    - **State of the system**: src owned by owner; map.getNeighbors(src) returns [neighbor]; neighbor owned by owner;
      map.getNeighbors(neighbor) returns []
    - **Expected output**: {src, neighbor}

- **TC15: src owned by owner, enemy neighbor only — returns src only** ( :x: )
    - **State of the system**: src owned by owner; map.getNeighbors(src) returns [enemy]; enemy owned by other player
    - **Expected output**: {src}

- **TC16: chain of three owner-owned territories — returns all three** ( :x: )
    - **State of the system**: src→mid→end all owned by owner; BFS traverses chain
    - **Expected output**: {src, mid, end}
