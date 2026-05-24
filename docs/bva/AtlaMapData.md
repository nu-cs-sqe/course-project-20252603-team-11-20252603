# AtlaMapData - BVA Analysis

## Design notes

**Unowned-Territory contract**: `buildMap()` constructs every `Territory` via
`Territory(String name)`, which encapsulates a `null` owner internally and sets
`troopCount` to 0. No `null` is ever passed across a call site.
`Game.assignTerritories()` subsequently calls `setOwner` and `addTroops(1)` on
each territory during setup.

**Continent seam**: `AtlaMapData` owns the 5 faction groupings (Moon Tribe,
Ba Sing Se Kingdom, Fire Nation, Omashu Kingdom, Ocean Tribe) and their territory
membership. `continentCount()` is the single tested source of truth that a future
`Player.calculateTroops` consumer will call. Do not implement that consumer here.

---

### Method under test: `buildMap()`

- **TC1: total territory count** ( :white_check_mark: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: `buildMap().getTerritories().size() == 42`

- **TC2: Moon Tribe faction count** ( :white_check_mark: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: exactly 7 territories named: Northern Tundra,
      Frost Hills, Moon Tribe, Bin-Er, Yue Bay, Wulong, Western Air Temple

- **TC3: Ba Sing Se Kingdom faction count** ( :white_check_mark: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: exactly 10 territories named: Zigan, Northern Air
      Temple, Northern Mountains, Taihua, Ba Sing Se City, Ba Sing Se Province,
      Continental Corridor, Taku, Green Farmlands, Charmeleon Province

- **TC4: Fire Nation faction count** ( :x: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: exactly 7 territories named: Sun Isles, Burning Gates,
      Caldera City, Black Cliffs, Keonso, Fire Archipelago, Whale Tail Isle

- **TC5: Omashu Kingdom faction count** ( :x: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: exactly 10 territories named: Hei Bei, Great Divide,
      Serpent Pass, Full Moon Bay, Omashu, Western Si Wong Desert, Eastern Si
      Wong Desert, Heart Farmlands, Chin, Gao Ling

- **TC6: Ocean Tribe faction count** ( :x: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: exactly 8 territories named: Eastern Air Temple,
      Seafoam Isles, Hakoda Island, Shimsom Isle, Southern Air Temple, Ocean
      Tribe, Southern Tundra, Icy Plains

- **TC7: all territories start unowned** ( :x: )
    - **State of the system**: `buildMap()` returned; no players assigned
    - **Expected output**: `getOwner() == null` for every territory in the map

- **TC8: all territories start with zero troops** ( :x: )
    - **State of the system**: `buildMap()` returned; no players assigned
    - **Expected output**: `getTroopCount() == 0` for every territory in the map

- **TC9: no self-loops** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: for every territory `t`, `t` is not contained in
      `getNeighbors(t)`

- **TC10: no duplicate edges** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: for every territory `t`, `getNeighbors(t)` has no
      duplicates (set size equals list size)

- **TC11: Moon Tribe intra-faction adjacencies** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: all 7 edges present and symmetric (areAdjacent both ways):
        - Northern Tundra — Frost Hills
        - Northern Tundra — Moon Tribe
        - Frost Hills — Bin-Er
        - Moon Tribe — Bin-Er
        - Bin-Er — Yue Bay
        - Yue Bay — Wulong
        - Wulong — Western Air Temple

- **TC12: Ba Sing Se Kingdom intra-faction adjacencies** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: all 11 edges present and symmetric:
        - Zigan — Northern Air Temple
        - Zigan — Northern Mountains
        - Northern Air Temple — Northern Mountains
        - Northern Mountains — Taihua
        - Taihua — Ba Sing Se City
        - Ba Sing Se City — Ba Sing Se Province
        - Ba Sing Se Province — Continental Corridor
        - Continental Corridor — Taku
        - Taku — Green Farmlands
        - Green Farmlands — Charmeleon Province
        - Charmeleon Province — Ba Sing Se Province

- **TC13: Fire Nation intra-faction adjacencies** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: all 7 edges present and symmetric:
        - Sun Isles — Burning Gates
        - Sun Isles — Caldera City
        - Burning Gates — Caldera City
        - Caldera City — Black Cliffs
        - Black Cliffs — Keonso
        - Keonso — Fire Archipelago
        - Fire Archipelago — Whale Tail Isle

- **TC14: Omashu Kingdom intra-faction adjacencies** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: all 11 edges present and symmetric:
        - Hei Bei — Great Divide
        - Great Divide — Serpent Pass
        - Serpent Pass — Full Moon Bay
        - Full Moon Bay — Omashu
        - Omashu — Western Si Wong Desert
        - Omashu — Eastern Si Wong Desert
        - Western Si Wong Desert — Heart Farmlands
        - Eastern Si Wong Desert — Heart Farmlands
        - Heart Farmlands — Chin
        - Chin — Gao Ling
        - Gao Ling — Hei Bei

- **TC15: Ocean Tribe intra-faction adjacencies** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: all 8 edges present and symmetric:
        - Eastern Air Temple — Seafoam Isles
        - Seafoam Isles — Hakoda Island
        - Hakoda Island — Shimsom Isle
        - Shimsom Isle — Southern Air Temple
        - Southern Air Temple — Ocean Tribe
        - Ocean Tribe — Southern Tundra
        - Southern Tundra — Icy Plains
        - Icy Plains — Eastern Air Temple

- **TC16: sea route — Whale Tail Isle to Shimsom Isle** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: areAdjacent(Whale Tail Isle, Shimsom Isle) == true
      and areAdjacent(Shimsom Isle, Whale Tail Isle) == true
      (Fire Nation ↔ Ocean Tribe cross-faction)

- **TC17: sea routes — Bin-Er and Yue Bay to Zigan** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: Bin-Er ↔ Zigan and Yue Bay ↔ Zigan both adjacent and
      symmetric (Moon Tribe ↔ Ba Sing Se Kingdom)

- **TC18: sea route — Gao Ling to Seafoam Isles** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: areAdjacent(Gao Ling, Seafoam Isles) == true and
      areAdjacent(Seafoam Isles, Gao Ling) == true
      (Omashu Kingdom ↔ Ocean Tribe)

- **TC19: sea route — Hakoda Island to Chin** ( :x: )
    - **State of the system**: `buildMap()` returned
    - **Expected output**: areAdjacent(Hakoda Island, Chin) == true and
      areAdjacent(Chin, Hakoda Island) == true
      (Ocean Tribe ↔ Omashu Kingdom)

---

### Method under test: `continentCount()`

- **TC20: continent count derived from faction structure** ( :x: )
    - **State of the system**: `AtlaMapData` instantiated
    - **Expected output**: `continentCount() == 5`; value is derived from the
      faction data structure, not a literal
