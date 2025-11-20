package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.dto.GameDTO;
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

    private final Map<String, Game> games = new HashMap<>();

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
        games.put(game.getId().toString(), game);
        return game;
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public String getGameStatus(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return game.getStatus().toString();
    }

    public GameDTO getGameDTO(String gameId) {
        Game game = games.get(gameId);
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
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        // Trouver le plugin qui correspond à ce jeu
        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getId().equalsIgnoreCase(game.getFactoryId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No plugin found for game type"));

        plugin.play(game, j, k);

        return plugin.buildDTO(game);
    }
}
