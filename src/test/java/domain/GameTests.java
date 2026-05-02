package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    // ! Helpers
    private List<Player> makePlayers(int count) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            players.add(EasyMock.createMock(Player.class));
        }
        return players;
    }

    private IGameMap makeMap() {
        return EasyMock.createMock(IGameMap.class);
    }

    private void replayAll(List<Player> players, IGameMap map) {
        players.forEach(EasyMock::replay);
        EasyMock.replay(map);
    }

    private void verifyAll(List<Player> players, IGameMap map) {
        players.forEach(EasyMock::verify);
        EasyMock.verify(map);
    }

    private List<ITerritory> makeTerritories(int count) {
        List<ITerritory> territories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            territories.add(EasyMock.createMock(ITerritory.class));
        }
        return territories;
    }

    private void expectTerritoryAssignment(ITerritory territory, Player owner) {
        territory.setOwner(owner);
        territory.addTroops(1);
    }

    // ! Constructor tests
    @Test
    public void constructor_nullPlayers_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(null, map));
        EasyMock.verify(map);
    }

    @Test
    public void constructor_emptyPlayersList_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(new ArrayList<>(), map));
        EasyMock.verify(map);
    }

    @Test
    public void constructor_onePlayer_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(1);
        replayAll(players, map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        verifyAll(players, map);
    }

    @Test
    public void constructor_twoPlayers_gameConstructedWithPlayersAndMap() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(2, game.getPlayerCount());
        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }

    @Test
    public void constructor_sixPlayers_gameConstructedWithPlayersAndMap() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(6);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(6, game.getPlayerCount());
        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }

    @Test
    public void constructor_sevenPlayers_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(7);
        replayAll(players, map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        verifyAll(players, map);
    }

    @Test
    public void constructor_playersListContainsNull_throwsIllegalArgumentException() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(1);
        players.add(null);
        players.forEach(p -> { if (p != null) EasyMock.replay(p); });
        EasyMock.replay(map);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, map));
        players.forEach(p -> { if (p != null) EasyMock.verify(p); });
        EasyMock.verify(map);
    }

    @Test
    public void constructor_nullMap_throwsIllegalArgumentException() {
        List<Player> players = makePlayers(2);
        players.forEach(EasyMock::replay);
        assertThrows(IllegalArgumentException.class, () -> new Game(players, null));
        players.forEach(EasyMock::verify);
    }

    // ! assignTerritories tests
    @Test
    public void assignTerritories_oneTerritory_assignedToFirstPlayerWithOneTroop() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        ITerritory territory = EasyMock.createMock(ITerritory.class);

        EasyMock.expect(map.getTerritories()).andReturn(List.of(territory));
        players.get(0).addTerritory(territory);
        territory.setOwner(players.get(0));
        territory.addTroops(1);

        replayAll(players, map);
        EasyMock.replay(territory);

        Game game = new Game(players, map);
        game.assignTerritories();

        verifyAll(players, map);
        EasyMock.verify(territory);
    }

    @Test
    public void assignTerritories_fourTerritoriesTwoPlayers_eachPlayerGetsTwoTerritories() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        List<ITerritory> territories = makeTerritories(4);

        EasyMock.expect(map.getTerritories()).andReturn(territories);
        players.get(0).addTerritory(territories.get(0));
        players.get(0).addTerritory(territories.get(2));
        players.get(1).addTerritory(territories.get(1));
        players.get(1).addTerritory(territories.get(3));
        expectTerritoryAssignment(territories.get(0), players.get(0));
        expectTerritoryAssignment(territories.get(1), players.get(1));
        expectTerritoryAssignment(territories.get(2), players.get(0));
        expectTerritoryAssignment(territories.get(3), players.get(1));

        replayAll(players, map);
        territories.forEach(EasyMock::replay);

        Game game = new Game(players, map);
        game.assignTerritories();

        verifyAll(players, map);
        territories.forEach(EasyMock::verify);
    }

    @Test
    public void assignTerritories_noTerritories_noTerritoriesAddedToPlayers() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        EasyMock.expect(map.getTerritories()).andReturn(new ArrayList<>());
        replayAll(players, map);

        Game game = new Game(players, map);
        game.assignTerritories();

        verifyAll(players, map);
    }

    @Test
    public void constructor_validPlayersAndMap_mapStoredCorrectly() {
        IGameMap map = makeMap();
        List<Player> players = makePlayers(2);
        replayAll(players, map);

        Game game = new Game(players, map);

        assertEquals(map, game.getMap());
        verifyAll(players, map);
    }
}
