package domain;

public class Territory {
    private Player owner;
    private int troopCount;

    Territory(Player owner, int troopCount) {
        this.owner = owner;
        this.troopCount = troopCount;
    }

    public void addTroops(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Input must be non-negative integer");
        }
        this.troopCount += amount;
    }

    public int getTroopCount() {
        return this.troopCount;
    }

}
