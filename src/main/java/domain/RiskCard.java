package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class RiskCard {
  private final RiskCardType riskCardType;
  private final Territory territory;

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Territory is an aggregate domain object intentionally shared by reference; "
          + "the card identifies an actual board territory, not a copy."
  )
  public RiskCard(RiskCardType riskCardType, Territory territory) {
    if (riskCardType == null) {
      throw new IllegalArgumentException("Risk Card Type cannot be null");
    }
    if (territory == null) {
      throw new IllegalArgumentException("Territory associated with Risk Card cannot be null");
    }
    this.riskCardType = riskCardType;
    this.territory = territory;
  }

  public RiskCardType getType() {
    return this.riskCardType;
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Returns the shared aggregate Territory by design; see ctor justification."
  )
  public Territory getTerritory() {
    return this.territory;
  }
}
