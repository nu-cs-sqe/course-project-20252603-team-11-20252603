package domain;

public class Territory {
    private final String name;
    private Player owner;
    private int troopCount;

    public Territory(String name, Player owner, int troopCount) {
        this.name = name;
        this.owner = owner;
        this.troopCount = troopCount;
    }

    public String getName() { return name; }

    public void addTroops(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Input must be non-negative integer");
        }
        this.troopCount += amount;
    }

    public void removeTroops(int amount) {
        if (amount >= this.troopCount) {
            throw new IllegalArgumentException("Territories must have at least 1 troop");
        }
        this.troopCount -= amount;
    }

    public int getTroopCount() {
        return this.troopCount;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void conquer(Player newOwner, int troopsMovedIn) {
        if (troopsMovedIn == 0) {
            throw new IllegalArgumentException("Conquered Territories must have at least 1 troop");
        }
        this.owner = newOwner;
        this.troopCount = troopsMovedIn;
    }

}
