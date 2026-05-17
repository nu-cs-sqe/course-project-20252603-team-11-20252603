package domain;

public class ReinforcementPhase {
    private final Player player;
    private int troopsToPlace = 0;

    public ReinforcementPhase(Player player) {
        this.player = player;
    }

    public boolean validatePlacement(int troops, Territory territory) {
        return false;
    }


}
