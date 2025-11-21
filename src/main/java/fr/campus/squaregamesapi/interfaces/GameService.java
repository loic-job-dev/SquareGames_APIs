package fr.campus.squaregamesapi.interfaces;

import fr.campus.squaregamesapi.dto.AbstractGameDTO;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameService {

    public void setGamePlugin(String gameId);

    public Game createGame();

    public List<String> getSessions();

    public Game getGame(String gameId);

    public String getGameStatus(String gameId);

    public AbstractGameDTO getGameDTO(String gameId);

    public AbstractGameDTO playGame(String gameId, int x, int y) throws InvalidPositionException;
}
