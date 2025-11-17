package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class GameServiceImpl implements GameService {

    private final List<GamePlugin> gamePlugins;
    private GamePlugin gamePlugin;

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
        return this.gamePlugin.createGame();
    }

    public String getName(Locale locale) {
        return this.gamePlugin.getName(locale);
    }
}
