# Territory Class BVA

## addTroops(int amount)

|              | System under test       | Expected output  | Implemented?              |
|--------------|-------------------------|------------------|---------------------------|
| Test Case 1  | 5 troops, addTroops(-1) | Exception thrown | :x: or :white_check_mark: |
| Test Case 2  | 0 troops, addTroops(0)  | Exception thrown | :x: or :white_check_mark: |
| Test Case 3  | 1 troop, addTroops(1)   | troopCount = 2   | :x: or :white_check_mark: |

## removeTroops(int amount)

|              | System under test         | Expected output  | Implemented?              |
|--------------|---------------------------|------------------|---------------------------|
| Test Case 1  | 1 troop, RemoveTroops(1)  | Exception thrown | :x: or :white_check_mark: |
| Test Case 2  | 1 troop, RemoveTroops(2)  | Exception thrown | :x: or :white_check_mark: |
| Test Case 3  | 5 troops, RemoveTroops(4) | troopCount = 1   | :x: or :white_check_mark: |

## setOwner(Player p1)

Setter

## getOwner()

Getter

## conquer(Player newOwner, int troopsMovedIn)

|              | System under test            | Expected output           | Implemented?              |
|--------------|------------------------------|---------------------------|---------------------------|
| Test Case 1  | owner = P1, newOwner = P2, 0 | Exception thrown          | :x: or :white_check_mark: |
| Test Case 2  | owner = P1, newOwner = P2, 1 | owner = p2, 1 troop count | :x: or :white_check_mark: |