package fr.campus.squaregamesapi.plugins;

import fr.campus.squaregamesapi.dto.GameDTO;
import fr.campus.squaregamesapi.dto.taquin.TaquinGameDTO;
import fr.campus.squaregamesapi.dto.taquin.TaquinTileDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class TaquinPlugin implements GamePlugin {

    private GameFactory gameFactory =new TaquinGameFactory();

    @Value("${game.15puzzle.default-name}")
    private String gameId;

    @Value("${game.15puzzle.default-player-count}")
    private int defaultPlayerCount;

    @Value("${game.15puzzle.default-board-size}")
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
        return messageSource.getMessage("15puzzle.name", null, locale);
    }

    private CellPosition findEmpty(TaquinGame tg) {
        int size = tg.getBoardSize();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                CellPosition pos = new CellPosition(x, y);
                if (!tg.getBoard().containsKey(pos)) {
                    return pos; // case vide trouvée
                }
            }
        }
        throw new RuntimeException("No empty position found on the board");
    }

    @Override
    public GameDTO buildDTO(Game game) {
        TaquinGame tg = (TaquinGame) game;
        int size = tg.getBoardSize();

        // Grille initialisée avec "."
        String[][] grid = new String[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = ".";
            }
        }

        // Récupérer la case vide
        CellPosition empty = findEmpty(tg);

        // Générer les tiles avec autorisation de mouvement uniquement si elles sont adjacentes à la case vide
        List<TaquinTileDTO> tileList = tg.getBoard().entrySet().stream()
                .map(entry -> {
                    Token token = entry.getValue();
                    int x = entry.getKey().x();
                    int y = entry.getKey().y();
                    String value = token.getName();
                    grid[y][x] = value;

                    // Autorisation de déplacement uniquement vers la case vide
                    Set<CellPosition> allowedMoves = Set.of();
                    if ((Math.abs(x - empty.x()) + Math.abs(y - empty.y())) == 1) {
                        allowedMoves = Set.of(empty);
                    }

                    return new TaquinTileDTO(value, x, y, allowedMoves);
                })
                .toList();

        // Construire le DTO complet
        return new TaquinGameDTO(
                tg.getId().toString(),
                tg.getStatus().name(),
                size,
                grid,
                tileList,
                empty.x(),
                empty.y()
        );
    }

    @Override
    public void play(Game game, int x, int y) throws InvalidPositionException {
        TaquinGame taquin = (TaquinGame) game;

        // Récupère la tuile sélectionnée à la position x,y
        Token selectedTile = taquin.getBoard().get(new CellPosition(x, y));

        if (selectedTile == null) {
            //throw new InvalidPositionException("No tile at the selected position");
            return;
        }

        // Vérifie si le déplacement vers la case vide est autorisé
        Set<CellPosition> allowedMoves = selectedTile.getAllowedMoves();

        if (allowedMoves.isEmpty()) {
            //throw new InvalidPositionException("Selected tile is not adjacent to empty space");
            return;
        }

        // Comme dans le Taquin il n’y a qu’une seule case vide, il y aura au maximum un allowedMove
        CellPosition destination = allowedMoves.iterator().next();

        // Déplace la tuile
        selectedTile.moveTo(destination);
    }
}
