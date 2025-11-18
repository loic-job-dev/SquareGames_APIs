package fr.campus.squaregamesapi.interfaces;

import fr.campus.squaregamesapi.controller.games.dto.GameDTO;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public interface GameService {

    public void setGamePlugin(String gameId);

    public Game createGame();

    public Game getGame(String gameId);

    public String getGameStatus(String gameId);

    public GameDTO getGameDTO(String gameId);

    public GameDTO playGame(String gameId, int x, int y) throws InvalidPositionException;
}
