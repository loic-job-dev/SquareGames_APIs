package fr.campus.squaregamesapi.interfaces;

import fr.campus.squaregamesapi.controller.games.dto.tictactoe.TicTacToeGameDTO;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import org.springframework.stereotype.Service;

@Service
public interface GameService {

    public void setGamePlugin(String gameId);

    public Game createGame();

    public Game getGame(String gameId);

    public String getGameStatus(String gameId);

    public TicTacToeGameDTO getGameDTO(String gameId);

    public TicTacToeGameDTO playGame(String gameId, int x, int y) throws InvalidPositionException;
}
