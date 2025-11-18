package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.controller.games.dto.CellDTO;
import fr.campus.squaregamesapi.controller.games.dto.GameDTO;
import fr.campus.squaregamesapi.controller.games.dto.PlayersDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final List<GamePlugin> gamePlugins;
    private GamePlugin gamePlugin;

    private final Map<String, Game> games = new HashMap<>();

    @Autowired
    public GameServiceImpl(List<GamePlugin> gamePlugins) {
        this.gamePlugins = gamePlugins;
    }

    public void setGamePlugin(String gameId) {
        for (GamePlugin plugin : gamePlugins) {
            if (plugin.getId().equalsIgnoreCase(gameId)) {
                this.gamePlugin = plugin;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid game id: " + gameId);
    }

    public Game createGame() {
        Game game = this.gamePlugin.createGame();
        games.put(game.getId().toString(), game);
        return game;
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public String getGameStatus(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return game.getStatus().toString();
    }

    public GameDTO getGameDTO(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        PlayersDTO players = new PlayersDTO(
                game.getPlayerIds().toArray(new UUID[0])[0].toString(),
                game.getPlayerIds().toArray(new UUID[0])[1].toString()
        );
        List<CellDTO> cells = game.getBoard().entrySet().stream()
                .map(entry -> new CellDTO(
                        entry.getKey().x(),
                        entry.getKey().y(),
                        entry.getValue().getName(),                         // "X" ou "0"
                        entry.getValue().getOwnerId().orElseThrow().toString()
                ))
                .toList();

        int remainingMoves = game.getRemainingTokens().size();

        int size = game.getBoardSize();

        String[][] grid = new String[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = ".";
            }
        }

        game.getBoard().forEach((position, token) -> {
            grid[position.x()][position.y()] = token.getName();
        });

        return new GameDTO(
                gameId,
                game.getBoardSize(),
                game.getStatus().name(),
                game.getCurrentPlayerId().toString(),
                game.getStatus().name().equals("TERMINATED") && game.getCurrentPlayerId() != null
                        ? game.getCurrentPlayerId().toString()
                        : null,
                remainingMoves,
                players,
                cells,
                grid
        );
    }
}
