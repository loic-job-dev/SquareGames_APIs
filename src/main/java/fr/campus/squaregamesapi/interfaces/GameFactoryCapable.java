package fr.campus.squaregamesapi.interfaces;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameFactory;

public interface GameFactoryCapable {

    GameFactory getGameFactory(String id);

    void setGameFactory(String id);

    Game createGame(int playerCount, int boardSize);
}
