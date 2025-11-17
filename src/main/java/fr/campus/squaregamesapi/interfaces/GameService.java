package fr.campus.squaregamesapi.interfaces;

import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public interface GameService {

    public void setGamePlugin(String gameId);

    public Game createGame();

    public String getName(Locale locale);
}
