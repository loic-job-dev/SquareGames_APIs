package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.dto.AbstractGameDTO;
import fr.campus.squaregamesapi.dto.PlayMoveDTO;
import fr.campus.squaregamesapi.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.campus.squaregamesapi.service.TicTacToeGameService;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private TicTacToeGameService ticTacToeGameService;

    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        // Créer le jeu via le moteur
        this.gameService.setGamePlugin(params.getGameType());
        Game game = this.gameService.createGame();

        // Sauvegarder le jeu dans la base via JPA
        if (game instanceof TicTacToeGame ttt) {
            ticTacToeGameService.saveGame(ttt);
        }

        return game.getId().toString(); // ID du jeu
    }

    @GetMapping("/sessions")
    public List<String> getGames() {
        return this.gameService.getSessions();
    }

    @GetMapping("/sessions/{gameId}/status")
    public String gameStatus(@PathVariable String gameId) {
        Game game = loadGameById(gameId);
        return game.getStatus().name();
    }

    @GetMapping("/sessions/{gameId}")
    public AbstractGameDTO gameInformations(@PathVariable String gameId) {
        Game game = loadGameById(gameId);
        return this.gameService.getGameDTO(game.getId().toString());
    }

    @PostMapping("/sessions/{gameId}")
    public AbstractGameDTO gamePlay(@PathVariable String gameId, @RequestBody PlayMoveDTO move) {
        TicTacToeGame game = (TicTacToeGame) loadGameById(gameId);

        try {
            AbstractGameDTO dto = this.gameService.playGame(game.getId().toString(), move.getX(), move.getY());

            // Après chaque coup, sauvegarder le jeu mis à jour
            ticTacToeGameService.saveGame(game);

            return dto;
        } catch (InvalidPositionException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible de jouer à cette position : " + e.getMessage()
            );
        }
    }

    // Méthode utilitaire pour charger un jeu depuis la DB
    private Game loadGameById(String gameId) {
        TicTacToeGame game = ticTacToeGameService.loadGame(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jeu non trouvé : " + gameId);
        }
        return game;
    }
}
