package fr.campus.squaregamesapi.service;

import fr.campus.squaregamesapi.interfaces.GamePlugin;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GameServiceImpl implements GamePlugin {

    @Override
    public Game createGame() {
        return null;
    }

    @Override
    public String getName(Locale locale) {
        return "";
    }
}
