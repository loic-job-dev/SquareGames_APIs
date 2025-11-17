package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.controller.games.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {


    @Autowired
    private GameService gameService;

    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        this.gameService.setGamePlugin(params.getGameType());
        this.gameService.createGame();
        return this.gameService.getName(LocaleContextHolder.getLocale());
    }

    @GetMapping("/games/{gameId}")
    public Object getGame(@PathVariable String gameId) {
    // TODO - actually get and return game with id 'gameId'
        return "Game id: " + gameId;
    }
}
