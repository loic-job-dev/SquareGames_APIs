package fr.campus.squaregamesapi.dao;

import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("collectionGameDAO")
public class CollectionGameDAO implements GameDAO {

    private final Map<String, Game> games = new HashMap<>();

    @Override
    public List<Game> getGames() {
        return List.copyOf(games.values());
    }

    @Override
    public Game getGameById(String gameId) {
        return games.get(gameId);
    }

    @Override
    public void saveGame(Game game) {
        games.put(game.getId().toString(), game);
    }


    @Override
    public void deleteGame(Game game) {
        games.remove(game.getId().toString());
    }
}
