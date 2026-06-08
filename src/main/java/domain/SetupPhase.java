package domain;

public class SetupPhase {
  public SetupPhase(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
  }
}
