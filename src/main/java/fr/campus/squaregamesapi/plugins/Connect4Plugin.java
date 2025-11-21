package fr.campus.squaregamesapi.plugins;

import fr.campus.squaregamesapi.dto.AbstractGameDTO;
import fr.campus.squaregamesapi.dto.connect4.Connect4TokenDTO;
import fr.campus.squaregamesapi.dto.connect4.Connect4AbstractGameDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class Connect4Plugin implements GamePlugin {

    private GameFactory gameFactory =new ConnectFourGameFactory();

    @Value("${game.connect4.default-name}")
    private String gameId;

    @Value("${game.connect4.default-player-count}")
    private int defaultPlayerCount;

    @Value("${game.connect4.default-board-size}")
    private int defaultBoardSize;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getId() {
        return gameId;
    }

    @Override
    public Game createGame() {
        return gameFactory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("connect4.name", null, locale);
    }

    @Override
    public AbstractGameDTO buildDTO(Game game) {

        ConnectFourGame cf = (ConnectFourGame) game;

        String redPlayer = cf.getPlayerIds().toArray(new UUID[0])[0].toString();
        String yellowPlayer = cf.getPlayerIds().toArray(new UUID[0])[1].toString();

        String current = cf.getCurrentPlayerId() != null ? cf.getCurrentPlayerId().toString() : null;

        String winner = null;
        if (cf.getWinningLine() != null && !cf.getWinningLine().isEmpty()) {
            winner = cf.getWinningLine().get(0).getOwnerId().orElse(null).toString();
        }

        int remaining = cf.getRemainingTokens().size();

        // --- Construction de la grille (6 lignes × 7 colonnes)
        String[][] grid = new String[6][7];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                grid[row][col] = ".";
            }
        }

        // --- Remplir avec les tokens
        List<Connect4TokenDTO> tokens = cf.getBoard().entrySet().stream()
                .map(entry -> {
                    int x = entry.getKey().x();
                    int y = entry.getKey().y();
                    String symbol = entry.getValue().getName();
                    String owner = entry.getValue().getOwnerId().orElseThrow().toString();

                    grid[y][x] = symbol;

                    return new Connect4TokenDTO(x, y, symbol, owner);
                })
                .toList();

        return new Connect4AbstractGameDTO(
                cf.getId().toString(),
                cf.getStatus().name(),
                redPlayer,
                yellowPlayer,
                current,
                winner,
                remaining,
                grid,
                tokens
        );
    }

    @Override
    public void play(Game game, int column, int ignored) throws InvalidPositionException {
        // Connect4 ignore la ligne : gravité gérée par le moteur
        game.getRemainingTokens()
                .stream()
                .findFirst()
                .orElseThrow()
                .moveTo(new CellPosition(column, -1));
    }
}
