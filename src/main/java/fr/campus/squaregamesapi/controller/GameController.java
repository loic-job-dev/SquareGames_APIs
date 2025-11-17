package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.controller.games.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameFactoryCapable;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.campus.squaregamesapi.service.GameCreator;
import fr.le_campus_numerique.square_games.engine.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GameController {

    @Autowired
    private GameFactoryCapable gameCreator;

    @Autowired
    private GameService gameService;

    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        this.gameCreator.setGameFactory(params.getGameType());
        Game game = this.gameCreator.createGame(params.getPlayerCountParam(), params.getBoardSizeParam());
        //return UUID.randomUUID().toString();
        return game.getId().toString();
    }

    @GetMapping("/games/{gameId}")
    public Object getGame(@PathVariable String gameId) {
    // TODO - actually get and return game with id 'gameId'
        return "Game id: " + gameId;
    }
}
