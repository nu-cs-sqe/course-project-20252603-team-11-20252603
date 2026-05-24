# Territory Class BVA

## Territory(String name, Player owner, int troopCount)
|             | System under test | Expected output                 | Implemented?       |
|-------------|-------------------|---------------------------------|--------------------|
| Test Case 1 | null name         | IllegalArgumentException thrown | :white_check_mark: |
| Test Case 2 | empy name         | IllegalArgumentException thrown | :white_check_mark: |
| Test Case 3 | valid name        | this.name = name                | :white_check_mark: |
| Test Case 4 | null Player       | IllegalArgumentException thrown | :white_check_mark: |
| Test Case 5 | valid Player      | this.player = Player            | :white_check_mark: |

## Territory(String name)

|              | System under test | Expected output                                                       | Implemented? |
|--------------|-------------------|-----------------------------------------------------------------------|--------------|
| Test Case 15 | null name         | IllegalArgumentException thrown                                       | :white_check_mark: |
| Test Case 16 | empty name        | IllegalArgumentException thrown                                       | :white_check_mark: |
| Test Case 17 | valid name        | getName() == name; getOwner() == null (unowned); getTroopCount() == 0 | :x:          |

## getName()

Getter

## addTroops(int amount)

|             | System under test       | Expected output  | Implemented?       |
|-------------|-------------------------|------------------|--------------------|
| Test Case 6 | 5 troops, addTroops(-1) | Exception thrown | :white_check_mark: |
| Test Case 7 | 0 troops, addTroops(0)  | Exception thrown | :white_check_mark: |
| Test Case 8 | 1 troop, addTroops(1)   | troopCount = 2   | :white_check_mark: |

## removeTroops(int amount)

|              | System under test         | Expected output  | Implemented?       |
|--------------|---------------------------|------------------|--------------------|
| Test Case 9  | 1 troop, RemoveTroops(1)  | Exception thrown | :white_check_mark: |
| Test Case 10 | 1 troop, RemoveTroops(2)  | Exception thrown | :white_check_mark: |
| Test Case 11 | 5 troops, RemoveTroops(4) | troopCount = 1   | :white_check_mark: |

## setOwner(Player p1)

|              | System under test                      | Expected output       | Implemented?       |
|--------------|----------------------------------------|-----------------------|--------------------|
| Test Case 14 | territory owned by P1, setOwner(P2)    | owner = P2            | :white_check_mark: |

## getOwner()

Getter

## conquer(Player newOwner, int troopsMovedIn)

|              | System under test            | Expected output           | Implemented?       |
|--------------|------------------------------|---------------------------|--------------------|
| Test Case 12 | owner = P1, newOwner = P2, 0 | Exception thrown          | :white_check_mark: |
| Test Case 13 | owner = P1, newOwner = P2, 1 | owner = p2, 1 troop count | :white_check_mark: |