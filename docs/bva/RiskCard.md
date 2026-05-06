# RiskCard - BVA Analysis

### Method under test: `RiskCard(RiskCardType riskCardType, Territory territory)`
- **TC1: null riskCardType** ( :white_check_mark: )
  - **State of the system**: No card created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC2: null territory** ( :white_check_mark: or :x: )
  - **State of the system**: No card created yet
  - **Expected output**: IllegalArgumentException thrown
- **TC3: valid riskCardType and territory** ( :white_check_mark: or :x: )
  - **State of the system**: No card created yet
  - **Expected output**: RiskCard created; getType() returns the provided type, getTerritory() returns the provided territory

### Method under test: `RiskCardType getType()`

`riskCardType` is a Case variable over { INFANTRY, CAVALRY, ARTILLERY, WILDCARD }.

- **TC4: INFANTRY type** ( :white_check_mark: or :x: )
  - **State of the system**: RiskCard constructed with RiskCardType.INFANTRY
  - **Expected output**: getType() returns RiskCardType.INFANTRY
- **TC5: CAVALRY type** ( :white_check_mark: or :x: )
  - **State of the system**: RiskCard constructed with RiskCardType.CAVALRY
  - **Expected output**: getType() returns RiskCardType.CAVALRY
- **TC6: ARTILLERY type** ( :white_check_mark: or :x: )
  - **State of the system**: RiskCard constructed with RiskCardType.ARTILLERY
  - **Expected output**: getType() returns RiskCardType.ARTILLERY
- **TC7: WILDCARD type** ( :white_check_mark: or :x: )
  - **State of the system**: RiskCard constructed with RiskCardType.WILDCARD
  - **Expected output**: getType() returns RiskCardType.WILDCARD


### Method under test: `Territory getTerritory()`

Getter
