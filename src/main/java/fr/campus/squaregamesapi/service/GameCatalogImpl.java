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
    public String getGamesRules(Locale locale) {
        String gamesRules =  "";
        for (GameFactory gameFactory : gameFactories) {
            String gameIdentifier = gameFactory.getGameFactoryId();
            switch(gameIdentifier) {
                case "tictactoe": {
                    gamesRules += messageSource.getMessage("tictactoe.rules", null, locale);
                    gamesRules += "\n";
                    break;
                }
                case "connect4": {
                    gamesRules += messageSource.getMessage("connect4.rules", null, locale);
                    gamesRules += "\n";
                    break;
                }
                case "15 puzzle": {
                    gamesRules += messageSource.getMessage("15puzzle.rules", null, locale);
                    gamesRules += "\n";
                    break;
                }
            }
        }
        return  gamesRules;
    }

    @Override
    public String getGameRules(Locale locale, String gameId) {
        String gamesRules =  "";
        switch(gameId) {
            case "tictactoe": {
                gamesRules += messageSource.getMessage("tictactoe.rules", null, locale);
                gamesRules += "\n";
                break;
            }
            case "connect4": {
                gamesRules += messageSource.getMessage("connect4.rules", null, locale);
                gamesRules += "\n";
                break;
            }
            case "15puzzle": {
                gamesRules += messageSource.getMessage("15puzzle.rules", null, locale);
                gamesRules += "\n";
                break;
            }
        }
        return  gamesRules;
    }
}
