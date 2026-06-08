package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConnectivityGraphTests {

  @Test
  public void constructor_nullMap_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new ConnectivityGraph(null));
  }
}
