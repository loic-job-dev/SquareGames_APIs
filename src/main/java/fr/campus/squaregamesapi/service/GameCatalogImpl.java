package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GameCatalog;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameCatalogImpl implements GameCatalog {

    private final List<GameFactory> gameFactories;
    private GameFactory gameFactory;

    @Autowired
    public GameCatalogImpl(List<GameFactory> gameFactories) {
        this.gameFactories = gameFactories;
    }


    @Override
    public String getGameIdentifiers() {
        String gameIdentifiers = "";
        for (GameFactory gameFactory : gameFactories) {
            gameIdentifiers += gameFactory.getGameFactoryId();
            gameIdentifiers += "\n";
        }
        return gameIdentifiers;
    }
}
