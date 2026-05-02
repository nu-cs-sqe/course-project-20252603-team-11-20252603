package domain;

public interface ITerritory {
    String getName();
    Player getOwner();
    int getTroopCount();
    void setOwner(Player owner);
    void addTroops(int amount);
}
