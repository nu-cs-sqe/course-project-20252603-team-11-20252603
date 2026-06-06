package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class ReinforcementPhase {
  private final Player player;
  private int troopsToPlace;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Player is an aggregate domain object intentionally shared by reference; "
          + "the phase needs to mutate and read the same Player as the rest of the Turn."
  )
  public ReinforcementPhase(Player player, int troopsToPlace) {
    this.player = player;
    this.troopsToPlace = troopsToPlace;
  }

  public boolean validatePlacement(int troops, Territory territory) {
    if (troops == 0) {
      return false;
    }
    for (Territory t : player.getTerritories()) {
      if (t.equals(territory)) {
        return troops <= troopsToPlace;
      }
    }
    return false;
  }

  public void placeTroops(int troops, Territory territory) {
    if (!validatePlacement(troops, territory)) {
      throw new IllegalArgumentException("Invalid troop placement");
    }
    territory.addTroops(troops);
    this.troopsToPlace -= troops;
  }

  public int getRemaining() {
    return this.troopsToPlace;
  }

  public boolean isComplete() {
    return this.troopsToPlace == 0;
  }
}
