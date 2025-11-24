package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.dto.AbstractGameDTO;
import fr.campus.squaregamesapi.dto.PlayMoveDTO;
import fr.campus.squaregamesapi.dto.GameCreationParams;
import fr.campus.squaregamesapi.interfaces.GameService;
import fr.campus.squaregamesapi.service.TicTacToeGameService;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(
            summary = "Create a new game",
            description = "Create a game choosing between different game types. Returns the game ID.",
            tags = {"Game", "Creation"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game created successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid game creation parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/games")
    public String createGame(@RequestBody GameCreationParams params) {
        this.gameService.setGamePlugin(params.getGameType());
        Game game = this.gameService.createGame();

        if (game instanceof TicTacToeGame ttt) {
            ticTacToeGameService.saveGame(ttt);
        }

        return game.getId().toString();
    }

    @Operation(
            summary = "Get all game sessions",
            description = "Retrieve a list of all active game session IDs",
            tags = {"Game", "Session"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of game sessions returned", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/sessions")
    public List<String> getGames() {
        return this.gameService.getSessions();
    }

    @Operation(
            summary = "Get game status",
            description = "Retrieve the status of a specific game by its ID",
            tags = {"Game", "Session"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game status returned", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/sessions/{gameId}/status")
    public String gameStatus(
            @Parameter(description = "ID of the game", required = true)
            @PathVariable String gameId
    ) {
        Game game = loadGameById(gameId);
        return game.getStatus().name();
    }

    @Operation(
            summary = "Get game information",
            description = "Retrieve detailed information of a specific game",
            tags = {"Game", "Session"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game information returned", content = @Content(schema = @Schema(implementation = AbstractGameDTO.class))),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/sessions/{gameId}")
    public AbstractGameDTO gameInformations(
            @Parameter(description = "ID of the game", required = true)
            @PathVariable String gameId
    ) {
        Game game = loadGameById(gameId);
        return this.gameService.getGameDTO(game.getId().toString());
    }

    @Operation(
            summary = "Play a move in a game",
            description = "Play a move in a TicTacToe game and return the updated game state",
            tags = {"Game", "Play"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Move played successfully, game state returned", content = @Content(schema = @Schema(implementation = AbstractGameDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid move", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @PostMapping("/sessions/{gameId}")
    public AbstractGameDTO gamePlay(
            @Parameter(description = "ID of the game", required = true)
            @PathVariable String gameId,
            @Parameter(description = "Move details", required = true)
            @RequestBody PlayMoveDTO move
    ) {
        TicTacToeGame game = (TicTacToeGame) loadGameById(gameId);

        try {
            AbstractGameDTO dto = this.gameService.playGame(game.getId().toString(), move.getX(), move.getY());
            ticTacToeGameService.saveGame(game);
            return dto;
        } catch (InvalidPositionException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Impossible de jouer à cette position : " + e.getMessage()
            );
        }
    }

    // Utility method to load game by ID
    private Game loadGameById(String gameId) {
        TicTacToeGame game = ticTacToeGameService.loadGame(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jeu non trouvé : " + gameId);
        }
        return game;
    }
}
