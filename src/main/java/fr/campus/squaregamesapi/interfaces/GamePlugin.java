package fr.campus.squaregamesapi.interfaces;

import fr.campus.squaregamesapi.dto.GameDTO;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;

import java.util.Locale;

public interface GamePlugin {

    String getId();

    Game createGame();

    String getName(Locale locale);

    GameDTO buildDTO(Game game);

    void play(Game game, int x, int y) throws InvalidPositionException;
}
