package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.dto.GameDTO;
import fr.campus.squaregamesapi.interfaces.GameDAO;
import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    private final List<GamePlugin> gamePlugins;
    private GamePlugin gamePlugin;

    //private final Map<String, Game> games = new HashMap<>();

    @Autowired
    private GameDAO mysqlGameDAO;

    @Autowired
    public GameServiceImpl(List<GamePlugin> gamePlugins) {
        this.gamePlugins = gamePlugins;
    }

    public void setGamePlugin(String gameId) {
        for (GamePlugin plugin : gamePlugins) {
            if (plugin.getId().equalsIgnoreCase(gameId)) {
                this.gamePlugin = plugin;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid game id: " + gameId);
    }

    public Game createGame() {
        Game game = this.gamePlugin.createGame();
        mysqlGameDAO.saveGame(game);
        //games.put(game.getId().toString(), game);
        return game;
    }

    public Game getGame(String gameId) {
        Game game = mysqlGameDAO.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return game;
        //return games.get(gameId);
    }

    public List<String> getSessions() {
        List<Game> games = this.mysqlGameDAO.getGames();
        List<String> gamesIds = new ArrayList<>();
        for (Game game : games) {
            String gameId = game.getId().toString();
            gamesIds.add(gameId);
        }
        return gamesIds;
    };

    public String getGameStatus(String gameId) {
        Game game = mysqlGameDAO.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return game.getStatus().toString();
    }

    public GameDTO getGameDTO(String gameId) {
        Game game = mysqlGameDAO.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getId().equalsIgnoreCase(game.getFactoryId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No plugin for factory: " + game.getFactoryId()));
        return plugin.buildDTO(game);
    }



    @Override
    public GameDTO playGame(String gameId, int j, int k) throws InvalidPositionException {
        Game game = mysqlGameDAO.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        // Trouver le plugin qui correspond à ce jeu
        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getId().equalsIgnoreCase(game.getFactoryId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No plugin found for game type"));

        plugin.play(game, j, k);
        mysqlGameDAO.saveGame(game);

        return plugin.buildDTO(game);
    }
}
