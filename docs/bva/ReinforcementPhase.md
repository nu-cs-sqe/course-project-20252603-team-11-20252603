# Reinforcement Phase BVA

## placeTroops(int amount, Territory territory)

|             | System under test                            | Expected output                                                      | Implemented?       |
|-------------|----------------------------------------------|----------------------------------------------------------------------|--------------------|
| Test Case 1 | Place one valid troop on your territory      | Territory troops increases by one, troops left decreases by one      | :white_check_mark: |
| Test Case 2 | Place max valid troops on your territory     | Territory troops increases by 10, troops left decreases to 0 from 10 | :white_check_mark: |
| Test Case 2 | Place max + 1 valid troops on your territory | IllegalArgumentException                                             | :white_check_mark: |
| Test Case 1 | Place troop on invalid territory             | IllegalArgumentException                                             | :white_check_mark: |

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

