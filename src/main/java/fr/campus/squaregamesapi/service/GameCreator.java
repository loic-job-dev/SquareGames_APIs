package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GameFactoryCapable;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameCreator implements GameFactoryCapable {

    private final List<GameFactory> gameFactories;
    private final Map<String, GameFactory> factories = new HashMap<>();
    private GameFactory gameFactory;

    // Constructor injection
    public GameCreator(List<GameFactory> gameFactories) {
        this.gameFactories = gameFactories;

        // Safe to iterate now
        for (GameFactory gameFactory : gameFactories) {
            factories.put(gameFactory.getGameFactoryId(), gameFactory);
        }
    }

    @Override
    public GameFactory getGameFactory(String id) {
        return factories.get(id);
    }

    @Override
    public void setGameFactory(String id) {
        this.gameFactory = factories.get(id);
    }

    @Override
    public Game createGame(int playerCount, int boardSize) {
        return gameFactory.createGame(playerCount, boardSize);
    }
}
