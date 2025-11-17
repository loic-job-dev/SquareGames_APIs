package fr.campus.squaregamesapi.plugins;

import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

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
    public Game createGame() {
        return gameFactory.createGame(defaultPlayerCount, defaultBoardSize);
    }

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("connect4.name", null, locale);
    }
}
