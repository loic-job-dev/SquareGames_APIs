package fr.campus.squaregamesapi.plugins;

import fr.campus.squaregamesapi.dto.tictactoe.TicTacToeCellDTO;
import fr.campus.squaregamesapi.dto.tictactoe.TicTacToeAbstractGameDTO;
import fr.campus.squaregamesapi.dto.tictactoe.TicTacToePlayersDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class TicTacToePlugin implements GamePlugin {

    private GameFactory gameFactory =new TicTacToeGameFactory();

    @Value("${game.tictactoe.default-name}")
    private String gameId;

    @Value("${game.tictactoe.default-player-count}")
    private int defaultPlayerCount;

    @Value("${game.tictactoe.default-board-size}")
    private int defaultBoardSize;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getId() {
        return gameId; // "tictactoe"
    }

    @Override
    public Game createGame() {
        return gameFactory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("tictactoe.name", null, locale);
    }

    @Override
    public TicTacToeAbstractGameDTO buildDTO(Game game) {
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

        return new TicTacToeAbstractGameDTO(
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
    public void play(Game game, int x, int y) throws InvalidPositionException {
        CellPosition cellPosition = new CellPosition(x, y);

        List<Token> tokenList = game.getRemainingTokens().stream().toList();
        tokenList.get(0).moveTo(cellPosition);
    }
}
