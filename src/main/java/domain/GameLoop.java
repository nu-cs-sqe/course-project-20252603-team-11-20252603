package domain;

import java.util.List;
import java.util.stream.Collectors;

public class GameLoop {
  private final Game game;

  public GameLoop(Game game) {
    if (game == null) {
      throw new IllegalArgumentException("game cannot be null.");
    }
    this.game = game;
  }

  public Game getGame() {
    return game;
  }

  public boolean checkWinCondition() {
    List<Player> activePlayers =
        game.getPlayers().stream()
            .filter(p -> !p.isEliminated() && p.getTerritoryCount() > 0)
            .collect(Collectors.toList());

    if (activePlayers.size() == 1) {
      game.setGameState(GameState.GAME_OVER);
      game.setWinner(activePlayers.get(0));
      return true;
    }
    return false;
  }
}
