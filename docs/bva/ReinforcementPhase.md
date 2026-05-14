# Reinforcement Phase BVA

## calculateTroops()

|             | System under test              | Expected output  | Implemented?       |
|-------------|--------------------------------|------------------|--------------------|
| Test Case 1 | 0 territories, 0 continents    | Exception thrown | :white_check_mark: |
| Test Case 2 | 1 territory, 1 (3pt) continent | 6 new troops     | :white_check_mark: |
| Test Case 3 | 11 territory, 0 continents     | 3 new troops     | :white_check_mark: |
| Test Case 4 | 12 territory, 0 continents     | 4 new troops     | :white_check_mark: |
| Test Case 5 | 42 territories, 5 continents   | 35 new troops    | :white_check_mark: |

## placeTroops(int amount, Territory territory)

|             | System under test                        | Expected output                                                      | Implemented?       |
|-------------|------------------------------------------|----------------------------------------------------------------------|--------------------|
| Test Case 1 | Place one valid troop on your territory  | Territory troops increases by one, troops left decreases by one      | :white_check_mark: |
| Test Case 2 | Place max valid troops on your territory | Territory troops increases by 10, troops left decreases to 0 from 10 | :white_check_mark: |

## isComplete()

|             | System under test         | Expected output | Implemented?       |
|-------------|---------------------------|-----------------|--------------------|
| Test Case 1 | 0 remaining troops        | True            | :white_check_mark: |
| Test Case 2 | 1 remaining troop         | False           | :white_check_mark: |

## getRemaining()

Getter

## validatePlacement(int amount, Territory territory)

|             | System under test                                          | Expected output | Implemented?        |
|-------------|------------------------------------------------------------|-----------------|---------------------|
| Test Case 1 | Territory not owned                                        | False           | :white_check_mark:  |
| Test Case 2 | Place 4 troops on your territory when have 3 left to place | False           | :white_check_mark:  |
| Test Case 3 | Place 1 troops on your territory when have 3 left to place | True            | :white_check_mark:  |
| Test Case 4 | Place 0 troops                                             | False           | :white_check_mark:  |

