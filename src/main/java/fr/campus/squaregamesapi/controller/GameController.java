package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.dto.GameDTO;
import fr.campus.squaregamesapi.dto.PlayMoveDTO;
import fr.campus.squaregamesapi.dto.tictactoe.TicTacToeCellPositionDTO;
import fr.campus.squaregamesapi.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class GameController {


    @Autowired
    private GameService gameService;

    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        this.gameService.setGamePlugin(params.getGameType());
        Game game = this.gameService.createGame();
        return game.getId().toString();// the id of the game
    }

    @GetMapping("/sessions")
    public List<String> getGames() {
        return this.gameService.getSessions();
    }

    @GetMapping("/sessions/{gameId}/status")
    public String gameStatus(@PathVariable("gameId") String gameId) {
        return this.gameService.getGameStatus(gameId);
    }

    @GetMapping("/sessions/{gameId}")
    public GameDTO gameInformations(@PathVariable("gameId") String gameId) {
        return this.gameService.getGameDTO(gameId);
    }

    @PostMapping("/sessions/{gameId}")
    public GameDTO gamePlay(@PathVariable("gameId") String gameId, @RequestBody PlayMoveDTO move) {
        try {
            return this.gameService.playGame(gameId, move.getX(), move.getY());
        } catch (InvalidPositionException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible de jouer à cette position : " + e.getMessage()
            );
        }
    }
}
