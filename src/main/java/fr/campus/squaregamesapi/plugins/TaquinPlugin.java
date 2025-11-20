package fr.campus.squaregamesapi.plugins;

import fr.campus.squaregamesapi.dto.GameDTO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

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

    @Override
    public GameDTO buildDTO(Game game) {
        return null;
    }

    @Override
    public void play(Game game, int x, int y) {

    }
}
