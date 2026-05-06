package domain;

public class RiskCard {
    public final RiskCardType riskCardType;
    public final Territory territory;

    public RiskCard(RiskCardType riskCardType, Territory territory) {
        if (riskCardType == null) {
            throw new IllegalArgumentException("Risk Card Type cannot be null");
        }
        this.riskCardType = riskCardType;
        this.territory = territory;
    }

    public RiskCardType getType(){
        return this.riskCardType;
    }

    public Territory getTerritory(){
        return this.territory;
    }
}
