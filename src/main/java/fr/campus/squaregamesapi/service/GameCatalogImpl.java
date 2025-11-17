package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GameCatalog;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class GameCatalogImpl implements GameCatalog {

    private final List<GameFactory> gameFactories;
    private GameFactory gameFactory;

    @Autowired
    public GameCatalogImpl(List<GameFactory> gameFactories) {
        this.gameFactories = gameFactories;
    }

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getGameIdentifiers() {
        String gameIdentifiers = "";
        for (GameFactory gameFactory : gameFactories) {
            gameIdentifiers += gameFactory.getGameFactoryId();
            gameIdentifiers += "\n";
        }
        return gameIdentifiers;
    }

    @Override
    public String getGameRules(Locale locale) {
        String gameRules =  "";
        for (GameFactory gameFactory : gameFactories) {
            String gameIdentifier = gameFactory.getGameFactoryId();
            switch(gameIdentifier) {
                case "tictactoe": {
                    gameRules += messageSource.getMessage("tictactoe.rules", null, locale);
                    gameRules += "\n";
                    break;
                }
                case "connect4": {
                    gameRules += messageSource.getMessage("connect4.rules", null, locale);
                    gameRules += "\n";
                    break;
                }
                case "15 puzzle": {
                    gameRules += messageSource.getMessage("15puzzle.rules", null, locale);
                    gameRules += "\n";
                    break;
                }
            }
        }
        return  gameRules;
    }
}
