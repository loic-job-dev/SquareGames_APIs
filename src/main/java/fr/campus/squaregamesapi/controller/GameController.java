package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.controller.games.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/sessions/{gameId}/status")
    public String gameStatus(@PathVariable("gameId") String gameId) {
        return this.gameService.getGameStatus(gameId);
    }
}
