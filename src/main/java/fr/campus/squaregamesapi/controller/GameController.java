package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.controller.games.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameFactoryCapable;
import fr.campus.squaregamesapi.service.GameCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GameController {

    @Autowired
    private GameFactoryCapable gameCreator;

    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        this.gameCreator.setGameFactory(params.getGameType());
        this.gameCreator.createGame(params.getPlayerCountParam(), params.getBoardSizeParam());
        return UUID.randomUUID().toString();
    }
}
