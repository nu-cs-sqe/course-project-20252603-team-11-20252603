package domain;

public class ReinforcementPhase {
    private final Player player;
    private int troopsToPlace;

    public ReinforcementPhase(Player player, int troopsToPlace) {
        this.player = player;
        this.troopsToPlace = troopsToPlace;
    }

    public boolean validatePlacement(int troops, Territory territory) {
        if (troops == 0) return false;
        for (Territory t : player.getTerritories()) {
            if (t.equals(territory)) {
                return troops <= troopsToPlace;
            }
        }
        return false;
    }

    public int getTroopsToPlace() {
        return this.troopsToPlace;
    }


}
