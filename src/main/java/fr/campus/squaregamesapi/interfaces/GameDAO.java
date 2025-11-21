package fr.campus.squaregamesapi.interfaces;

import fr.le_campus_numerique.square_games.engine.Game;

import java.util.List;

public interface GameDAO {
    public List<Game> getGames();
    public Game getGameById(String gameId);
    public void saveGame(Game game);
    public void deleteGame(String gameId);
}
