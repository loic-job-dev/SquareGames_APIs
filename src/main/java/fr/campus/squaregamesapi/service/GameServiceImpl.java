package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.controller.games.dto.tictactoe.TicTacToeCellDTO;
import fr.campus.squaregamesapi.controller.games.dto.tictactoe.TicTacToeGameDTO;
import fr.campus.squaregamesapi.controller.games.dto.tictactoe.TicTacToePlayersDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public TicTacToeGameDTO getGameDTO(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        TicTacToePlayersDTO players = new TicTacToePlayersDTO(
                game.getPlayerIds().toArray(new UUID[0])[0].toString(),
                game.getPlayerIds().toArray(new UUID[0])[1].toString()
        );
        List<TicTacToeCellDTO> cells = game.getBoard().entrySet().stream()
                .map(entry -> new TicTacToeCellDTO(
                        entry.getKey().x(),
                        entry.getKey().y(),
                        entry.getValue().getName(),                         // "X" ou "0"
                        entry.getValue().getOwnerId().orElseThrow().toString()
                ))
                .toList();

        int remainingMoves = game.getRemainingTokens().size();

        int size = game.getBoardSize();

        String[][] grid = new String[size][size];

        // initialisation
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = ".";
            }
        }

        // remplissage avec les tokens
        game.getBoard().forEach((position, token) -> {
            grid[position.y()][position.x()] = token.getName();
        });

        return new TicTacToeGameDTO(
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

    @Override
    public TicTacToeGameDTO playGame(String gameId, int j, int k) throws InvalidPositionException {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        System.out.println("remaining tokens " + game.getRemainingTokens()); //remaining tokens [X,  O, X,  O, X,  O, X,  O, X]

        if (game.getFactoryId().equals("tictactoe")) {

            CellPosition cellPosition = new CellPosition(j, k);

            List<Token> tokenList = game.getRemainingTokens().stream().toList();
            tokenList.get(0).moveTo(cellPosition);

            return getGameDTO(gameId);
        } else {
            return null;
        }
    }
}
