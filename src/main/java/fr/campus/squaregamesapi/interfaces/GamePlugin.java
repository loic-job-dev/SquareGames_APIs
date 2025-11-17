package fr.campus.squaregamesapi.interfaces;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;

import java.util.Locale;

public interface GamePlugin {

    String getId();

    Game createGame();

    String getName(Locale locale);

}
